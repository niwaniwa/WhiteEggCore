package com.github.niwaniwa.we.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
		for (RankProperty p : RankProperty.values()) {
			for (Rank r : Rank.getRanks()) {
				if (r.getProperty().equals(p)) {
					if (player.hasPermission(r.getPermission())) {
						player.addRank(r);
					}
				}
			}
		}
		AltAccount.determine(player);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent event){
		WhitePlayer player = WhiteEggCore.getAPI().getPlayer(event.getPlayer());
		((WhiteEggPlayer) player).saveTask();
	}

}
