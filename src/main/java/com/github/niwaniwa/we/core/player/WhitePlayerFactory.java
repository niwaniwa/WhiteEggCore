package com.github.niwaniwa.we.core.player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;

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

	private static final List<WhitePlayer> players = new ArrayList<>();
	private static boolean isLock = WhiteEggCore.isLock;

	/**
	 * プレイヤーのインスタンスを返します
	 * @param player プレイヤー
	 * @return プレイヤー
	 */
	public static WhitePlayer getInstance(Player player){
		if(isLock || !WhiteEggCore.getConf().getConfig().getBoolean("setting.player.enable", true)){ throw new IllegalStateException("Cannot use newInstance for data load."); }
		for(WhitePlayer p : players){
			if(p.getUniqueId().equals(player.getUniqueId())){
				((EggPlayer) p).update();
				return p;
			}
		}
		WhitePlayer white = new WhiteEggPlayer(player);
		white.load();
		players.add(white);
		return white;
	}

	/**
	 * instance
	 * @param <T> WhitePlayerを継承したクラス
	 * @param clazz 取得するクラス
	 * @param player プレイヤー
	 * @return T 指定したクラスのinstance
	 */
	public static <T extends WhitePlayer> T getInstance(Player player, Class<T> clazz){
		if(clazz.isInterface()){ throw new IllegalArgumentException(clazz.getSimpleName() + " is Interface"); }
		if(Modifier.isAbstract(clazz.getModifiers())){ throw new IllegalArgumentException(clazz.getSimpleName() + " is Abstract class"); }
		T instance = null;
		try {
			Constructor<T> c = clazz.getConstructor(Player.class);
			instance = c.newInstance(player);
		} catch (Exception e){
		}
		if(instance == null){ return null; }
		instance.load();
		return instance;
	}

	/**
	 * 型を指定したクラスへ変換する
	 * @param <T> WhitePlayerを継承したクラス
	 * @param from 変換前のinstance
	 * @param to 変換後のクラス
	 * @return 指定したクラスのinstance(データ引き継ぎ)
	 */
	public static <T extends WhitePlayer> T cast(WhitePlayer from, Class<T> to){
		if(Modifier.isAbstract(to.getModifiers())
				|| from.getClass().getSimpleName().equals(to.getSimpleName())){
			throw new IllegalArgumentException(to.getSimpleName() + " is Abstract class or Same as '" + to.getSimpleName() + "'");
		}
		from.save();
		T instance = null;
		try {
			instance = (T) to.getConstructor(Player.class).newInstance(from.getPlayer());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		instance.saveVariable(from.serialize().toString());
		if(instance.getClass().getSuperclass().getSimpleName().equalsIgnoreCase("EggPlayer")){
			((EggPlayer) instance).update();
		}
		return instance;
	}

	/**
	 * プレイヤーデータの保存
	 */
	public static void saveAll(){
		WhitePlayerFactory.getPlayers().forEach(p -> p.save());
	}

	/**
	 * プレイヤーデータの読み込み
	 */
	public static void load(){
		if(Bukkit.getOnlinePlayers().isEmpty()){ return; }
		new BukkitRunnable() {
			@Override
			public void run() {
				WhiteEggAPI.getOnlinePlayers().forEach(p -> p.reload());
			}
		}.runTaskAsynchronously(WhiteEggCore.getInstance());
	}

	/**
	 * サーバー起動時から取得時までに生成されたプレイヤーのインスタンスの取得
	 * @return instance
	 */
	public static List<WhitePlayer> getPlayers(){
		return players;
	}

}
