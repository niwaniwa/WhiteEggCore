package com.github.niwaniwa.we.core.player;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.permissions.Permission;

import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.twitter.TwitterManager;

import net.sf.json.JSONObject;

/**
 * Playerクラス
 * @author KokekoKko_
 *
 */
public interface WhitePlayer extends OfflineWhitePlayer, ConfigurationSerializable {

	public abstract void sendMessage(String message);

	public abstract List<Rank> getRanks();

	public abstract boolean addRank(Rank rank);

	public abstract boolean removeRank(Rank rank);

	public abstract boolean isVanish();

	public abstract boolean isOp();

	public abstract boolean hasPermission(String permission);

	public abstract boolean hasPermission(Permission permission);

	public abstract boolean vanish();

	public abstract boolean show();

	public abstract void setVanish(boolean b);

	public abstract boolean saveVariable(JSONObject json);

	public abstract boolean reload();

	public abstract boolean load();

	public abstract boolean save();

	public abstract Map<String, Object> serialize();

	public abstract TwitterManager getTwitterManager();

	public abstract Map<String, Object> getToggleSettings();

}
