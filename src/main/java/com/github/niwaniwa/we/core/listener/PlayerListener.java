package com.github.niwaniwa.we.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.player.SubAccount;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.player.rank.RankProperty;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		WhitePlayer player = WhitePlayerFactory.newInstance(event.getPlayer());
		for (RankProperty p : RankProperty.values()) {
			for (Rank r : Rank.getRanks()) {
				if (r.getProperty().equals(p)) {
					if (player.hasPermission(r.getPermission())) {
						player.addRank(r);
					}
				}
			}
		}
		SubAccount.determine(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onHighJoin(PlayerJoinEvent event){
		for (WhitePlayer p : WhiteEggCore.getAPI().getOnlinePlayers()) {
			if (Boolean.parseBoolean(String.valueOf(ToggleSettings.getValue(p, "loginmsg")))) {
				p.sendMessage(event.getJoinMessage());
			}
		}
		event.setJoinMessage("");
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent event){
//		String command = event.getMessage().split(" ")[0].replaceAll("/", "");
	}

}
