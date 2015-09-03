package com.github.niwaniwa.we.core;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.api.WhiteEggAPIImpl;
import com.github.niwaniwa.we.core.command.WhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.WhiteEggHeadCommand;
import com.github.niwaniwa.we.core.command.WhiteEggReloadCommand;
import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.command.toggle.WhiteEggToggleCommand;
import com.github.niwaniwa.we.core.command.toggle.type.ToggleType;
import com.github.niwaniwa.we.core.command.twitter.WhiteEggTwitterCommand;
import com.github.niwaniwa.we.core.listener.Debug;
import com.github.niwaniwa.we.core.listener.PlayerListener;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.player.rank.RankProperty;
import com.github.niwaniwa.we.core.util.message.MessageManager;

public class WhiteEggCore extends JavaPlugin {

	private static WhiteEggCore instance;
	private static WhiteEggAPI api;
	private static MessageManager msg;

	private PluginManager pm;

	@Override
	public void onEnable(){
		instance = this;
		api = new WhiteEggAPIImpl();
		msg = new MessageManager(this.getDataFolder() + "/langs/");
		msg.loadLangFile();
		pm = Bukkit.getPluginManager();
		this.set();
		WhitePlayerFactory.reload();
	}

	@Override
	public void onDisable(){
		WhitePlayerFactory.saveAll();
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
		this.register();
	}

	private void registerCommands(){
		this.getCommand("whiteeggcore").setExecutor(new WhiteEggCoreCommand());
		this.getCommand("toggle").setExecutor(new WhiteEggToggleCommand());
		this.getCommand("head").setExecutor(new WhiteEggHeadCommand());
		this.getCommand("reload").setExecutor(new WhiteEggReloadCommand());
		this.getCommand("twitter").setExecutor(new WhiteEggTwitterCommand());
	}

	private void registerListener(){
		pm.registerEvents(new Debug(), this);
		pm.registerEvents(new PlayerListener(), this);
	}

	private void register(){
		Map<String, Object> result = new HashMap<>();
		result.put("chat", "off");
		ToggleSettings toggle = new ToggleSettings(instance, ToggleType.DEFAULT, "whiteegg", "toggle", result, false);
		toggle.add();
		Rank r = new Rank("*", ChatColor.WHITE, "Owner", RankProperty.HIGHEST, "whiteegg.owner");
		r.add();
	}

}