package com.github.niwaniwa.we.core.listener;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.event.WhiteEggPostTweetEvent;
import com.github.niwaniwa.we.core.event.WhiteEggPreTweetEvent;
import com.github.niwaniwa.we.core.player.WhitePlayer;

import twitter4j.TwitterException;

public class Debug implements Listener {

	public Debug(boolean isTest) {
		if(isTest){
			Bukkit.getPluginManager().registerEvents(this, WhiteEggCore.getInstance());
		}
	}

	/** debag
	 * @throws IOException **/
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		WhitePlayer player = WhiteEggCore.getAPI().getPlayer(event.getPlayer());
		if(event.getPlayer().getUniqueId().toString()
				.equalsIgnoreCase("f010845c-a9ac-4a04-bf27-61d92f8b03ff")){
			WhiteEggCore.getInstance().getLogger().info(
					"-- " + player.getPlayer().getName() + "Join the game. --");
		}
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
