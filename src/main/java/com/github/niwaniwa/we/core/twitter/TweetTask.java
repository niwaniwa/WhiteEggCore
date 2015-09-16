package com.github.niwaniwa.we.core.twitter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.event.WhiteEggTweetEvent;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TweetTask extends BukkitRunnable {

	private static final String URL_REGEX = "https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+";

	private TwitterManager twitter;
	private String tweet;
	private String url;
	private boolean media;
	private List<Status> status;
	private boolean successfull = false;

	public TweetTask(TwitterManager twitter, String tweet){
		this.twitter = twitter;
		this.tweet = tweet;
		this.url = "";
		media = false;
		status = new ArrayList<>();
	}

	/**  **/
	private boolean contains(String tweet){
		return tweet.contains(URL_REGEX);
	}

	@Override
	public void run() {
		try {
			tweet();
		} catch (TwitterException e) {
		}
	}

	public void tweet() throws TwitterException{
		Twitter t = twitter.getTwitter();
		if(twitter instanceof PlayerTwitterManager){
			PlayerTwitterManager pt = (PlayerTwitterManager) twitter;
			WhiteEggTweetEvent event = new WhiteEggTweetEvent(pt.getPlayer(), tweet);
			if(event.isCancelled()){
				return;
			}
			tweet = event.getTweet();
		}
		StatusUpdate su = new StatusUpdate(tweet);
		Status s = t.updateStatus(su);
		if(s != null){
			this.successfull = true;
		}
		status.add(s);
	}

	public boolean isSuccessfull() {
		return successfull;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}

	public boolean isMedia() {
		return media;
	}

	public void setMedia(boolean media) {
		this.media = media;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTweet() {
		return tweet;
	}

}
