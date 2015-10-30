package com.github.niwaniwa.we.core.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

public abstract class Extension {

	public abstract void send(Player player);

	protected IChatBaseComponent build(String source){
		return ChatSerializer.a("{\"text\": \""+source+"\"}");
	}

	protected CraftPlayer cast(Player p){
		if (!(p instanceof Player)) {return null;}
		return (CraftPlayer) p;
	}

	public static <T extends Extension> void permissionBroadcast(String prm, T t){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasPermission(prm)){
				t.send(p);
			}
		}
	}
	public static <T extends Extension> void worldBroadcast(World world, T t){
		world.getPlayers().forEach(player -> t.send(player));
	}

	public static <T extends Extension> void serverBroadcast(T t){
		Bukkit.getOnlinePlayers().forEach(player -> t.send(player));
	}

}
