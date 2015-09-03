package com.github.niwaniwa.we.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.player.rank.RankProperty;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent event){
		WhitePlayer player = WhitePlayerFactory.newInstance(event.getPlayer());
		for(RankProperty p : RankProperty.values()){
			for(Rank r : Rank.getRanks()){
				if(r.getProperty().equals(p)){
					if(player.hasPermission(r.getPermission())){
						player.addRank(r);
					}
				}
			}
		}
	}

}
