package com.github.niwaniwa.we.core.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.niwaniwa.we.core.command.toggle.type.ToggleType;
import com.github.niwaniwa.we.core.player.OfflineWhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.rank.Rank;

/**
 * WhiteEggCoreのAPIクラス
 * @author niwaniwa
 *
 */
public abstract class WhiteEggAPI {

	/**
	 * 現在のオンラインプレイヤーを返す
	 * @return List 現在接続しているプレイヤーを取得
	 */
	public abstract List<WhitePlayer> getOnlinePlayers();

	/**
	 * 現在登録されているランクを返す
	 * @return List 現在サーバーに登録されているランクの取得
	 */
	public abstract List<Rank> getRanks();

	/**
	 * RankBroadcast
	 * @param rank rank
	 * @param message message
	 */
	public abstract void RankBroadcastMessage(Rank rank, String message);

	/**
	 * WorldBroadcast
	 * @param world world
	 * @param message message
	 */
	public abstract void WorldBroadcastMessage(World world, String message);

	/**
	 * オフラインのプレイヤーインスタンスを返す
	 * @param name Minecraft ID
	 * @return OfflineWhitePlayer
	 * @deprecated
	 */
	public abstract OfflineWhitePlayer getOfflinePlayer(String name);

	/**
	 *
	 * @param name プレイヤー名
	 * @return WhitePlayer
	 *
	 */
	public abstract WhitePlayer getPlayer(String name);

	/**
	 * uuidからオンラインプレイヤーを取得する
	 * @param uuid プレイヤーのUUID
	 * @return WhitePlayer プレイヤー
	 *
	 */
	public abstract WhitePlayer getPlayer(UUID uuid);

	/**
	 * プレイヤーを取得します
	 * @param player プレイヤー
	 * @return WhitePlayer プレイヤー
	 */
	public abstract WhitePlayer getPlayer(Player player);

	/**
	 * 新たな設定を追加します
	 * @param plugin プラグイン
	 * @param type タイプ
	 * @param permission 権限
	 * @param custam 任意の名前
	 * @param toggles 設定内容
	 * @param isHide デフォルトで表示させるか(true = permissionが無効)
	 * @return 成功したか
	 * @deprecated 使用しないでください
	 */
	public abstract boolean registerToggle(Plugin plugin, ToggleType type, String permission,
			String custam, Map<String, Object> toggles, boolean isHide);

	/**
	 * データベースを使用するか
	 * @return データベースを使用するか
	 */
	public abstract boolean useDataBase();

	/**
	 * ツイートを送信します
	 * @param tweet ツイートする文字列
	 */
	public abstract void tweet(String tweet);
}
