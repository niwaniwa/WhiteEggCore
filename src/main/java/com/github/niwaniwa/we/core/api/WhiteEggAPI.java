package com.github.niwaniwa.we.core.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.niwaniwa.we.core.player.OfflineWhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.rank.Rank;

/**
 * WhiteEggCoreのAPIクラス
 * @author niwaniwa
 *
 */
public abstract class WhiteEggAPI {

	private static WhiteEggAPIImpl instance = new WhiteEggAPIImpl();

	/**
	 * 現在のオンラインプレイヤーを返す
	 * @return List 現在接続しているプレイヤーを取得
	 */
	public static List<WhitePlayer> getOnlinePlayers(){ return instance.getOnlinePlayers(); }

	/**
	 * 現在登録されているランクを返す
	 * @return List 現在サーバーに登録されているランクの取得
	 */
	public static List<Rank> getRanks(){ return instance.getRanks(); }

	/**
	 * RankBroadcast
	 * @param rank rank
	 * @param message message
	 */
	public static void RankBroadcastMessage(Rank rank, String message){ instance.RankBroadcastMessage(rank, message); }

	/**
	 * WorldBroadcast
	 * @param world world
	 * @param message message
	 */
	public static void WorldBroadcastMessage(World world, String message){ instance.WorldBroadcastMessage(world, message); }

	/**
	 * オフラインのプレイヤーインスタンスを返す
	 * @param name Minecraft ID
	 * @return OfflineWhitePlayer
	 * @deprecated
	 */
	public static OfflineWhitePlayer getOfflinePlayer(String name){ return instance.getOfflinePlayer(name); }

	/**
	 *
	 * @param name プレイヤー名
	 * @return WhitePlayer
	 *
	 */
	public static WhitePlayer getPlayer(String name){ return instance.getPlayer(name); }

	/**
	 * uuidからオンラインプレイヤーを取得する
	 * @param uuid プレイヤーのUUID
	 * @return WhitePlayer プレイヤー
	 *
	 */
	public static WhitePlayer getPlayer(UUID uuid){ return instance.getPlayer(uuid); }

	/**
	 * プレイヤーを取得します
	 * @param player プレイヤー
	 * @return WhitePlayer プレイヤー
	 */
	public static WhitePlayer getPlayer(Player player){ return instance.getPlayer(player); }

	/**
	 * 新たな設定を追加します
	 * @param plugin プラグイン
	 * @param tag 設定のタグ
	 * @param permission 権限
	 * @param custam 任意の名前
	 * @param toggles 設定内容
	 * @param isHide デフォルトで表示させるか(true = permissionが無効)
	 * @return 成功したか
	 * @deprecated 使用しないでください
	 */
	public static boolean registerToggle(Plugin plugin, String tag, String permission,
			String custam, Map<String, Object> toggles, boolean isHide){ return instance.registerToggle(plugin, tag, permission, custam, toggles, isHide); }

	/**
	 * データベースを使用するか
	 * @return データベースを使用するか
	 */
	public static boolean useDataBase(){ return instance.useDataBase(); }

	/**
	 * ツイートを送信します
	 * @param tweet ツイートする文字列
	 */
	public static void tweet(String tweet){ instance.tweet(tweet); }

	/**
	 * APIインスタンスの取得
	 * @return WhiteEggAPI instance
	 */
	public static WhiteEggAPIImpl getAPI(){
		return instance;
	}
}
