package com.github.niwaniwa.we.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.api.WhiteEggAPIImpl;
import com.github.niwaniwa.we.core.command.WhiteEggCoreCommandHandler;
import com.github.niwaniwa.we.core.command.WhiteEggHeadCommand;
import com.github.niwaniwa.we.core.command.WhiteEggReplayCommand;
import com.github.niwaniwa.we.core.command.WhiteEggWhisperCommand;
import com.github.niwaniwa.we.core.command.abstracts.AbstractWhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.core.WhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.toggle.WhiteEggToggleCommand;
import com.github.niwaniwa.we.core.command.twitter.WhiteEggTwitterCommand;
import com.github.niwaniwa.we.core.command.twitter.WhiteEggTwitterRegisterCommand;
import com.github.niwaniwa.we.core.config.WhiteEggCoreConfig;
import com.github.niwaniwa.we.core.listener.Debug;
import com.github.niwaniwa.we.core.listener.PlayerListener;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhiteConsoleSender;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.player.rank.RankProperty;
import com.github.niwaniwa.we.core.util.Util;
import com.github.niwaniwa.we.core.util.bar.Dragon;
import com.github.niwaniwa.we.core.util.message.LanguageType;
import com.github.niwaniwa.we.core.util.message.MessageManager;

public class WhiteEggCore extends JavaPlugin {

	private static WhiteEggCore instance;
	private static WhiteEggAPI api;
	private static MessageManager msg;
	private static LanguageType type;
	private static WhiteEggCoreConfig config;
	private PluginManager pm;

	@Override
	public void onEnable(){
		versionCheck();
		instance = this;
		api = new WhiteEggAPIImpl();
		pm = Bukkit.getPluginManager();
		msg = new MessageManager(this.getDataFolder() + "/lang/");
		this.set();
		WhitePlayerFactory.load(); // load
	}

	@Override
	public void onDisable(){
		WhitePlayerFactory.saveAll();  // IllegalPluginAccessException
		Dragon.disable();
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

	public static WhiteEggCoreConfig getConf(){
		return config;
	}

	private void set(){
		saveDefaultConfig();
		config = new WhiteEggCoreConfig();
		config.load();
		this.settingLanguage();
		this.registerCommands();
		this.registerListener();
		this.register();
		type = LanguageType.en_US;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		WhiteCommandSender whiteCommandSender;
		if(sender instanceof Player){
			whiteCommandSender = WhitePlayerFactory.newInstance((Player) sender);
		} else {
			whiteCommandSender = new WhiteConsoleSender(true);
		}
		return new WhiteEggCoreCommandHandler().onCommand(whiteCommandSender, command, label, args);
	}

	private void registerListener(){
		pm.registerEvents(new Debug(), this);
		pm.registerEvents(new PlayerListener(), this);
	}

	private void registerCommands(){
		WhiteEggCoreCommandHandler handler = new WhiteEggCoreCommandHandler();
		handler.registerCommand("whiteeggcore", new WhiteEggCoreCommand());
		handler.registerCommand("toggle", new WhiteEggToggleCommand());
		handler.registerCommand("head", new WhiteEggHeadCommand());
		handler.registerCommand("tweet", new WhiteEggTwitterCommand());
		handler.registerCommand("register", new WhiteEggTwitterRegisterCommand());
		handler.registerCommand("whisper", new WhiteEggWhisperCommand());
		handler.registerCommand("replay", new WhiteEggReplayCommand());
	}

	private void settingLanguage(){
		copyLangFiles(false);
		try {
			msg.loadLangFile();
		} catch (IOException | InvalidConfigurationException e1) {
			e1.printStackTrace();
		}
		msg.replaceDefaultLanguage(true);
		if(!msg.getLangs().isEmpty()){ return; }
		this.load(msg, LanguageType.ja_JP);
	}

	private void load(MessageManager msg, LanguageType type){
		JarFile jar = null;
		BufferedReader buffer = null;
		try {
			jar = new JarFile(getInstance().getFile());
			JarEntry entry = jar.getJarEntry("lang/" + type.getString() +".yml");
			buffer = new BufferedReader(new InputStreamReader(jar.getInputStream(entry), "UTF-8"));
			msg.loadLangFile(type, buffer);
		} catch (InvalidConfigurationException | IOException e) {
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


	@Override
	public File getFile() {
		return super.getFile();
	}

	public Map<String, AbstractWhiteEggCoreCommand> getCommands(){
		return WhiteEggCoreCommandHandler.getCommans();
	}

}