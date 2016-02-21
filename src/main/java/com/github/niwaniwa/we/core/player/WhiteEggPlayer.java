package com.github.niwaniwa.we.core.player;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.api.callback.Callback;
import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.database.DataBase;
import com.github.niwaniwa.we.core.database.mongodb.MongoDataBaseCollection;
import com.github.niwaniwa.we.core.database.mongodb.MongoDataBaseManager;
import com.github.niwaniwa.we.core.json.JsonManager;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.twitter.PlayerTwitterManager;
import com.github.niwaniwa.we.core.twitter.TwitterManager;
import com.github.niwaniwa.we.core.util.Util;
import com.github.niwaniwa.we.core.util.lib.Vanish;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

/**
 * WhitePlayerの実装クラス
 * @author niwaniwa
 *
 */
public class WhiteEggPlayer extends EggPlayer {

	private Player player;
	private final List<ToggleSettings> toggle = new ArrayList<>();
	private final List<Rank> ranks = new ArrayList<>();
	private AltAccount accounts;
	private boolean isVanish;
	private TwitterManager twitter;

	public static File path = new File(WhiteEggCore.getConf().getConfig().getString("setting.player.path", WhiteEggCore.getInstance().getDataFolder() + File.separator + "players" + File.separator));

	public WhiteEggPlayer(Player player){
		super(player);
		this.player = player;
		this.isVanish = false;
		this.twitter = new PlayerTwitterManager(this);
		ToggleSettings.getList().forEach(list -> toggle.add(list.clone()));
		this.accounts = new AltAccount();
	}

	@Override
	public String getPrefix() {
		StringBuilder sb = new StringBuilder();
		this.getRanks().forEach(rank -> sb.append(rank.getPrefix()));
		return sb.toString();
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
		if(jsonString.isEmpty() || jsonString == null){ throw new IllegalArgumentException(); }
		JsonObject source = jm.createJsonObject(jsonString);
		if(!source.has("WhiteEggPlayer")){ return false; }
		JsonObject json = source.getAsJsonObject("WhiteEggPlayer");
		JsonObject player = json.getAsJsonObject("player");
		if(json.get("twitter").isJsonObject()){
			JsonObject tw = json.getAsJsonObject("twitter");
			AccessToken token = new AccessToken(tw.get("accesstoken").getAsString(), tw.get("accesstokensecret").getAsString());
			this.getTwitterManager().setAccessToken(token);
		}
		this.setRank(player);
		this.setToggle(player);
		AltAccount.parser(player.toString());
		return true;
	}

	private void setRank(JsonObject j){
		if(j.get("rank") != null){
			JsonArray rank = j.getAsJsonArray("rank");
			for(Rank r : Rank.getRanks()){
				for (Object rank1 : rank) {
					if(!(rank1 instanceof JsonObject)){ continue; }
					if(r.getName().equalsIgnoreCase(String.valueOf(rank1))){ this.addRank(r); }
				}
			}
			for (Object rank1 : rank) {
				if(!(rank1 instanceof JsonObject)){ continue; }
				Rank r = Rank.parserRank(Util.toMap(((JsonObject) rank1).toString()));
				if(!Rank.check(r)){ continue; }
				this.addRank(r);
			}
		}
	}

	private void setToggle(JsonObject json){
		this.getToggleSettings().clear();
		JsonObject settings = json.getAsJsonObject("toggles");
		for(Entry<String, JsonElement> entry : settings.entrySet()){
			JsonObject toggleJ = entry.getValue().getAsJsonObject();
			ToggleSettings instance = ToggleSettings.deserializeJ(toggleJ);
			if(instance == null && !ToggleSettings.containsInstance(instance)){ continue; }
			this.getToggleSettings().add(instance);
		}
	}

	public void setName(String name){
		player.setCustomName(name);
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
		if(WhiteEggAPI.useDataBase()){
			try{
			DataBase database = WhiteEggCore.getDataBase();
			if(database.getName().equalsIgnoreCase("mongodb")){ // MongoDB
				MongoDataBaseManager mongo = (MongoDataBaseManager) database;
				MongoDataBaseCollection collection = new MongoDataBaseCollection(mongo, mongo.getDatabase("WhiteEgg"), "player");
				Document doc = collection.findOne(new Document("uuid", this.getUniqueId().toString()));
				if(doc == null){
					return false;
				}
				this.saveVariable(doc.toJson());
				return true;
			}
			// mysql
			} catch(Exception ex){
				WhiteEggCore.getConf().set("setting.database.enable", false);
				if(!WhiteEggCore.getConf().getConfig().getBoolean("warn", true)){ return false; }
				WhiteEggCore.logger.warning("Error establishing a database connection");
				return false;
			}
			return true;
		}
		// local
		JsonObject json = jm.getJson(new File(path + File.separator + this.getUniqueId().toString() + ".json"));
		if(json == null){ return false; }
		this.saveVariable(json.toString());
		return true;
	}

	@Override
	public boolean save() {
		if(!WhiteEggCore.getConf().savePlayerData()){ return false; }
		if(WhiteEggAPI.useDataBase()){
			try{
			DataBase database = WhiteEggCore.getDataBase();
			if(database.getName().equalsIgnoreCase("mongodb")){ // MongoDB
				MongoDataBaseManager mongo = (MongoDataBaseManager) database;
				MongoDataBaseCollection collection = new MongoDataBaseCollection(mongo, mongo.getDatabase("WhiteEgg"), "player");
				Object obj = collection.get(new Document("uuid", this.getUniqueId().toString()), this.getClass().getSimpleName());
				if(obj != null){
					collection.update(new Document("uuid", this.getUniqueId().toString()), new Document("$set", this.serialize()));
					return true;
				}
				collection.insert(new Document(this.serialize()));
				return true;
			}
			if(database.getName().equalsIgnoreCase("mysql")){ /* MySql */ }
			} catch(Exception ex){
				WhiteEggCore.logger.warning("Error establishing a database connection");
				WhiteEggCore.getConf().set("setting.database.enable", false);
			}
		}
		JsonObject json = unionJson();
		return jm.writeJson(path, getUniqueId().toString() + ".json", json.toString());
	}

	public void saveTask() {
		new BukkitRunnable() {
			@Override
			public void run() {
				save();
			}
		}.runTaskLaterAsynchronously(WhiteEggCore.getInstance(), 1);
	}

	public void loadTask() {
		new BukkitRunnable() {
			@Override
			public void run() {
				load();
			}
		}.runTaskLaterAsynchronously(WhiteEggCore.getInstance(), 1);
	}

	private JsonObject unionJson(){
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
		String jsonString = gson.toJson(this.serialize());
		JsonObject serialize = jm.createJsonObject(jsonString);
		JsonObject fileJson = jm.getJson(new File(path, this.getUniqueId().toString() + ".json"));
		if(fileJson == null){ return serialize; }
		fileJson.entrySet().stream().filter(entry -> !entry.getKey().equalsIgnoreCase(this.getClass().getSimpleName()))
										.forEach(entry -> serialize.add(entry.getKey(), entry.getValue()));
		return serialize;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> player = new HashMap<>();
		Map<String, Object> white = new HashMap<>();
		Map<String, Object> toggle = new HashMap<>();
		Map<String, Object> lastOnline = new HashMap<>();
		this.getToggleSettings().forEach(list -> toggle.put(list.getPlugin().getName(), list.serialize()));
		player.put("name", this.getName());
		player.put("rank", this.serializeRankData());
		player.put("toggles", toggle);
		lastOnline.put("server", Bukkit.getServerName());
		lastOnline.put("sec", new Date().getTime());
		player.put("lastonline", lastOnline);
		player.put("address", this.serializeAddress());
		player.put("account", this.getAccounts().get());
		result.put("player", player);
		result.put("twitter", this.getTwitterManager().getAccessToken() == null ? "null" : this.twitter.serialize());
		white.put(this.getClass().getSimpleName(), result);
		white.put("uuid", this.getUniqueId().toString());
		return white;
	}

	private List<String> serializeRankData(){
		List<String> ranks = new ArrayList<>(this.getRanks().size());
		this.getRanks().forEach(rank -> ranks.add(rank.getName()));
		return ranks;
	}

	private Map<String, Object> serializeAddress(){
		Map<String, Object> address = new HashMap<>();
		address.put("host", getAddress().getHostString());
		address.put("port", getAddress().getPort());
		address.put("hostName", getAddress().getHostName());
		return address;
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

	@Override
	public void updateStatus(StatusUpdate update) {
		try {
			twitter.getTwitter().updateStatus(update);
		} catch (TwitterException e) { e.printStackTrace(); }
	}

	@Override
	public void updateStatus(StatusUpdate update, Callback callback) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					callback.onTwitter((twitter.getTwitter().updateStatus(update) == null ? false : true));
				} catch (TwitterException e) { e.printStackTrace(); }
			}
		}.runTaskAsynchronously(WhiteEggCore.getInstance());
	}

	@Override
	public void updateStatus(String tweet) {
		twitter.tweet(tweet);
	}

	@Override
	public List<Status> getTimeLine() {
		try {
			return twitter.getTwitter().getHomeTimeline();
		} catch (TwitterException e) { e.printStackTrace(); }
		return new ArrayList<>(0);
	}

}
