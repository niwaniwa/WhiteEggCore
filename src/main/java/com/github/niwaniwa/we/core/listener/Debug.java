package com.github.niwaniwa.we.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.event.WhiteEggPostTweetEvent;
import com.github.niwaniwa.we.core.event.WhiteEggPreTweetEvent;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.util.bar.Bar;

import twitter4j.TwitterException;

public class Debug implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		WhitePlayer player = WhiteEggCore.getAPI().getPlayer(event.getPlayer());
		if(event.getPlayer().getUniqueId().toString()
				.equalsIgnoreCase("f010845c-a9ac-4a04-bf27-61d92f8b03ff")){
			WhiteEggCore.getInstance().getLogger().info(
					"-- " + player.getPlayer().getName() + "Join the game. --");
		}
		Bar.setDragon(player, "play.kokekokko.jp", 10);
	}

	@EventHandler
	public void onTweet(WhiteEggPreTweetEvent event){
		System.out.println(" : Event " + event.getEventName() + " : "
				+ " Tweet " + event.getTweet() + " : Player " + event.getPlayer().getFullName());
	}

	@EventHandler
	public void potTweetEvent(WhiteEggPostTweetEvent event) throws IllegalStateException, TwitterException{
		System.out.println(" : Event " + event.getEventName() + " : "
				+ " Tweet " + event.getStatus().getText()
				+ " : Twitter ID " + event.getTwitter().getTwitter().getScreenName());
	}

}
