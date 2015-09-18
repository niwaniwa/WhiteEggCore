package com.github.niwaniwa.we.core.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.niwaniwa.we.core.player.WhitePlayer;

/**
 * Twitter tweet event
 * @author niwaniwa
 * Minecraft内で使用不可能な文字列を含む場合があります
 */
public class WhiteEggPreTweetEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled = false;
	private WhitePlayer player;
	private String tweet;

	public WhiteEggPreTweetEvent(WhitePlayer player, String tweet) {
		this.player = player;
		this.tweet = tweet;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public WhitePlayer getPlayer() {
		return player;
	}

	public String getTweet() {
		return tweet;
	}

	public boolean setTweet(String tweet){
		if(tweet.length() == 0
				|| tweet.length() >= 140){ return false; }
		this.tweet = tweet;
		return true;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
