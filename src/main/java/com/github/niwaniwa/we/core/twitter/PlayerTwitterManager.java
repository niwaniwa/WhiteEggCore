package com.github.niwaniwa.we.core.twitter;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public class PlayerTwitterManager extends TwitterManager {

	private WhitePlayer player;
	private boolean isSuccessfull = false;

	private PlayerTwitterManager() {
		super();
	}

	public PlayerTwitterManager(WhitePlayer p){
		this();
		this.player = p;
	}

	@Override
	public boolean tweet(String tweet) {
		if(!check(tweet)){ return false; }
		TweetTask task = new TweetTask(this, tweet);
		task.runTaskAsynchronously(WhiteEggCore.getInstance());
		return task.isSuccessfull();
	}

	@Override
	public boolean tweet(String[] tweet) {
		StringBuilder sb = new StringBuilder();
		for(String s : tweet){
			sb.append(s);
		}
		return tweet(sb.toString());
	}

	public WhitePlayer getPlayer(){
		return player;
	}

	/**
	 * 直前のツイートが送信されたか
	 * @return
	 */
	public boolean isSuccessfull() {
		return isSuccessfull;
	}

	protected void set(boolean s){
		this.isSuccessfull = s;
	}

}
