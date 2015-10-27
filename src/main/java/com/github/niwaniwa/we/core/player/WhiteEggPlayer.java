package com.github.niwaniwa.we.core.player;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.json.JsonManager;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.twitter.PlayerTwitterManager;
import com.github.niwaniwa.we.core.twitter.TwitterManager;
import com.github.niwaniwa.we.core.util.Util;
import com.github.niwaniwa.we.core.util.Vanish;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import twitter4j.auth.AccessToken;

/**
 * WhitePlayerの実装クラス
 * @author niwaniwa
 *
 */
public class WhiteEggPlayer implements WhitePlayer {

	private static WhiteEggAPI api = WhiteEggCore.getAPI();

	private final Player player;
	private final List<ToggleSettings> toggle = new ArrayList<>();
	private final List<Rank> ranks = new ArrayList<>();
	private AltAccount accounts;
	private boolean isVanish;
	private TwitterManager twitter;
	// not use database
	private final File path = new File(WhiteEggCore.getInstance().getDataFolder() + File.separator + "players" + File.separator);

	protected WhiteEggPlayer(Player player){
		this.player = player;
		this.isVanish = false;
		this.twitter = new PlayerTwitterManager(this);
		this.setting();
		this.accounts = new AltAccount();
	}

	/**
	 * 初期化時の設定
	 */
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
	public String getPrefix() {
		StringBuilder sb = new StringBuilder();
		for(Rank r : getRanks()){
			sb.append(r.getPrefix());
		}
		return sb.toString();
	}

	@Override
	public String getFullName() {
		return getPrefix() + getName();
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

	/**
	 * アカウントの追加
	 * @param player
	 */
	protected void addAccount(WhitePlayer player){
		if(player.equals(this)){ return; }
		if(accounts.contains(player)){ return; }
		accounts.add(player.getUniqueId());
	}

	/**
	 * アカウントの削除
	 * @param player
	 */
	protected void removeAccount(WhitePlayer player){
		if(player.equals(this)){ return; }
		if(!accounts.contains(player)){ return; }
		accounts.remove(player);
	}

	@Override
	public boolean saveVariable(String jsonString) {
		JSONObject j = JSONObject.fromObject(jsonString);
		JSONObject json = j.getJSONObject("WhiteEggPlayer");
		JSONObject player = json.getJSONObject("player");
		this.isVanish = player.getBoolean("isvanish");
		if(!String.valueOf(json.get("twitter")).equalsIgnoreCase("null")){
			JSONObject tw = json.getJSONObject("twitter");
			this.getTwitterManager().setAccessToken(
					new AccessToken(tw.getString("accesstoken"), tw.getString("accesstokensecret")));
		}
		this.setRank(player);
		this.setToggle(player);
		AltAccount.parser(player);
		return true;
	}

	private void setRank(JSONObject j){
		if(j.get("rank") != null){
			JSONArray rank = (JSONArray) j.get("rank");
			for (Object rank1 : rank) {
				if(!(rank1 instanceof JSONObject)){ continue; }
				Rank r = Rank.parserRank(Util.toMap((JSONObject) rank1));
				if(!Rank.check(r)){ continue; }
				this.addRank(r);
			}
		}
	}

	private void setToggle(JSONObject json){
		this.getToggleSettings().clear();
		JSONObject t = json.getJSONObject("toggles");
		for(Object key : t.keySet()){
			JSONObject toggleJ = t.getJSONObject(String.valueOf(key));
			ToggleSettings instance = ToggleSettings.deserializeJ(toggleJ);
			if(instance == null){ continue; }
			if(!ToggleSettings.containsInstance(instance)){ continue; }
			this.getToggleSettings().add(instance);
		}
	}

	public void setName(String name){
		player.setCustomName("");
	}

	public void tweet(String[] tweet){
		twitter.tweet(tweet);
	}

	private JsonManager jm = new JsonManager();

	@Override
	public boolean reload() {
		this.save();
		return this.load();
	}

	@Override
	public boolean load() {
		if(api.useDataBase()){
			// sql
			return true;
		}
		// local
		JsonObject json = jm.getJson(new File(path + File.separator + this.getUniqueId().toString() + ".json"));
		if(json == null){ return false; }
		this.saveVariable(json.toString());
		return false;
	}

	@Override
	public boolean save() {
		if(api.useDataBase()){
			// sql
			return true;
		}
		JsonObject json = unionJson();
		return jm.writeJson(path, getUniqueId().toString() + ".json", json);
	}

	public void saveTask(){
		new BukkitRunnable() {
			@Override
			public void run() {
				save();
			}
		}.runTaskLaterAsynchronously(WhiteEggCore.getInstance(), 1);
	}

	private JsonObject unionJson(){
		Gson gson = new Gson();
		JsonObject serialize = gson.fromJson(this.serialize().toString(), JsonObject.class);
		JsonObject fileJson = jm.getJson(new File(path, this.getUniqueId().toString() + ".json"));
		if(fileJson == null){ return serialize; }
		for(Entry<String, JsonElement> entry : fileJson.entrySet()){
			if(!entry.getKey().equalsIgnoreCase(this.getClass().getSimpleName())){
				serialize.add(entry.getKey(), entry.getValue());
			}
		}
		return serialize;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> player = new HashMap<>();
		Map<String, Object> white = new HashMap<>();
		Map<String, Object> toggle = new HashMap<>();
		for(ToggleSettings to : getToggleSettings()){
			toggle.put(to.getPlugin().getName(), to.serialize());
		}
		player.put("name", this.getName());
		player.put("uuid", this.getUniqueId().toString());
		player.put("rank", this.getRanks());
		player.put("isvanish", this.isVanish);
		player.put("toggles", toggle);
		player.put("lastonline", new Date()+":"+Bukkit.getServerName());
		player.put("address", this.getAddress());
		player.put("account", this.getAccounts().get());
		result.put("player", player);
		result.put("twitter", this.getTwitterManager().getAccessToken() == null ? "null" : this.serializeTwitter());
		white.put(this.getClass().getSimpleName(), result);
		return white;
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

	@Override
	public EntityPlayer getHandle(){
		return ((CraftPlayer) player).getHandle();
	}

	public AltAccount getAccounts(){
		return accounts;
	}

	@Override
	public boolean clear() {
		this.toggle.clear();
		this.twitter = new PlayerTwitterManager(this);
		this.ranks.clear();
		return true;
	}
}
