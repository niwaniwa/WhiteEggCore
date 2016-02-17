package com.github.niwaniwa.we.core.player;

import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.twitter.TwitterManager;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Playerクラス
 * @author KokekoKko_
 *
 */
public interface WhitePlayer extends OfflineWhitePlayer, WhiteCommandSender {

	/**
	 * 現在登録されているランクを返します
	 * @return List rank
	 */
	public abstract List<Rank> getRanks();

	/**
	 * rankを追加します
	 * @param rank rank
	 * @return 成功したか
	 */
	public abstract boolean addRank(Rank rank);

	/**
	 * rankを削除します
	 * @param rank rank
	 * @return 削除されたか
	 */
	public abstract boolean removeRank(Rank rank);

	/**
	 * 現在vanish中か返します
	 * @return boolean
	 */
	public abstract boolean isVanish();

	/**
	 * OP権限が付与されているか
	 * @return op
	 */
	public abstract boolean isOp();

	/**
	 * vanishします
	 * @return 成功したか
	 * @deprecated
	 */
	public abstract boolean vanish();

	/**
	 * 姿を現します
	 * @return 成功したか
	 * @deprecated
	 */
	public abstract boolean show();

	/**
	 * フラグの変更
	 * @param b boolean
	 * @deprecated
	 */
	public abstract void setVanish(boolean b);

	/**
	 * jsonからデータを読み込み、変数を変更します
	 * @param jsonString json
	 * @return 成功したか
	 */
	public abstract boolean saveVariable(String jsonString);

	/**
	 * データのリロードをします
	 * @return 成功したか
	 */
	public abstract boolean reload();

	/**
	 * データのロードをします
	 * @return 成功したか
	 */
	public abstract boolean load();

	/**
	 * データをセーブします。local、databaseによって処理が違います
	 * @return 成功したか
	 */
	public abstract boolean save();

	/**
	 * TwitterManagerクラスを返します
	 * @return twittermanager
	 */
	public abstract TwitterManager getTwitterManager();

	/**
	 * 現在の設定を返します
	 * @return List ToggleSettings
	 */
	public abstract List<ToggleSettings> getToggleSettings();

	/**
	 * 現在接続しているプレイヤーのアドレスを返します
	 * @return Address
	 */
	public abstract InetSocketAddress getAddress();

	/**
	 * EntityPlayerを返します
	 * @return EntityPlayer player
	 */
	public abstract EntityPlayer getHandle();

	/**
	 * 現在いる座標を返します
	 * @return Location loc
	 */
	public abstract Location getLocation();

	/**
	 * プレイヤーのインベントリーを取得します
	 * @return Inventory inventory
	 */
	public abstract Inventory getInventory();

	/**
	 * 指定した座標に移動します
	 * @param loc 移動先の座標
	 */
	public abstract void teleport(Location loc);

	/**
	 * 指定したエンティティーに移動します
	 * @param entity エンティティー
	 */
	public abstract void teleport(Entity entity);

	/**
	 * サーバーから削除します
	 * @deprecated
	 */
	public abstract void remove();

	/**
	 * 初期化します
	 * @return 成功したか
	 */
	public abstract boolean clear();

}
