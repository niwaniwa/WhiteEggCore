package com.github.niwaniwa.we.core.listener;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.event.WhiteEggPostTweetEvent;
import com.github.niwaniwa.we.core.event.WhiteEggPreTweetEvent;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.util.lib.ActionBar;
import com.github.niwaniwa.we.core.util.lib.Tab;
import com.github.niwaniwa.we.core.util.lib.clickable.ChatExtra;
import com.github.niwaniwa.we.core.util.lib.clickable.ChatFormat;
import com.github.niwaniwa.we.core.util.lib.clickable.ClickEventType;
import com.github.niwaniwa.we.core.util.lib.clickable.Clickable;
import com.github.niwaniwa.we.core.util.lib.clickable.HoverEventType;

import twitter4j.TwitterException;

public class Debug implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent event) {
		final WhitePlayer player = WhiteEggCore.getAPI().getPlayer(event.getPlayer());
		if(event.getPlayer().getUniqueId().toString()
				.equalsIgnoreCase("f010845c-a9ac-4a04-bf27-61d92f8b03ff")){
			WhiteEggCore.getInstance().getLogger().info(
					"-- " + player.getPlayer().getName() + "Join the game. --");
		}
		Clickable click = new Clickable("Hellow! ->", ChatColor.GOLD, Arrays.asList(ChatFormat.BOLD));
		ChatExtra extra = new ChatExtra("PvP!", ChatColor.AQUA, Arrays.asList(ChatFormat.BOLD));
		extra.setClickEvent(ClickEventType.OPEN_URL, "https://twitter.com/");
		extra.setHoverEvent(HoverEventType.SHOW_TEXT, "ｳｪﾙｶﾑﾄｩｰｵｰｽ");
		click.addExtra(extra);
		click.send(player.getPlayer());

		Tab tab = new Tab("hi", "ihi");
		tab.send(player.getPlayer());
		actionBar(player);
	}

	private void actionBar(final WhitePlayer player){
		ActionBar actionBar = new ActionBar("§b>>>>>>>>>>>>>");
		actionBar.send(player.getPlayer());
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				if(!player.isOnline()){ this.cancel(); }
				switch(i){
				case 0:
					actionBar.setMessage("§6>§b>>>>>>>>>>>>");
					actionBar.send(player.getPlayer());
					break;
				case 1:
					actionBar.setMessage("§6>>§b>>>>>>>>>>>");
					actionBar.send(player.getPlayer());
					break;
				case 2:
					actionBar.setMessage("§6>>>§b>>>>>>>>>>");
					actionBar.send(player.getPlayer());
					break;
				case 3:
					actionBar.setMessage("§b>§6>>>§b>>>>>>>>>");
					actionBar.send(player.getPlayer());
					break;
				case 4:
					actionBar.setMessage("§b>§6>>>§b>>>>>>>>>");
					actionBar.send(player.getPlayer());
					break;
				case 5:
					actionBar.setMessage("§b>>§6>>>§b>>>>>>>>");
					actionBar.send(player.getPlayer());
					break;
				case 6:
					actionBar.setMessage("§b>>>§6>>>§b>>>>>>>");
					actionBar.send(player.getPlayer());
					break;
				case 7:
					actionBar.setMessage("§b>>>>§6>>>§b>>>>>>");
					actionBar.send(player.getPlayer());
					break;
				case 8:
					actionBar.setMessage("§b>>>>>§6>>>§b>>>>>");
					actionBar.send(player.getPlayer());
					break;
				case 9:
					actionBar.setMessage("§b>>>>>>§6>>>§b>>>>");
					actionBar.send(player.getPlayer());
					break;
				case 10:
					actionBar.setMessage("§b>>>>>>>§6>>>§b>>>");
					actionBar.send(player.getPlayer());
					break;
				case 11:
					actionBar.setMessage("§b>>>>>>>>§6>>>§b>>");
					actionBar.send(player.getPlayer());
					break;
				case 12:
					actionBar.setMessage("§b>>>>>>>>>§6>>>§b>");
					actionBar.send(player.getPlayer());
					break;
				case 13:
					actionBar.setMessage("§b>>>>>>>>>>§6>>>");
					actionBar.send(player.getPlayer());
					break;
				case 14:
					actionBar.setMessage("§b>>>>>>>>>>>§6>>");
					actionBar.send(player.getPlayer());
					break;
				case 15:
					actionBar.setMessage("§b>>>>>>>>>>>>§6>");
					actionBar.send(player.getPlayer());
					break;
				case 16:
					actionBar.setMessage("§b>>>>>>>>>>>>>");
					actionBar.send(player.getPlayer());
					break;
				default:
					actionBar.setMessage("§b>>>>>>>>>>>>>");
					actionBar.send(player.getPlayer());
					if(i == 30){
						i = -1;
					}
				}
				i++;
			}
		}.runTaskTimerAsynchronously(WhiteEggCore.getInstance(), 20, 1);
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
