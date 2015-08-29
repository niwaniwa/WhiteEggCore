package com.github.niwaniwa.we.core.command.toggle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.niwaniwa.we.core.player.WhitePlayer;

public class ToggleSettings {

	private static List<ToggleSettings> list = new ArrayList<>();

	private JavaPlugin plugin;
	private String name;
	private TogglePermission type;
	private Map<String, Object> toggles;
	private boolean isHide;

	public <T extends JavaPlugin> ToggleSettings(T plugin,String customName, TogglePermission type, Map<String, Object> toggles, boolean hide) {
		this.plugin = plugin;
		this.name = customName;
		this.type = type;
		this.isHide = hide;
		this.toggles = new HashMap<>();
		if(toggles != null){
			this.toggles.putAll(toggles);
		}
	}

	public <T extends JavaPlugin> ToggleSettings(T plugin, TogglePermission type, Map<String, Object> toggles, boolean hide) {
		this(plugin, plugin.getName(), type, toggles, hide);
	}

	public <T extends JavaPlugin> ToggleSettings(T plugin, TogglePermission type) {
		this(plugin, plugin.getName(), type, null, false);
	}

	public <T extends JavaPlugin> ToggleSettings(T plugin, String customName, TogglePermission type) {
		this(plugin, customName, type, null, false);
	}

	public JavaPlugin getPlugin(){
		return plugin;
	}

	public String getName(){
		return name;
	}

	public TogglePermission getType(){
		return type;
	}

	public Map<String, Object> getToggles(){
		return toggles;
	}

	public boolean add(String key, Object value){
		if(toggles.containsKey(key)){ return false; }
		this.toggles.put(key, value);
		return true;
	}

	public boolean remove(String key){
		if(!toggles.containsKey(key)){ return false; }
		this.toggles.remove(key);
		return true;
	}

	public void add(boolean add){
		if(!add){ list.add(this); }
		for(ToggleSettings t : list){
			if(t.getPlugin().getName().equalsIgnoreCase(this.getName())){
				t.getToggles().putAll(this.getToggles());
			}
		}
	}

	public static List<ToggleSettings> getList(){
		return list;
	}

	public static ToggleSettings getDefault(List<ToggleSettings> to){
		ToggleSettings toggle = new ToggleSettings(null, "default", new TogglePermission("", true));
		for(ToggleSettings t : to){
			if(t.getType().isDefault()){
				for(String key : t.getToggles().keySet()){
					toggle.add(key, t.getToggles().get(key));
				}
			}
		}
		return toggle;
	}

	public static ToggleSettings getDefault(){
		return getDefault(list);
	}

	public static List<ToggleSettings> getPluginSettings(List<ToggleSettings> to){
		List<ToggleSettings> toggles = new ArrayList<>();
		for(ToggleSettings t : to){
			if(!t.getType().isDefault()){
				toggles.add(t);
			}
		}
		return toggles;
	}

	public boolean isHide() {
		return isHide;
	}

	public void setHide(boolean isHide) {
		this.isHide = isHide;
	}

	public Object get(WhitePlayer player, String key, JavaPlugin p){
		for(ToggleSettings t : player.getToggleSettings()){
			if(t.getPlugin().getName().equals(p)){
				if(!player.hasPermission(t.getType().getPermission())){ return null; }
				if(t.isHide()){ return null; }
				for(String k : t.getToggles().keySet()){
					if(!k.equalsIgnoreCase(key)){ continue; }
					Object value = t.getToggles().get(k);
					return value;
				}
			}
		}
		return null;
	}

}
