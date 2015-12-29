package com.github.niwaniwa.we.core.initialization;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.PluginManager;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.WhiteEggCoreCommandHandler;
import com.github.niwaniwa.we.core.command.WhiteEggHeadCommand;
import com.github.niwaniwa.we.core.command.WhiteEggReplayCommand;
import com.github.niwaniwa.we.core.command.WhiteEggScriptCommand;
import com.github.niwaniwa.we.core.command.WhiteEggWhisperCommand;
import com.github.niwaniwa.we.core.command.core.WhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.toggle.WhiteEggToggleCommand;
import com.github.niwaniwa.we.core.command.twitter.WhiteEggTwitterCommand;
import com.github.niwaniwa.we.core.command.twitter.WhiteEggTwitterRegisterCommand;
import com.github.niwaniwa.we.core.config.WhiteEggCoreConfig;
import com.github.niwaniwa.we.core.database.DataBase;
import com.github.niwaniwa.we.core.database.DataBaseManager;
import com.github.niwaniwa.we.core.database.mongodb.MongoDataBaseManager;
import com.github.niwaniwa.we.core.listener.Debug;
import com.github.niwaniwa.we.core.listener.PlayerListener;
import com.github.niwaniwa.we.core.listener.ScriptListener;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.script.JavaScript;
import com.github.niwaniwa.we.core.util.message.MessageManager;

public class Initialization implements Base, Listener {

	private static boolean enable = false;

	private WhiteEggCoreConfig config;
	private JavaScript script;
	private WhiteEggCore mainClassInstance;
	private MessageManager msg;
	private DataBase database;

	private Initialization(WhiteEggCore instance){
		mainClassInstance = instance;
		config = WhiteEggCore.getConf();
		Bukkit.getPluginManager().registerEvents(this, mainClassInstance);
	}

	@Override
	public boolean start(boolean debug){
		this.setting(debug);
		LoadLanguage lang = LoadLanguage.getInstance();
		lang.start(false);
		msg = lang.getManager();
		enable = true;
		return enable;
	}

	public void debugMessage(String message, boolean debug){
		if(debug){ WhiteEggCore.logger.info("setting " + message + " ..."); }
	}

	private void setting(boolean debug){
//		debugMessage("commands", debug);
//		debugMessage("listener", debug);
		this.register();
		this.registerCommands();
		this.registerListener();
		this.load();
		this.settingDatabase();
		this.settingCheck();
	}

	private void load(){
		WhitePlayerFactory.load();
		Rank.load();
	}

	/**
	 * 各種登録
	 */
	private void register(){
		this.registerDatabaseClass();
		if(!config.isEnableScript()){ return; }
		try {
			JavaScript.copyScript();
		} catch (IOException e) {
			e.printStackTrace();
		}
		script = JavaScript.loadScript();
	}

	/**
	 * コマンドの登録
	 */
	private void registerCommands(){
		if(!config.isEnableCommand()){ return; }
		WhiteEggCoreCommandHandler handler = new WhiteEggCoreCommandHandler();
		handler.registerCommand("whiteeggcore", new WhiteEggCoreCommand());
		handler.registerCommand("toggle", new WhiteEggToggleCommand());
		handler.registerCommand("head", new WhiteEggHeadCommand());
		handler.registerCommand("register", new WhiteEggTwitterRegisterCommand());
		handler.registerCommand("whisper", new WhiteEggWhisperCommand());
		handler.registerCommand("replay", new WhiteEggReplayCommand());
		handler.registerCommand("script", new WhiteEggScriptCommand());
		if(config.useTwitter()){ handler.registerCommand("tweet", new WhiteEggTwitterCommand()); }
	}

	/**
	 * リスナーの登録
	 */
	private void registerListener(){
		if(!config.isEnableListener()){ return; }
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Debug(), mainClassInstance);
		pm.registerEvents(new PlayerListener(), mainClassInstance);
		new ScriptListener();
	}

	public static boolean isEnable() {
		return enable;
	}

	public JavaScript getScript() {
		return script;
	}

	public DataBase getDatabase() {
		return database;
	}

	private void settingCheck(){
		if(!config.getConfig().getBoolean("warn", true)){ return; }
		if(config.getConfig().getString("setting.twitter.consumerKey", "").isEmpty()
				|| config.getConfig().getString("setting.twitter.consumerSecret", "").isEmpty()){
			WhiteEggCore.logger.warning("Twitter Consumer key or Consumer Secret is empty");
			WhiteEggCore.logger.warning("Twitter command disable");
			config.getConfig().set("setting.twitter.useTwitter", false);
		}
		if(!config.isEnableListener() || !config.isEnableCommand()){
			WhiteEggCore.logger.warning("Listener or Command is disabled. Not recommended.");
		}
	}

	private void registerDatabaseClass(){
		DataBaseManager.register("MongoDB", MongoDataBaseManager.class);
	}

	private void settingDatabase(){
		if(!config.useDataBase()){ return; }
		for(Entry<String, Class<? extends DataBase>> entry : DataBaseManager.getDatabaseClass().entrySet()){
			if(entry.getKey().equalsIgnoreCase(config.getConfig().getString("setting.database.type"))){
				try {
					Constructor<? extends DataBase> constructor = entry.getValue().getConstructor(String.class, int.class);
					this.database = constructor.newInstance(config.getConfig().getString("setting.database.host"), config.getConfig().getInt("setting.database.port"));
				} catch (Exception e) {
					config.getConfig().set("setting.database.enable", false);
					e.printStackTrace();
				}
			}
		}
	}

	public MessageManager getMessageManager(){
		return msg;
	}

	public WhiteEggCoreConfig getConfig(){
		return config;
	}

	public static Initialization getInstance(WhiteEggCore instance){
		if(enable){ return null; }
		return new Initialization(instance);
	}

	@EventHandler
	public void onDisable(PluginDisableEvent event){
		if(event.getPlugin().getName().equalsIgnoreCase("WhiteEggCore")){ enable = false; }
	}

}
