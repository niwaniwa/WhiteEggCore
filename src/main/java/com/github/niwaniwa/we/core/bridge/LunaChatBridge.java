package com.github.niwaniwa.we.core.bridge;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ucchyocean.lc.LunaChat;
import com.github.ucchyocean.lc.LunaChatAPI;

public class LunaChatBridge {

	private String name = "LunaChat";
	private LunaChat plugin;

	private LunaChatBridge(LunaChat p){
		this.plugin = p;
	}

	public String getName() {
		return name;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public static LunaChatBridge load(Plugin plugin2) {
		if(!(plugin2 instanceof LunaChat)){ return null; }
		LunaChatBridge luna = new LunaChatBridge((LunaChat) plugin2);
		return luna;
	}

	public LunaChatAPI getAPI(){
		return plugin.getLunaChatAPI();
	}

}
