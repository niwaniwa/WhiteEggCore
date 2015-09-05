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

import net.sf.json.JSONObject;

public class ToggleSettings implements Cloneable, ConfigurationSerializable {

	private static List<ToggleSettings> list = new ArrayList<>();

	private Plugin p;
	private ToggleType type;
	private String permission;
	private String title;
	private Map<String, Object> toggles = new HashMap<>();
	private boolean isHide;

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
		}
		this.isHide = isHide;
	}

	public void add(){
		list.add(this);
	}

	public Plugin getPlugin(){
		return p;
	}

	public ToggleType getType(){
		return type;
	}

	public String getTitle(){
		return title;
	}

	public Map<String, Object> getToggles(){
		return toggles;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public boolean isHide(){
		return isHide;
	}

	protected void setPlugin(Plugin p){
		this.p = p;
	}

	public void setTitle(String name){
		this.title = name;
	}

	protected void setType(ToggleType type){
		this.type = type;
	}

	public void setHide(boolean b){
		this.isHide = b;
	}

	public ToggleSettings clone() {
		ToggleSettings t = new ToggleSettings(this.getPlugin(),
				this.getType(), this.getPermission(), this.getTitle(), this.getToggles(), this.isHide());
		return t;
	}

	public static List<ToggleSettings> getList() {
		return list;
	}

	public static List<ToggleSettings> getPluginSetting(List<ToggleSettings> l){
		List<ToggleSettings> result = new ArrayList<>();
		for(ToggleSettings toggle : l){
			if(toggle.getType().equals(ToggleType.PLUGIN)){
				result.add(toggle);
			}
			continue;
		}
		return result;
	}

	public static List<ToggleSettings> getDefaltSetting(List<ToggleSettings> l){
		List<ToggleSettings> result = new ArrayList<>();
		for(ToggleSettings toggle : l){
			if(toggle.getType().equals(ToggleType.DEFAULT)){
				result.add(toggle);
			}
			continue;
		}
		return result;
	}

	public static List<ToggleSettings> getModeratorSetting(List<ToggleSettings> l){
		List<ToggleSettings> result = new ArrayList<>();
		for(ToggleSettings toggle : l){
			if(toggle.getType().equals(ToggleType.MODERATOR)){
				result.add(toggle);
			}
			continue;
		}
		return result;
	}

	public static List<ToggleSettings> getServerSetting(List<ToggleSettings> l){
		List<ToggleSettings> result = new ArrayList<>();
		for(ToggleSettings toggle : l){
			if(toggle.getType().equals(ToggleType.SERVER)){
				result.add(toggle);
			}
			continue;
		}
		return result;
	}

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

	public static ToggleSettings deserializeJ(JSONObject json){
		JSONObject j = json.getJSONObject("togglesettings");
		Plugin p = Util.getPlugin(j.getString("plugin"));
		if(p == null){ return null; }
		JSONObject t = j.getJSONObject("toggles");
		JSONObject s = t.getJSONObject("settings");
		ToggleSettings toggle = new ToggleSettings(
				p, ToggleType.get(t.getString("toggletype")),
				String.valueOf(t.get("permission")),
				String.valueOf(t.get("name")), Util.toMap(s),
				Boolean.parseBoolean(String.valueOf(t.get("ishide"))));
		return toggle;
	}

	public static boolean a(ToggleSettings t){

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
