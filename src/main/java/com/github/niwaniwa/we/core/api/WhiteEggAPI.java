package com.github.niwaniwa.we.core.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.niwaniwa.we.core.player.OfflineWhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.rank.Rank;

public abstract class WhiteEggAPI {

	/**
	 * 現在のオンラインプレイヤーを返す
	 * @return List<WhitePlayer>
	 */
	public abstract List<WhitePlayer> getOnlinePlayers();

	/**
	 * 現在登録されているランクを返す
	 * @return List<Rank>
	 */
	public abstract List<Rank> getRanks();

	/**
	 * RankBroadcast
	 * @param message message
	 */
	public abstract void RankBroadcastMessage(Rank rank, String message);

	public abstract void WorldBroadcastMessage(World world, String message);

	/**
	 * オフラインのプレイヤーインスタンスを返す
	 * @param name Minecraft ID
	 * @return OfflineWhitePlayer
	 */
	public abstract OfflineWhitePlayer getOfflinePlayer(String name);

	/**
	 *
	 * @param name
	 * @return
	 * @deprecated {@link #getPlayer(Player)}
	 */
	public abstract WhitePlayer getPlayer(String name);

	/**
	 * uuidからオンラインプレイヤーを取得する
	 * @param uuid
	 * @return WhitePlayer
	 * @deprecated {@link #getPlayer(Player)}
	 */
	public abstract WhitePlayer getPlayer(UUID uuid);

	public abstract WhitePlayer getPlayer(Player player);


	/**
	 * 新たな設定を追加します
	 * @param plugin プラグイン
	 * @param permission Permission
	 * @param toggle 設定内容
	 * @param isDefault デフォルトで表示させるか(true = permissionが無効)
	 * @return
	 */
	public abstract <T extends JavaPlugin> boolean registerToggle(T plugin, String permission, Map<String,Object> toggle, boolean isDefault, boolean hide, boolean add);

	public abstract boolean useDataBase();
}
