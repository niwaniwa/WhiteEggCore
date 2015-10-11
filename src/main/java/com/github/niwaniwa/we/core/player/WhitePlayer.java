package com.github.niwaniwa.we.core.player;

import java.net.InetSocketAddress;
import java.util.List;

import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.twitter.TwitterManager;

import net.minecraft.server.v1_8_R3.EntityPlayer;

/**
 * Playerクラス
 * @author KokekoKko_
 *
 */
public interface WhitePlayer extends OfflineWhitePlayer, WhiteCommandSender {

	/**
	 * 現在登録されているランクを返します
	 * @return List<Rank> rank
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
	 */
	public abstract void setVanish(boolean b);

	/**
	 * jsonからデータを読み込み、変数を変更します
	 * @param json json
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
	 * @return
	 */
	public abstract List<ToggleSettings> getToggleSettings();

	/**
	 * 現在接続しているプレイヤーのアドレスを返します
	 * @return
	 */
	public abstract InetSocketAddress getAddress();

	/**
	 * EntityPlayerを返します
	 * @return EntityPlayer player
	 */
	public abstract EntityPlayer getHandle();

	/**
	 * 初期化します
	 * @return
	 */
	public abstract boolean clear();

}
