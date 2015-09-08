package com.github.niwaniwa.we.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.api.WhiteEggAPIImpl;
import com.github.niwaniwa.we.core.bridge.LunaChatBridge;
import com.github.niwaniwa.we.core.command.WhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.WhiteEggHeadCommand;
import com.github.niwaniwa.we.core.command.WhiteEggReloadCommand;
import com.github.niwaniwa.we.core.command.toggle.WhiteEggToggleCommand;
import com.github.niwaniwa.we.core.command.twitter.WhiteEggTwitterCommand;
import com.github.niwaniwa.we.core.command.twitter.WhiteEggTwitterRegisterCommand;
import com.github.niwaniwa.we.core.listener.Debug;
import com.github.niwaniwa.we.core.listener.PlayerListener;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.player.rank.RankProperty;
import com.github.niwaniwa.we.core.util.Util;
import com.github.niwaniwa.we.core.util.message.LanguageType;
import com.github.niwaniwa.we.core.util.message.MessageManager;

public class WhiteEggCore extends JavaPlugin {

	private static WhiteEggCore instance;
	private static WhiteEggAPI api;
	private static MessageManager msg;
	private static LanguageType type;
	private PluginManager pm;
	private LunaChatBridge lunaChat;

	@Override
	public void onEnable(){
		instance = this;
		api = new WhiteEggAPIImpl();
		pm = Bukkit.getPluginManager();
		msg = new MessageManager(this.getDataFolder() + "/lang/");
		this.set();
		WhitePlayerFactory.reload();
		versionCheck();
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

	public static LanguageType getType() {
		return type;
	}

	private void set(){
		this.settingLanguage();
		this.registerCommands();
		this.registerListener();
		this.register();
		this.settingBridge();
		type = LanguageType.en_US;
	}

	private void registerCommands(){
		this.getCommand("whiteeggcore").setExecutor(new WhiteEggCoreCommand());
		this.getCommand("toggle").setExecutor(new WhiteEggToggleCommand());
		this.getCommand("head").setExecutor(new WhiteEggHeadCommand());
		this.getCommand("reload").setExecutor(new WhiteEggReloadCommand());
		this.getCommand("tweet").setExecutor(new WhiteEggTwitterCommand());
		this.getCommand("register").setExecutor(new WhiteEggTwitterRegisterCommand());
	}

	private void registerListener(){
		pm.registerEvents(new Debug(), this);
		pm.registerEvents(new PlayerListener(), this);
	}

	private void settingLanguage(){
		copyLangFiles(false);
		try {
			msg.loadLangFile();
		} catch (IOException | InvalidConfigurationException e1) {
			e1.printStackTrace();
		}
		if(!msg.getLangs().isEmpty()){ return; }
		JarFile jar = null;
		BufferedReader buffer = null;
		try {
			jar = new JarFile(getInstance().getFile());
			JarEntry entry = jar.getJarEntry("lang/ja_JP.yml");
			buffer = new BufferedReader(new InputStreamReader(jar.getInputStream(entry), "UTF-8"));
			msg.loadLangFile(LanguageType.ja_JP, buffer);
		} catch (IOException e) {
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} finally {
			try{
				if(jar != null){ jar.close(); }
				if(buffer != null){ buffer.close(); }
			} catch (IOException e){}
		}
	}

	private void register(){
		Rank r = new Rank("*", ChatColor.WHITE, "Owner", RankProperty.HIGHEST, "whiteegg.owner");
		r.add();
	}

	private void versionCheck(){
		String packageName = getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);
		if(!version.equalsIgnoreCase("v1_8_R3")){
			getLogger().warning("Unsupported CraftBukkit Version >_< : " + version);
			getLogger().warning("Please use 1.8.X");
			pm.disablePlugin(instance);
			return;
		}
	}

	private void copyLangFiles(boolean send){
		for(LanguageType type : LanguageType.values()){
			if(new File(WhiteEggCore.getInstance().getDataFolder() + "/lang/" + type.getString() + ".yml").exists()){
				continue;
			}
			Util.copyFileFromJar(
					new File(WhiteEggCore.getInstance().getDataFolder() + "/lang/"),
					WhiteEggCore.getInstance().getFile(), "lang/"+type.getString()+".yml");
		}
	}

	private void settingBridge(){
		if(Bukkit.getPluginManager().getPlugin("LunaChat") == null){ return; }
		LunaChatBridge luna = LunaChatBridge.load(Bukkit.getPluginManager().getPlugin("LunaChat"));
		this.lunaChat = luna;
	}

	@Override
	public File getFile() {
		return super.getFile();
	}

	public LunaChatBridge getLunaChat() {
		return lunaChat;
	}

}