package com.github.niwaniwa.we.core.player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.WhiteEggCore;

import net.sf.json.JSONObject;

public class WhitePlayerFactory {

	private WhitePlayerFactory(){}

	private static List<WhitePlayer> players = new ArrayList<>();

	public static WhitePlayer newInstance(Player player){
		for(WhitePlayer p : players){
			if(p.getUniqueId().equals(player.getUniqueId())){ return p; }
		}
		WhitePlayer white = new WhiteEggPlayer(player);
		white.reload();
		players.add(white);
		return white;
	}

	/**
	 * instance
	 * @param clazz 取得するクラス
	 * @param player プレイヤー
	 * @return T 指定したクラスのinstance
	 */
	@SuppressWarnings("unchecked")
	public static <T extends WhitePlayer> T newInstance(Class<T> clazz, Player player){
		if(clazz.isInterface()){ return null; }
		if(Modifier.isAbstract(clazz.getModifiers())){ return null; }
		if(clazz.getName().equals("WhitePlayer")){ return (T) WhitePlayerFactory.newInstance(player); }
		T instance = null;
		try {
			Constructor<T> c = clazz.getConstructor(clazz);
			instance = c.newInstance(player);
		} catch (NoSuchMethodException | SecurityException | InstantiationException |
				IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		if(instance == null){ return null; }
		instance.reload();
		return instance;
	}

	/**
	 * 型を指定したクラスへ変換する
	 * @param from 変換前のinstance
	 * @param to 変換後のクラス
	 * @return 変換したinstance
	 */
	public static <T extends WhitePlayer> WhitePlayer cast(T from, Class<T> to){
		WhitePlayer white = (WhitePlayer) from;
		white.save();
		if(to.getName().equals("WhitePlayer")){ return (WhitePlayer) from; }
		WhitePlayer instance = null;
		try {
			instance = (WhitePlayer) to.getConstructor(to).newInstance(white.getPlayer());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			return null; // >_<
		}
		instance.saveVariable(JSONObject.fromObject(white.serialize()));
		return instance;
	}

	public static void saveAll(){
		if(Bukkit.getOnlinePlayers().size() != 0){
			System.out.println("Saving players (WhiteEgg)");
			for(Player p : Bukkit.getOnlinePlayers()){
				WhitePlayerFactory.newInstance(p).save();
			}
		}
	}

	public static void reload(){
		if(Bukkit.getOnlinePlayers().size() == 0){ return; }
		for(WhitePlayer p : WhiteEggCore.getAPI().getOnlinePlayers()){
			p.reload();
		}
	}

	public static List<WhitePlayer> getPlayers(){
		return players;
	}

}
