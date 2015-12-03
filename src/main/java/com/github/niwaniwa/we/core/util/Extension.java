package com.github.niwaniwa.we.core.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

public abstract class Extension {

	/**
	 * プレイヤーにパケットなどを送信します
	 * @param player 対象プレイヤー
	 */
	public abstract void send(Player player);

	protected IChatBaseComponent build(String source){
		return ChatSerializer.a("{\"text\": \""+source+"\"}");
	}

	protected CraftPlayer cast(Player p){
		if (!(p instanceof Player)) {return null;}
		return (CraftPlayer) p;
	}

	/**
	 * 指定した権限を所持しているプレイヤーのみに送信します
	 * @param prm 権限
	 * @param t Extension
	 */
	public static void permissionBroadcast(String prm, Extension t){
		Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission(prm)).forEach(p -> t.send(p));
	}

	/**
	 * 指定したワールドにいるプレイヤーに送信します
	 * @param world world
	 * @param t Extension
	 */
	public static void worldBroadcast(World world, Extension t){
		world.getPlayers().forEach(player -> t.send(player));
	}

	/**
	 * すべてのプレイヤーに送信します
	 * @param t Extension
	 */
	public static void serverBroadcast(Extension t){
		Bukkit.getOnlinePlayers().forEach(player -> t.send(player));
	}

}
