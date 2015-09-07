package com.github.niwaniwa.we.core.player;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.json.JsonManager;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.twitter.TwitterManager;
import com.github.niwaniwa.we.core.util.Util;
import com.github.niwaniwa.we.core.util.Vanish;

import net.md_5.bungee.api.ChatColor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import twitter4j.auth.AccessToken;

public class WhiteEggPlayer implements WhitePlayer {

	private static WhiteEggAPI api = WhiteEggCore.getAPI();

	private Player player;
	private List<ToggleSettings> toggle = new ArrayList<>();
	private List<Rank> ranks = new ArrayList<>();
	private SubAccount accounts;
	private boolean isVanish;
	private TwitterManager twitter;
	// not use database
	private final File path = new File(WhiteEggCore.getInstance().getDataFolder() + "/players/");

	protected WhiteEggPlayer(Player player){
		this.player = player;
		this.isVanish = false;
		this.twitter = new TwitterManager();
		this.setting();
		this.accounts = new SubAccount();
	}

	private void setting(){
		for(ToggleSettings t : ToggleSettings.getList()){
			toggle.add(t.clone());
		}
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
		this.sendMessage(message, true);
	}

	@Override
	public void sendMessage(String message, boolean replaceColorCode) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	@Override
	public List<Rank> getRanks() {
		return ranks;
	}

	@Override
	public boolean addRank(Rank rank) {
		if(ranks.contains(rank)){ return false; }
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

	protected void addAccount(WhitePlayer player){
		if(player.equals(this)){ return; }
		if(accounts.contains(player)){ return; }
		accounts.add(player);
	}

	protected void removeAccount(WhitePlayer player){
		if(player.equals(this)){ return; }
		if(!accounts.contains(player)){ return; }
		accounts.remove(player);
	}

	@Override
	public boolean saveVariable(JSONObject j) {
		JSONObject json = j.getJSONObject("WhitePlayer");
		JSONObject player = json.getJSONObject("player");
		this.isVanish = player.getBoolean("isvanish");
		if(!String.valueOf(json.get("twitter")).equalsIgnoreCase("null")){
			JSONObject tw = json.getJSONObject("twitter");
			this.getTwitterManager().setAccessToken(
					new AccessToken(tw.getString("accesstoken"), tw.getString("accesstokensecret")));
		}
		if(player.get("rank") != null){
			JSONArray rank = (JSONArray) player.get("rank");
			for(int i = 0; i < rank.size(); i++){
				if(!(rank.get(i) instanceof JSONObject)){ continue; }
				Rank r = Rank.parserRank(Util.toMap((JSONObject) rank.get(i)));
				if(r == null){ continue; }
				this.addRank(r);
			}
		}
		this.setToggle(player);
		return true;
	}

	private void setToggle(JSONObject json){
		this.getToggleSettings().clear();
		JSONObject t = json.getJSONObject("toggles");
		for(Object key : t.keySet()){
			JSONObject toggleJ = t.getJSONObject(String.valueOf(key));
			ToggleSettings instance = ToggleSettings.deserializeJ(toggleJ);
			if(instance == null){ continue; }
			if(!ToggleSettings.a(instance)){
				continue;
			}
			this.getToggleSettings().add(instance);
		}
	}

	private JsonManager jm = new JsonManager();

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
			json = jm.getJSON(new File(path + "/" + this.getUniqueId().toString() + ".json"));
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
		Map<String, Object> player = new HashMap<>();
		Map<String, Object> w = new HashMap<>();
		Map<String, Object> t = new HashMap<>();
		for(ToggleSettings to : getToggleSettings()){
			t.put(to.getPlugin().getName(), to.serialize());
		}
		player.put("name", this.getName());
		player.put("uuid", this.getUniqueId().toString());
		player.put("rank", this.getRanks());
		player.put("isvanish", this.isVanish);
		player.put("toggles", t);
		player.put("lastonline", new Date()+":"+Bukkit.getServerName());
		player.put("address", this.getAddress());
		player.put("account", this.getAccounts().get());
		result.put("player", player);
		result.put("twitter", this.getTwitterManager().getAccessToken() == null ? "null" : this.serializeTwitter());
		w.put("WhitePlayer", result);
		return w;
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
	public List<ToggleSettings> getToggleSettings() {
		return toggle;
	}

	@Override
	public String toString() {
		return this.serialize().toString();
	}

	@Override
	public InetSocketAddress getAddress() {
		return player.getAddress();
	}

	public SubAccount getAccounts(){
		return accounts;
	}

}
