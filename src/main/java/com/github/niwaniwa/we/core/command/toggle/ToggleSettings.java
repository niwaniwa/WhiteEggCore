package com.github.niwaniwa.we.core.command.toggle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.Plugin;

import com.github.niwaniwa.we.core.command.toggle.type.ToggleType;

public class ToggleSettings implements Cloneable {

	private static List<ToggleSettings> list = new ArrayList<>();

	private Plugin p;
	private ToggleType type;
	private String permission;
	private String name;
	private Map<String, Object> toggles = new HashMap<>();
	private boolean isHide;

	public ToggleSettings(Plugin plugin, ToggleType type, String permission, String custam, Map<String, Object> toggles, boolean isHide) {
		this.p = plugin;
		this.type = type;
		this.permission = permission;
		this.name = custam;
		this.toggles = toggles;
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

	public String getToggleName(){
		return name;
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

	public void setName(String name){
		this.name = name;
	}

	protected void setType(ToggleType type){
		this.type = type;
	}

	public void setHide(boolean b){
		this.isHide = b;
	}

	public ToggleSettings clone() {
		ToggleSettings t = null;
		try {
			t = (ToggleSettings) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		t.setHide(this.isHide());
		t.setType(getType());
		t.setPlugin(getPlugin());
		t.getToggles().putAll(t.getToggles());
		t.setPermission(getPermission());
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
				if(toggle.getToggleName().equals(t.getToggleName())){
					if(toggle.getType().equals(t.getType())){
						return toggle;
					}
				}
			}
		}
		return null;
	}

}
