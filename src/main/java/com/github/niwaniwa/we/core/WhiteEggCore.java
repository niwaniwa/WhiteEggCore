package com.github.niwaniwa.we.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.command.WhiteEggCoreCommandHandler;
import com.github.niwaniwa.we.core.command.WhiteEggHeadCommand;
import com.github.niwaniwa.we.core.command.WhiteEggReplayCommand;
import com.github.niwaniwa.we.core.command.WhiteEggScriptCommand;
import com.github.niwaniwa.we.core.command.WhiteEggWhisperCommand;
import com.github.niwaniwa.we.core.command.abstracts.AbstractWhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.core.WhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.toggle.WhiteEggToggleCommand;
import com.github.niwaniwa.we.core.command.twitter.WhiteEggTwitterCommand;
import com.github.niwaniwa.we.core.command.twitter.WhiteEggTwitterRegisterCommand;
import com.github.niwaniwa.we.core.config.WhiteEggCoreConfig;
import com.github.niwaniwa.we.core.listener.PlayerListener;
import com.github.niwaniwa.we.core.listener.ScriptListener;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.commad.WhiteConsoleSender;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.script.JavaScript;
import com.github.niwaniwa.we.core.util.Util;
import com.github.niwaniwa.we.core.util.Versioning;
import com.github.niwaniwa.we.core.util.message.LanguageType;
import com.github.niwaniwa.we.core.util.message.MessageManager;

/**
 * メインクラス
 * @author niwaniwa
 * @version 2.0.0
 */
public class WhiteEggCore extends JavaPlugin {

	private static WhiteEggCore instance;
	private static WhiteEggAPI api = WhiteEggAPI.getAPI();
	private static MessageManager msg;
	private static LanguageType type = LanguageType.en_US;;
	private static WhiteEggCoreConfig config;

	private PluginManager pm = Bukkit.getPluginManager();
	private JavaScript script;

	public static final String logPrefix = "[WhiteEggCore]";
	public static final String msgPrefix = "§7[§bWhiteEggCore§7]§r";

	public static Logger logger;
	public static Versioning version;

	@Override
	public void onLoad() {
		logger = this.getLogger();
		logger.info("Checking version....");
		version = Versioning.getInstance();
	}

	/**
	 * プラグインの初期化処理
	 */
	@Override
	public void onEnable(){
		this.versionCheck();
		if (!version.isSupport()) { return; }
		long time = System.nanoTime();
		this.init();
		long finish = (System.nanoTime() - time);
		logger.info(String.format("Done : %.3f  s", new Object[] { Double.valueOf(finish / 1.0E9D) }));
	}

	/**
	 * プラグインの終了処理
	 */
	@Override
	public void onDisable(){
		if (!version.isSupport()) { return; }
		WhitePlayerFactory.saveAll();
		Rank.saveAll();
	}

	/**
	 * instanceの取得
	 * @return このプラグインのインスタンス
	 */
	public static WhiteEggCore getInstance(){
		return instance;
	}

	/**
	 * APIインスタンスの取得
	 * @return APIを返します
	 */
	public static WhiteEggAPI getAPI(){
		return api;
	}

	/**
	 * メッセージマネージャーの取得
	 * @return メッセージマネージャー
	 */
	public static MessageManager getMessageManager(){
		return msg;
	}

	/**
	 * デフォルトの言語を取得
	 * @return 言語
	 */
	public static LanguageType getType() {
		return type;
	}

	/**
	 * コンフィグの取得
	 * @return コンフィグ
	 */
	public static WhiteEggCoreConfig getConf(){
		return config;
	}

	private void init(){
		instance = this;
		msg = new MessageManager(this.getDataFolder() + File.pathSeparator +"lang" + File.pathSeparator);
		this.setting();
	}

	/**
	 * 初期化処理
	 */
	private void setting(){
		this.loadConfig();
		this.register();
		this.registerCommands();
		this.registerListener();
		this.settingLanguage();
		this.load();
		this.settingCheck();
		this.runTask();
	}

	private void loadConfig(){
		saveDefaultConfig();
		config = new WhiteEggCoreConfig();
		config.load();
	}

	private void settingCheck(){
		if(config.getConfig().getString("setting.twitter.consumerKey", "").isEmpty()
				|| config.getConfig().getString("setting.twitter.consumerSecret", "").isEmpty()){
			logger.warning("Twitter Consumer key or Consumer Secret is empty");
			logger.warning("Twitter command disable");
			config.getConfig().set("setting.twitter.useTwitter", false);
		}
	}

	/**
	 * 読み込み処理
	 */
	private void load(){
		WhitePlayerFactory.load();
		Rank.load();
	}

	/**
	 * command
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		WhiteCommandSender whiteCommandSender;
		if(sender instanceof Player){
			whiteCommandSender = WhitePlayerFactory.newInstance((Player) sender);
		} else {
			whiteCommandSender = new WhiteConsoleSender(true);
		}
		return WhiteEggCoreCommandHandler.onCommand(whiteCommandSender, command, label, args);
	}

	/**
	 * リスナーの登録
	 */
	private void registerListener(){
//		pm.registerEvents(new Debug(), this);
		pm.registerEvents(new PlayerListener(), this);
		new ScriptListener();
	}

	/**
	 * コマンドの登録
	 */
	private void registerCommands(){
		if(!config.getConfig().getBoolean("setting.enableCommands", true)){ return; }
		WhiteEggCoreCommandHandler handler = new WhiteEggCoreCommandHandler();
		handler.registerCommand("whiteeggcore", new WhiteEggCoreCommand());
		handler.registerCommand("toggle", new WhiteEggToggleCommand());
		handler.registerCommand("head", new WhiteEggHeadCommand());
		handler.registerCommand("register", new WhiteEggTwitterRegisterCommand());
		handler.registerCommand("whisper", new WhiteEggWhisperCommand());
		handler.registerCommand("replay", new WhiteEggReplayCommand());
		handler.registerCommand("script", new WhiteEggScriptCommand());
		if(config.getConfig().getBoolean("setting.twitter.useTwitter", false)){
			handler.registerCommand("tweet", new WhiteEggTwitterCommand());
		}
	}

	private void runTask(){
		final int run = config.getConfig().getInt("setting.autoSave.time", 300000);
		new BukkitRunnable() {
			@Override
			public void run() {
				if(!config.getConfig().getBoolean("setting.autoSave.enable", false)){ this.cancel(); }
				WhitePlayerFactory.saveAll();
			}
		}.runTaskTimerAsynchronously(instance, run, run);
	}

	/**
	 * 言語ファイルの読み込み、言語設定
	 */
	private void settingLanguage(){
		this.copyLangFiles(false);
		try {
			msg.loadLangFile();
		} catch (IOException | InvalidConfigurationException e) {}
		msg.replaceDefaultLanguage(true);
		if(!msg.getLangs().isEmpty()){ return; }
		this.load(msg, LanguageType.ja_JP);
	}

	/**
	 * 言語ファイルの読み込み
	 * @param msg 保存先のインスタンス
	 * @param type 言語種類 {@link LanguageType}
	 */
	private void load(MessageManager msg, LanguageType type){
		BufferedReader buffer = null;
		try {
			InputStreamReader reader = new InputStreamReader(
					this.getClass().getClassLoader()
					.getResourceAsStream(
							"lang/" + type.getString() +".yml"), StandardCharsets.UTF_8);
			buffer = new BufferedReader(reader);
			msg.loadLangFile(type, buffer);
		} catch (InvalidConfigurationException | IOException e) {
		} finally {
			try{ if(buffer != null){ buffer.close(); }
			} catch (IOException e){}
		}
	}

	/**
	 * 各種登録
	 */
	private void register(){
		if(config.getConfig().getBoolean("setting.enablecCommands"))
		try {
			JavaScript.copyScript();
		} catch (IOException e) {
			e.printStackTrace();
		}
		script = JavaScript.loadScript();
	}

	/**
	 * バージョンチェック
	 */
	private void versionCheck(){
		if(version.getJavaVersion() <= 1.7){
			logger.warning("Unsupported Java Version >_< : " + version.getJavaVersion());
			logger.warning("Please use 1.8");
			pm.disablePlugin(instance);
			return;
		}
		if(!version.getCraftBukkitVersion().equalsIgnoreCase("v1_8_R3")){
			logger.warning("Unsupported CraftBukkit Version >_< : " + version);
			logger.warning("Please use v1_8_R3");
			pm.disablePlugin(instance);
			return;
		}
		return;
	}

	/**
	 * 言語ファイルの読み込み
	 * @param send 読み込み時にログに出力するか
	 */
	private void copyLangFiles(boolean send){
		for(LanguageType type : LanguageType.values()){
			File path = new File(WhiteEggCore.getInstance().getDataFolder() + File.separator + "lang" + File.separator  + type.getString() + ".yml");
			if(path.exists()){
				continue;
			}
			if(send){ logger.info(" " + type.getString() + " : loading now..."); }
			Util.copyFileFromJar(new File(WhiteEggCore.getInstance().getDataFolder() + "/lang/"),
					WhiteEggCore.getInstance().getFile(), "lang/" + type.getString() + ".yml");
			if(send){ logger.info(" " + type.getString() + " : " + (path.exists() ? "complete" : "failure")); }
		}
	}

	/**
	 * JarFileのパスの取得
	 */
	@Override
	public File getFile() {
		return super.getFile();
	}

	/**
	 * このプラグインに登録されているコマンドの取得
	 * @return Map
	 */
	public Map<String, AbstractWhiteEggCoreCommand> getCommands(){
		return WhiteEggCoreCommandHandler.getCommans();
	}

	public JavaScript getScript() {
		return script;
	}

	public void setScript(JavaScript s){
		this.script = s;
	}

}