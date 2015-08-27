package com.github.niwaniwa.we.core.command.toggle;

import java.util.HashMap;
import java.util.Map;

public class ToggleSettings {

	private static Map<String, Object> defaultToggleSettings = new HashMap<>();
	private static Map<String, Object> adminToggleSettings = new HashMap<>();

	public ToggleSettings(){
		this.registerT();
		this.registerA();
	}

	public void addToggleSetting(Map<String, Object> map){
		defaultToggleSettings.putAll(map);
	}

	public void addAdminToggleSetting(Map<String, Object> map){
		adminToggleSettings.putAll(map);
	}

	private void registerT(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("chat", true);
		result.put("joinmessage", true);
		result.put("punishmessage", true);
		defaultToggleSettings.putAll(result);
	}

	private void registerA(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("whitelist", true);
		adminToggleSettings.putAll(result);
	}

	public static Map<String, Object> getToggleSettings(){
		return defaultToggleSettings;
	}

	public static Map<String, Object> getAdminToggleSettings(){
		return adminToggleSettings;
	}

}
