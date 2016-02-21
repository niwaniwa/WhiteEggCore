package com.github.niwaniwa.we.core.twitter;

import java.util.Arrays;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.callback.Callback;

public class ServerTwitterManager extends TwitterManager {

    private static ServerTwitterManager manager = new ServerTwitterManager();

    private ServerTwitterManager() {
        super();
    }

    @Override
    public void tweet(String tweet, Callback callback) {
        TweetScheduler task = new TweetScheduler(this, tweet, callback);
        task.runTaskAsynchronously(WhiteEggCore.getInstance());
    }

    @Override
    public void tweet(String[] tweet) {
        StringBuilder sb = new StringBuilder();
        Arrays.asList(tweet).forEach(s -> sb.append(s));
        tweet(sb.toString());
    }

    public static ServerTwitterManager getInstance() {
        return manager;
    }

}
