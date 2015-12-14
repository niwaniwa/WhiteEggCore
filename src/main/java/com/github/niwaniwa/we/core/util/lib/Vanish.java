package com.github.niwaniwa.we.core.util.lib;

import org.bukkit.Bukkit;

import com.github.niwaniwa.we.core.player.WhitePlayer;

public class Vanish {

	private Vanish(){}

	@SuppressWarnings("deprecation")
	public static boolean hide(WhitePlayer player){
		if(player.isVanish()){return false;}
		Bukkit.getOnlinePlayers().stream().filter(p -> !p.hasPermission("whiteegg.moderator"))
											.forEach(p -> p.hidePlayer(player.getPlayer()));
		player.setVanish(true);
		return true;
	}

	@SuppressWarnings("deprecation")
	public static boolean show(WhitePlayer player){
		if(!player.isVanish()){return false;}
		Bukkit.getOnlinePlayers().stream().filter(p -> !p.hasPermission("whiteegg.moderator"))
											.forEach(p -> p.showPlayer(player.getPlayer()));
		player.setVanish(false);
		return true;
	}

}
