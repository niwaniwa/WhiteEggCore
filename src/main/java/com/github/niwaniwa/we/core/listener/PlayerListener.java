package com.github.niwaniwa.we.core.listener;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.AltAccount;
import com.github.niwaniwa.we.core.player.WhiteEggPlayer;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.player.rank.RankProperty;

/**
 * 初期処理
 * @author niwaniwa
 *
 */
public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		WhitePlayer player = WhiteEggCore.getAPI().getPlayer(event.getPlayer());
		Arrays.asList(RankProperty.values()).forEach(property -> {
			 Rank.getRanks().stream().filter(r -> r.getProperty().equals(property))
			 					.forEach(rank -> { if (player.hasPermission(rank.getPermission())) { player.addRank(rank); }});
		});
		AltAccount.determine(player);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent event){
		WhitePlayer player = WhiteEggCore.getAPI().getPlayer(event.getPlayer());
		((WhiteEggPlayer) player).saveTask();
	}

	public void onKick(PlayerKickEvent event){
		WhitePlayer player = WhiteEggCore.getAPI().getPlayer(event.getPlayer());
		((WhiteEggPlayer) player).saveTask();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onSign(SignChangeEvent event) {
		for (int i = 0; i <= 3; i++) {
			event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));
		}
	}

}
