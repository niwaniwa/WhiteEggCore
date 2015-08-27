package com.github.niwaniwa.we.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.api.WhiteEggAPIImpl;
import com.github.niwaniwa.we.core.command.WhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.command.toggle.WhiteEggToggleCommand;
import com.github.niwaniwa.we.core.listener.Debug;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;

public class WhiteEggCore extends JavaPlugin {

	private static WhiteEggCore instance;
	private static WhiteEggAPI api;

	public static final String commandPermission = "whiteegg.core.command";

	@Override
	public void onEnable(){
		instance = this;
		api = new WhiteEggAPIImpl();
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

	private void set(){
		this.registerCommands();
		this.registerListener();
		new ToggleSettings();
		WhitePlayerFactory.reload();
	}

	private void registerCommands(){
		this.getCommand("whiteeggcore").setExecutor(new WhiteEggCoreCommand());
		this.getCommand("toggle").setExecutor(new WhiteEggToggleCommand());
	}

	private void registerListener(){
		Bukkit.getPluginManager().registerEvents(new Debug(), this);
	}

}