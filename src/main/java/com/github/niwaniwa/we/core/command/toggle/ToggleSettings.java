package com.github.niwaniwa.we.core.command.toggle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;

import com.github.niwaniwa.we.core.command.toggle.type.ToggleType;
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
	private ToggleType type;
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
	public ToggleSettings(Plugin plugin, ToggleType type, String permission,
			String custam, Map<String, Object> toggles, boolean isHide) {
		this.p = plugin;
		this.type = type;
		this.permission = permission;
		this.title = custam.isEmpty() ? p.getName() : custam;
		if(toggles != null){
			for(String key : toggles.keySet()){
				this.toggles.put(key, toggles.get(key));
			}
			if(type == ToggleType.SERVER){
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
	 * タイプ
	 * @return タイプ
	 */
	public ToggleType getType(){
		return type;
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
	 * タイプの設定
	 * @param type タイプ
	 */
	protected void setType(ToggleType type){
		this.type = type;
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
				this.getType(), this.getPermission(), this.getTitle(), this.getToggles(), this.isHide());
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
	 * プラグインに設定されている設定
	 * @param l 設定
	 * @return List
	 */
	public static List<ToggleSettings> getPluginSetting(List<ToggleSettings> l){
		List<ToggleSettings> result = new ArrayList<>();
		for(ToggleSettings toggle : l){
			if(toggle.getType().equals(ToggleType.PLUGIN)){
				result.add(toggle);
			}
		}
		return result;
	}

	/**
	 * デフォルトで表示される設定
	 * @param l 設定
	 * @return List
	 */
	public static List<ToggleSettings> getDefaltSetting(List<ToggleSettings> l){
		List<ToggleSettings> result = new ArrayList<>();
		for(ToggleSettings toggle : l){
			if(toggle.getType().equals(ToggleType.DEFAULT)){
				result.add(toggle);
			}
		}
		return result;
	}

	/**
	 * moderator用の設定
	 * @param l List
	 * @return List リスト
	 */
	public static List<ToggleSettings> getModeratorSetting(List<ToggleSettings> l){
		List<ToggleSettings> result = new ArrayList<>();
		for(ToggleSettings toggle : l){
			if(toggle.getType().equals(ToggleType.MODERATOR)){
				result.add(toggle);
			}
		}
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
	 * サーバーの設定
	 * @param l List
	 * @return List リスト
	 */
	public static List<ToggleSettings> getServerSetting(List<ToggleSettings> l){
		List<ToggleSettings> result = new ArrayList<>();
		for(ToggleSettings toggle : l){
			if(toggle.getType().equals(ToggleType.SERVER)){
				result.add(toggle);
			}
		}
		return result;
	}

	/**
	 * 指定した設定の中から設定を探して返す
	 * @param ts 対象設定List
	 * @param t 設定
	 * @return 設定
	 */
	public static ToggleSettings getSetting(List<ToggleSettings> ts, ToggleSettings t){

		for(ToggleSettings toggle : ts){
			if(toggle.getPlugin().equals(t.getPlugin())){
				if(toggle.getTitle().equals(t.getTitle())){
					if(toggle.getType().equals(t.getType())){
						return toggle;
					}
				}
			}
		}
		return null;
	}

	public static boolean contains(String key){
		for(ToggleSettings t : list){
			for(String k : t.getToggles().keySet()){
				if(k.equalsIgnoreCase(key)){
					return true;
				}
			}
		}
		return false;
	}

	public static Object getValue(WhitePlayer player, String key){
		if(!contains(key)){ return null; }
		for(ToggleSettings t : player.getToggleSettings()){
			for(String k : t.getToggles().keySet()){
				if(k.equalsIgnoreCase(key)){
					return t.getToggles().get(key);
				}
			}
		}
		return null;
	}

	public static boolean set(WhitePlayer player, String key, Object value){
		for(ToggleSettings t : player.getToggleSettings()){
			for(String k : t.getToggles().keySet()){
				if(k.equalsIgnoreCase(key)){
					if(t.getType() == ToggleType.SERVER){
						getServerSetting().put(key, value);
						return true;
					}
					t.getToggles().put(key, value);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> plugin = new HashMap<>();
		Map<String, Object> toggles = new HashMap<>();
		toggles.put("name", this.getTitle());
		toggles.put("toggletype", this.getType().getType());
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
				p, ToggleType.valueOf(String.valueOf(t.get("toggletype"))),
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
				p, ToggleType.get(t.get("toggletype").getAsString()),
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
					&& t.getType().equals(s.getType())){
				return true;
			}
		}
		return false;
	}

}
