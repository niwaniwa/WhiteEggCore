package com.github.niwaniwa.we.core.twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import com.github.niwaniwa.we.core.WhiteEggCore;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.api.callback.Callback;
import com.github.niwaniwa.we.core.event.WhiteEggPostTweetEvent;
import com.github.niwaniwa.we.core.event.WhiteEggPreTweetEvent;
import com.github.niwaniwa.we.core.util.Util;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

/**
 * ツイートの投稿クラス
 *
 * @author mmott
 */
public class TweetScheduler extends BukkitRunnable {

    private TwitterManager manager;
    private String tweet;
    private List<Callback> callback;

    /**
     * コンストラクター
     *
     * @param manager TwitterManager
     * @param tweet   ツイート
     */
    public TweetScheduler(TwitterManager manager, String tweet) {
        this.manager = manager;
        this.tweet = tweet;
        this.callback = new ArrayList<>(0);
    }

    /**
     * コンストラクター
     *
     * @param manager   TwitterManager
     * @param tweet     ツイート
     * @param callbacks ツイート後に実行する
     */
    public TweetScheduler(TwitterManager manager, String tweet, Callback... callbacks) {
        this.manager = manager;
        this.tweet = tweet;
        this.callback = Arrays.asList(callbacks);
    }

    public TwitterManager getManager() {
        return manager;
    }

    public String getTweet() {
        return tweet;
    }

    public boolean tweet() {
        if (!this.callPreEvent()) {
            return false;
        }
        StatusUpdate statusUpdate = TwitterMediaUploader.checkMediaURL(tweet) == true ? null : new StatusUpdate(tweet);
        if (statusUpdate == null) {
            List<String> urls = this.getURL();
            statusUpdate = new StatusUpdate(tweet);
            TwitterMediaUploader uploader = new TwitterMediaUploader(manager.getTwitter(), statusUpdate, urls);
            uploader.upload();
        }
        Status status = null;
        try {
            status = manager.getTwitter().updateStatus(statusUpdate);
        } catch (TwitterException e) {
            WhiteEggCore.logger.warning(e.getMessage());
            return false;
        }
        WhiteEggPostTweetEvent postEvent = new WhiteEggPostTweetEvent(manager, status);
        Util.callEvent(postEvent);
        return true;
    }

    private List<String> getURL() {
        List<String> result = new ArrayList<>();
        final Matcher matcher = TwitterMediaUploader.urlPattern.matcher(tweet);
        while (matcher.find()) {
            String url = matcher.group();
            if (TwitterMediaUploader.checkMediaExtension(url)) {
                tweet.replace(url, "");
                result.add(url);
            }
        }
        return result;
    }

    private boolean callPreEvent() {
        WhiteEggPreTweetEvent preEvent;
        if (manager instanceof PlayerTwitterManager) {
            preEvent = new WhiteEggPreTweetEvent(((PlayerTwitterManager) manager).getPlayer(), tweet);
        } else {
            preEvent = new WhiteEggPreTweetEvent(null, tweet);
        }
        Util.callEvent(preEvent);
        if (preEvent.isCancelled()) {
            return false;
        }
        tweet = preEvent.getTweet();
        return true;
    }

    @Override
    public void run() {
        callback.forEach(c -> c.onTwitter(tweet()));
    }

}
