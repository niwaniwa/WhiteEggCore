package com.github.niwaniwa.we.core;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.api.WhiteEggAPIImpl;
import com.github.niwaniwa.we.core.command.WhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.WhiteEggHeadCommand;
import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.command.toggle.WhiteEggToggleCommand;
import com.github.niwaniwa.we.core.command.toggle.type.ToggleType;
import com.github.niwaniwa.we.core.listener.Debug;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.util.message.MessageManager;

public class WhiteEggCore extends JavaPlugin {

	private static WhiteEggCore instance;
	private static WhiteEggAPI api;
	private static MessageManager msg;

	public static final String commandPermission = "whiteegg.core.command";

	@Override
	public void onEnable(){
		instance = this;
		api = new WhiteEggAPIImpl();
		msg = new MessageManager(this.getDataFolder() + "/langs/");
		this.set();
	}

	@Override
	public void onDisable(){
//		WhitePlayerFactory.saveAll();
	}

	public static WhiteEggCore getInstance(){
		return instance;
	}

	public static WhiteEggAPI getAPI(){
		return api;
	}

	public static MessageManager getMessageManager(){
		return msg;
	}

	private void set(){
		this.registerCommands();
		this.registerListener();
		WhitePlayerFactory.reload();
		this.registerToggle();
	}

	private void registerCommands(){
		this.getCommand("whiteeggcore").setExecutor(new WhiteEggCoreCommand());
		this.getCommand("toggle").setExecutor(new WhiteEggToggleCommand());
		this.getCommand("head").setExecutor(new WhiteEggHeadCommand());
	}

	private void registerListener(){
		Bukkit.getPluginManager().registerEvents(new Debug(), this);
	}

	private void registerToggle(){
		Map<String, Object> result = new HashMap<>();
		result.put("test", "okok");
		ToggleSettings toggle = new ToggleSettings(instance, ToggleType.DEFAULT, "whiteegg", "toggle", result, false);
		toggle.add();
	}

}