package com.github.niwaniwa.we.core.player;

import java.net.InetSocketAddress;
import java.util.List;

import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.twitter.TwitterManager;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.sf.json.JSONObject;

/**
 * Playerクラス
 * @author KokekoKko_
 *
 */
public interface WhitePlayer extends OfflineWhitePlayer, WhiteCommandSender {

	public abstract List<Rank> getRanks();

	public abstract boolean addRank(Rank rank);

	public abstract boolean removeRank(Rank rank);

	public abstract boolean isVanish();

	public abstract boolean isOp();

	public abstract boolean vanish();

	public abstract boolean show();

	public abstract void setVanish(boolean b);

	public abstract boolean saveVariable(JSONObject json);

	public abstract boolean reload();

	public abstract boolean load();

	public abstract boolean save();

	public abstract TwitterManager getTwitterManager();

	public abstract List<ToggleSettings> getToggleSettings();

	public abstract InetSocketAddress getAddress();

	public abstract EntityPlayer getHandle();

	public abstract boolean clear();

}
