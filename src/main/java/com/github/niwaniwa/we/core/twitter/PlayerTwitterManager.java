package com.github.niwaniwa.we.core.twitter;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.Callback;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public class PlayerTwitterManager extends TwitterManager {

	private WhitePlayer player;
	private boolean isSuccessfull = false;

	public PlayerTwitterManager(WhitePlayer p){
		super();
		this.player = p;
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

	public WhitePlayer getPlayer(){
		return player;
	}

	@Override
	public boolean isSuccessfull() {
		return isSuccessfull;
	}

	/**
	 * set
	 * @param s 成功したか
	 * @deprecated
	 */
	public void set(boolean s){
		this.isSuccessfull = s;
	}

}
