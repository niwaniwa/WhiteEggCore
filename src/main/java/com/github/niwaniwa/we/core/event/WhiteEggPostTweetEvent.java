package com.github.niwaniwa.we.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.niwaniwa.we.core.twitter.PlayerTwitterManager;
import com.github.niwaniwa.we.core.twitter.TwitterManager;

import twitter4j.Status;

/**
 * ツイート後のイベント」
 * @author niwaniwa
 *
 */
public class WhiteEggPostTweetEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private TwitterManager twitter;
	private Status status;
	private boolean successfull;

	public WhiteEggPostTweetEvent(TwitterManager twitter, Status tweet, boolean successfull){
		this.twitter = twitter;
		this.status = tweet;
		this.successfull = successfull;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public TwitterManager getTwitter() {
		return twitter;
	}

	public Status getStatus() {
		return status;
	}

	public boolean isPlayer(){
		return (twitter instanceof PlayerTwitterManager);
	}

	public boolean isSuccessfull() {
		return successfull;
	}

}
