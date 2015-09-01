package com.github.niwaniwa.we.core.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.player.WhitePlayer;

public class Vanish {

	private Vanish(){}

	public static boolean hide(WhitePlayer player){

		if(player.isVanish()){return false;}

		for(Player p : Bukkit.getOnlinePlayers()){
			if(!p.hasPermission("whiteegg.moderator")){
				p.hidePlayer(player.getPlayer());
			}
		}

		player.setVanish(true);

		return true;

	}

	public static boolean show(WhitePlayer player){

		if(!player.isVanish()){return false;}

		for(Player p : Bukkit.getOnlinePlayers()){
			p.showPlayer(player.getPlayer());
		}

		player.setVanish(false);

		return true;

	}

}
