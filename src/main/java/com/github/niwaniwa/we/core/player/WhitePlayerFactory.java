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

/**
 * WhitePlayerのfactoryクラス
 * @author niwaniwa
 *
 */
public class WhitePlayerFactory {

	/**
	 * instance化不可
	 */
	private WhitePlayerFactory(){}

	private static List<WhitePlayer> players = new ArrayList<>();

	/**
	 * プレイヤーのインスタンスを返します
	 * @param player プレイヤー
	 * @return プレイヤー
	 */
	public static WhitePlayer newInstance(Player player){
		for(WhitePlayer p : players){
			if(p.getUniqueId().equals(player.getUniqueId())){ return p; }
		}
		WhitePlayer white = new WhiteEggPlayer(player);
		white.load();
		players.add(white);
		return white;
	}

	/**
	 * instance
	 * @param clazz 取得するクラス
	 * @param player プレイヤー
	 * @return T 指定したクラスのinstance
	 * @deprecated エラーが起こる可能性が高いので使わないでください
	 */
	public static <T extends WhitePlayer> WhitePlayer newInstance(Class<T> clazz, Player player){
		if(clazz.isInterface()){ return null; }
		if(Modifier.isAbstract(clazz.getModifiers())){ return null; }
		if(clazz.getName().equals("WhitePlayer")){ return WhitePlayerFactory.newInstance(player); }
		T instance = null;
		try {
			Constructor<T> c = clazz.getConstructor(Player.class);
			instance = c.newInstance(player);
		} catch (NoSuchMethodException | SecurityException | InstantiationException |
				IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		if(instance == null){ return null; }
		instance.load();
		return instance;
	}

	/**
	 * 型を指定したクラスへ変換する
	 * @param from 変換前のinstance
	 * @param to 変換後のクラス
	 * @return 指定したクラスのinstance(データ引き継ぎ)
	 * @deprecated
	 */
	public static <T extends WhitePlayer> WhitePlayer cast(WhitePlayer from, Class<T> to){
		if(Modifier.isAbstract(to.getModifiers())
				|| from.getClass().equals(to)){
			return null;
		}
		from.save();
		WhitePlayer instance = null;
		try {
			Constructor<?> constructor = to.getConstructor(Player.class);
			instance = (WhitePlayer) (constructor == null ? to.getConstructor(WhitePlayer.class).newInstance(from) : constructor.newInstance(from.getPlayer()));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
		}
		instance.saveVariable(JSONObject.fromObject(from.serialize()));
		return instance;
	}

	/**
	 * プレイヤーデータの保存
	 */
	public static void saveAll(){
		System.out.println("Saving players (WhiteEgg)");
		for(WhitePlayer p : WhitePlayerFactory.getPlayers()){
			p.save();
		}
	}

	/**
	 * プレイヤーデータの読み込み
	 */
	public static void load(){
		if(Bukkit.getOnlinePlayers().size() == 0){ return; }
		for(WhitePlayer p : WhiteEggCore.getAPI().getOnlinePlayers()){
			p.reload();
		}
	}

	/**
	 * サーバー起動時から取得時までのプレイヤーのインスタンスの取得
	 * @return instance
	 */
	public static List<WhitePlayer> getPlayers(){
		return players;
	}

}
