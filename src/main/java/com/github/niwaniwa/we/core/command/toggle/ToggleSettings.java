package com.github.niwaniwa.we.core.command.toggle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;

import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.util.Util;
import com.google.gson.JsonObject;

/**
 * 各種設定クラス
 * @author niwaniwa
 *
 */
public class ToggleSettings implements Cloneable, ConfigurationSerializable {

	private static final List<ToggleSettings> list = new ArrayList<>();
	private static final Map<String, Object> server = new HashMap<>();

	private Plugin p;
	private String tag;
	private String permission;
	private String title;
	private final Map<String, Object> toggles = new HashMap<>();
	private boolean isHide;

	/**
	 * コンストラクター
	 * @param plugin プラグイン
	 * @param type タイプ
	 * @param permission 権限
	 * @param custam 任意の名前
	 * @param toggles 設定
	 * @param isHide デフォルトでの表示
	 */
	public ToggleSettings(Plugin plugin, String tag, String permission,
			String custam, Map<String, Object> toggles, boolean isHide) {
		this.p = plugin;
		this.tag = tag;
		this.permission = permission;
		this.title = custam.isEmpty() ? p.getName() : custam;
		if(toggles != null){
			for(String key : toggles.keySet()){
				this.toggles.put(key, toggles.get(key));
			}
			if(tag.equalsIgnoreCase("SERVER")){
				server.putAll(toggles);
			}
		}
		this.isHide = isHide;
	}

	/**
	 * 設定を追加
	 */
	public void add(){
		list.add(this);
	}

	/**
	 * 設定を設定したプラグイン
	 * @return プラグイン
	 */
	public Plugin getPlugin(){
		return p;
	}

	/**
	 * タグの取得
	 * @return String タグ
	 */
	public String getTag(){
		return tag;
	}

	/**
	 * 名前
	 * @return 名前
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * 設定
	 * @return Map
	 */
	public Map<String, Object> getToggles(){
		return toggles;
	}

	/**
	 * 権限
	 * @return 権限
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * 権限の設定
	 * @param permission 設定する権限
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * デフォルトで表示するか
	 * @return デフォルトでの表示
	 */
	public boolean isHide(){
		return isHide;
	}

	/**
	 * プラグインのセット
	 * @param p プラグイン
	 */
	protected void setPlugin(Plugin p){
		this.p = p;
	}

	/**
	 * 名前の設定
	 * @param name 文字列
	 */
	public void setTitle(String name){
		this.title = name;
	}

	/**
	 * タグの設定
	 * @param tag タグ
	 */
	protected void setTag(String tag){
		this.tag = tag;
	}

	/**
	 * デフォルトで表示するか
	 * @param b boolean
	 */
	public void setHide(boolean b){
		this.isHide = b;
	}

	public ToggleSettings clone() {
		ToggleSettings t = new ToggleSettings(this.getPlugin(),
				this.getTag(), this.getPermission(), this.getTitle(), this.getToggles(), this.isHide());
		return t;
	}

	/**
	 * 現在登録されている設定の取得
	 * @return List
	 */
	public static List<ToggleSettings> getList() {
		return list;
	}

	/**
	 * 取得したい種別の設定を返す
	 * @param type 取得したい種別
	 * @param toggle 検索する設定
	 * @return List 設定
	 */
	public static List<ToggleSettings> getList(String tag, List<ToggleSettings> toggle){
		List<ToggleSettings> result = new ArrayList<>();
		toggle.stream().filter(t -> t.getTag().equalsIgnoreCase(tag)).forEach(t -> result.add(t));
		return result;
	}

	/**
	 * サーバーの設定
	 * @return List リスト
	 */
	public static Map<String, Object> getServerSetting(){
		return server;
	}

	/**
	 * 指定した設定の中から設定を探して返す
	 * @param ts 対象設定List
	 * @param t 設定
	 * @return 設定
	 */
	public static ToggleSettings getSetting(List<ToggleSettings> toggles, ToggleSettings t){
		if(toggles.isEmpty()){ throw new IllegalArgumentException("list is empty"); }
		List<ToggleSettings> result = toggles.stream().filter(toggle -> toggle.getPlugin().equals(t.getPlugin()))
						.filter(toggle -> toggle.getTitle().equals(t.getTitle()))
						.collect(Collectors.toList());
		return result.isEmpty() ? null : result.get(0);
	}

	public static boolean contains(String key){
		return list.stream()
				.anyMatch(toggles ->  toggles.getToggles()
						.entrySet().stream().anyMatch(entry -> entry.getKey().equalsIgnoreCase(key)));
	}

	public static Object getValue(WhitePlayer player, String key){
		if(!contains(key)){ return null; }
		List<ToggleSettings> result = player.getToggleSettings().stream()
				.filter(toggle -> toggle.getToggles().keySet().stream()
					.anyMatch(k -> k.equalsIgnoreCase(key)))
				.collect(Collectors.toList());
		return result.isEmpty() ? null : result.get(0);
	}

	public static boolean set(WhitePlayer player, String key, Object value){
		player.getToggleSettings().stream()
			.filter(toggle -> toggle.getToggles().keySet().stream()
				.anyMatch(string -> string.equalsIgnoreCase(key)))
			.filter(toggle -> toggle.getTag().equalsIgnoreCase("SERVER")).limit(1)
			.forEach(toggle -> toggle.getToggles().put(key, value));
		return false;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> plugin = new HashMap<>();
		Map<String, Object> toggles = new HashMap<>();
		toggles.put("name", this.getTitle());
		toggles.put("toggletag", this.getTag());
		toggles.put("permission", this.getPermission());
		toggles.put("ishide", this.isHide());
		toggles.put("settings", this.getToggles());
		plugin.put("plugin", this.getPlugin().getName());
		plugin.put("toggles", toggles);
		result.put("togglesettings", plugin);
		return result;
	}

	@Override
	public String toString() {
		return this.serialize().toString();
	}

	public ToggleSettings deserialize(Map<String, Object> map) throws IOException{
		Map<String, Object> map2 = Util.toMap(String.valueOf(map.get("togglesettings")));
		Plugin p = Bukkit.getPluginManager().getPlugin(String.valueOf(map2.get("plugin")));
		if(p == null){ return null; }
		Map<String, Object> t = Util.toMap(String.valueOf(map2.get("toggles")));
		Map<String, Object> toggles = Util.toMap(String.valueOf(t.get("settings")));
		ToggleSettings toggle = new ToggleSettings(
				p, String.valueOf(t.get("toggletag")),
				String.valueOf(t.get("permission")),
				String.valueOf(t.get("name")), toggles,
				Boolean.parseBoolean(String.valueOf(t.get("ishide"))));
		return toggle;
	}

	public static ToggleSettings deserializeJ(JsonObject json){
		JsonObject j = json.getAsJsonObject("togglesettings");
		Plugin p = Util.getPlugin(j.get("plugin").getAsString());
		if(p == null){ return null; }
		JsonObject t = j.getAsJsonObject("toggles");
		JsonObject s = t.getAsJsonObject("settings");
		ToggleSettings toggle = new ToggleSettings(
				p, t.get("toggletag").getAsString(),
				String.valueOf(t.get("permission")),
				String.valueOf(t.get("name")), Util.toMap(s.toString()),
				Boolean.parseBoolean(String.valueOf(t.get("ishide"))));
		return toggle;
	}

	public static boolean containsInstance(ToggleSettings t){
		for(ToggleSettings s : list){
			if(t.getPermission().equalsIgnoreCase(s.getPermission())
					&& t.getPlugin().getName().equalsIgnoreCase(s.getPlugin().getName())
					&& t.getTitle().equalsIgnoreCase(s.getTitle())
					&& t.getTag().equalsIgnoreCase(s.getTag())){
				return true;
			}
		}
		return false;
	}

}
