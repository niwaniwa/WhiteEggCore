package com.github.niwaniwa.we.core.twitter;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.Callback;

public class ServerTwitterManager extends TwitterManager {

	private static ServerTwitterManager manager = new ServerTwitterManager();

	private ServerTwitterManager() {
		super();
	}

	@Override
	public void tweet(String tweet, Callback callback) {
		if(!check(tweet)){ return; }
		TweetTask task = new TweetTask(this, tweet, callback);
		task.runTaskAsynchronously(WhiteEggCore.getInstance());
	}

	@Override
	public void tweet(String[] tweet) {
		StringBuilder sb = new StringBuilder();
		for(String s : tweet){
			sb.append(s);
		}
		tweet(sb.toString());
	}

	public static ServerTwitterManager getInstance(){
		return manager;
	}

}
