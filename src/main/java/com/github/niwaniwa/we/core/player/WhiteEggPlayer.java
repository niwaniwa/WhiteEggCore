package com.github.niwaniwa.we.core.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.json.JSONManager;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.twitter.TwitterManager;
import com.github.niwaniwa.we.core.util.Vanish;

import net.sf.json.JSONObject;

public class WhiteEggPlayer implements WhitePlayer {

	private static WhiteEggAPI api = WhiteEggCore.getAPI();

	private Player player;
	private Map<String, Object> toggle = new HashMap<>();
	private List<Rank> ranks = new ArrayList<>();
	private boolean isVanish;
	private TwitterManager twitter;
	// not use database
	private final File path = new File(WhiteEggCore.getInstance().getDataFolder() + "/players/");

	protected WhiteEggPlayer(Player player){
		this.player = player;
		this.isVanish = false;
		this.toggle.putAll(ToggleSettings.getToggleSettings());
		this.twitter = new TwitterManager();
	}

	@Override
	public String getName() {
		return player.getName();
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public UUID getUniqueId() {
		return player.getUniqueId();
	}

	@Override
	public boolean isOnline() {
		return player.isOnline();
	}

	@Override
	public void sendMessage(String message) {
		player.sendMessage(message);
	}

	@Override
	public List<Rank> getRanks() {
		return ranks;
	}

	@Override
	public boolean addRank(Rank rank) {
		return ranks.add(rank);
	}

	@Override
	public boolean removeRank(Rank rank) {
		return ranks.remove(rank);
	}

	@Override
	public boolean isVanish() {
		return isVanish;
	}

	@Override
	public boolean isOp(){
		return player.isOp();
	}

	@Override
	public boolean hasPermission(String permission) {
		return player.hasPermission(permission);
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return player.hasPermission(permission);
	}

	@Override
	public boolean vanish() {
		return Vanish.hide(this);
	}

	@Override
	public boolean show() {
		return Vanish.show(this);
	}

	@Override
	public void setVanish(boolean b) {
		this.isVanish = b;
	}

	@Override
	public boolean isBanned() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setBanned(boolean paramBoolean) {
		player.setBanned(paramBoolean);
	}

	@Override
	public boolean isWhitelisted() {
		return player.isWhitelisted();
	}

	@Override
	public void setWhitelisted(boolean paramBoolean) {
		player.setWhitelisted(paramBoolean);
	}

	@Override
	public long getFirstPlayed() {
		return player.getFirstPlayed();
	}

	@Override
	public long getLastPlayed() {
		return player.getLastPlayed();
	}

	@Override
	public boolean hasPlayedBefore() {
		return player.hasPlayedBefore();
	}

	@Override
	public Location getBedSpawnLocation() {
		return player.getBedSpawnLocation();
	}

	@Override
	public void setOp(boolean paramBoolean) {
		player.setOp(paramBoolean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean saveVariable(JSONObject json) {
		this.isVanish = json.getBoolean("isvanish");
		if (json instanceof Map) {
			Map<String, Object> twitter;
			try{
			twitter = (Map<String, Object>) json;
			} catch (ClassCastException cast){
				cast.printStackTrace();
				return false;
			}
			this.twitter.setAccessToken(TwitterManager.toAccesToken(twitter));
		}
		return false;
	}

	private JSONManager jm = new JSONManager();

	@Override
	public boolean reload() {
		return this.load();
	}

	@Override
	public boolean load() {
		if(api.useDataBase()){
			// sql
			return true;
		}
		// local
		JSONObject json = null;
		try {
			json = jm.getJSON(new File(path + this.getUniqueId().toString() + ".json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(json == null){ return false; }
		this.saveVariable(json);
		return false;
	}

	@Override
	public boolean save() {
		if(api.useDataBase()){
			// sql
			return true;
		}
		JSONObject json = JSONObject.fromObject(this.serialize());
		try {
			jm.writeJSON(path, this.getUniqueId().toString() + ".json", json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<>();
		result.put("name", this.getName());
		result.put("uuid", this.getUniqueId().toString());
		result.put("rank", this.getRanks());
		result.put("isvanish", this.isVanish);
		result.put("twitter", this.getTwitterManager().getAccessToken() == null ? "null" : this.serializeTwitter());
		return result;
	}

	private Map<String, Object> serializeTwitter() {
		Map<String, Object> result = new HashMap<>();
		result.put("accesstoken", getTwitterManager().getAccessToken().getToken());
		result.put("accesstokensecret", getTwitterManager().getAccessToken().getTokenSecret());
		return result;
	}

	@Override
	public TwitterManager getTwitterManager() {
		return twitter;
	}

	@Override
	public Map<String, Object> getToggleSettings() {
		return toggle;
	}

}
