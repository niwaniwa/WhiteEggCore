package com.github.niwaniwa.we.core.listener;

import org.bson.Document;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.database.mongodb.MongoDataBaseCollection;
import com.github.niwaniwa.we.core.database.mongodb.MongoDataBaseManager;
import com.github.niwaniwa.we.core.database.mongodb.MongoDataBaseManager.MongoDataBase;
import com.github.niwaniwa.we.core.event.WhiteEggPostTweetEvent;
import com.github.niwaniwa.we.core.event.WhiteEggPreTweetEvent;
import com.github.niwaniwa.we.core.player.WhitePlayer;

import twitter4j.TwitterException;

public class Debug implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent event) {
		final WhitePlayer player = WhiteEggAPI.getPlayer(event.getPlayer());
		if(event.getPlayer().getUniqueId().toString()
				.equalsIgnoreCase("f010845c-a9ac-4a04-bf27-61d92f8b03ff")){
			WhiteEggCore.getInstance().getLogger().info(
					"-- " + player.getPlayer().getName() + "Join the game. --");
		}
		// database start
		MongoDataBaseManager manager = new MongoDataBaseManager("192.168.99.100", 32768);
		MongoDataBase database = manager.getDatabase("WhiteEgg");
		MongoDataBaseCollection collection = new MongoDataBaseCollection(manager, database, "player");
//		Document uuid = new Document().append("uuid", player.getUniqueId().toString());
		Document playerData = new Document().append("player", new Document(player.serialize())).append("uuid", player.getUniqueId().toString());
//		Document updatedoc = new Document("$addToSet", playerData);
		collection.getCollection().insertOne(playerData);
		// database end
	}

	@EventHandler
	public void onTweet(WhiteEggPreTweetEvent event){
		System.out.println(" : Event " + event.getEventName() + " : "
				+ " Tweet " + event.getTweet() + " : Player " + event.getPlayer().getFullName());
	}

	@EventHandler
	public void postTweetEvent(WhiteEggPostTweetEvent event) throws IllegalStateException, TwitterException{
		System.out.println(" : Event " + event.getEventName() + " : "
				+ " Tweet " + event.getStatus().getText()
				+ " : Twitter ID " + event.getTwitter().getTwitter().getScreenName());
	}

}
