package com.github.niwaniwa.we.core.twitter;

import java.util.Arrays;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.callback.Callback;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public class PlayerTwitterManager extends TwitterManager {

	private WhitePlayer player;

	public PlayerTwitterManager(WhitePlayer p){
		super();
		this.player = p;
	}

	@Override
	public void tweet(String tweet, Callback callback) {
		TweetTask task = new TweetTask(this, tweet, callback);
		task.runTaskAsynchronously(WhiteEggCore.getInstance());
	}

	@Override
	public void tweet(String[] tweet) {
		StringBuilder sb = new StringBuilder();
		Arrays.asList(tweet).forEach(s -> sb.append(s));
		tweet(sb.toString());
	}

	public WhitePlayer getPlayer(){
		return player;
	}

}
