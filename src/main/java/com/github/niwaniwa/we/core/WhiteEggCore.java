package com.github.niwaniwa.we.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
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

/**
 * メインクラス
 * @author niwaniwa
 *
 */
public class WhiteEggCore extends JavaPlugin {

	private static WhiteEggCore instance;
	private static WhiteEggAPI api;
	private static MessageManager msg;
	private static LanguageType type;
	private static WhiteEggCoreConfig config;
	private PluginManager pm;

	public static final String msgPrefix = "§7[§bWEC§7]§r";

	@Override
	public void onEnable(){
		long time = new Date().getTime();
		versionCheck();
		instance = this;
		api = new WhiteEggAPIImpl();
		pm = Bukkit.getPluginManager();
		msg = new MessageManager(this.getDataFolder() + File.separator + "lang" + File.separator);
		this.setting();
		this.load();
		System.out.println("complete : " + (new Date().getTime() - time));
	}

	@Override
	public void onDisable(){
		WhitePlayerFactory.saveAll();
		Rank.saveAll();
		Dragon.disable(); // ConcurrentModificationException
	}

	/**
	 * instanceの取得
	 * @return
	 */
	public static WhiteEggCore getInstance(){
		return instance;
	}

	/**
	 * APIインスタンスの取得
	 * @return
	 */
	public static WhiteEggAPI getAPI(){
		return api;
	}

	/**
	 * メッセージマネージャーの取得 {@link WhiteEggAPI}
	 * @return
	 */
	public static MessageManager getMessageManager(){
		return msg;
	}

	public static LanguageType getType() {
		return type;
	}

	public static WhiteEggCoreConfig getConf(){
		return config;
	}

	/**
	 * 初期化処理
	 */
	private void setting(){
		saveDefaultConfig();
		config = new WhiteEggCoreConfig();
		config.load();
		this.settingLanguage();
		this.registerCommands();
		this.registerListener();
		this.register();
		type = LanguageType.en_US;
	}

	/**
	 * 読み込み処理
	 */
	private void load(){
		WhitePlayerFactory.load();
		Rank.load();
	}

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
		pm.registerEvents(new Debug(true), this);
		pm.registerEvents(new PlayerListener(), this);
	}

	/**
	 * コマンドの登録
	 */
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

	/**
	 * 言語ファイルの読み込み、言語設定
	 */
	private void settingLanguage(){
		copyLangFiles(false);
		try {
			msg.loadLangFile();
		} catch (IOException | InvalidConfigurationException e) {
		}
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
		JarFile jar = null;
		BufferedReader buffer = null;
		try {
			jar = new JarFile(getInstance().getFile());
			JarEntry entry = jar.getJarEntry("lang" + File.separator + type.getString() +".yml");
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

	/**
	 * 各種登録
	 */
	private void register(){
		Rank r = new Rank("*", ChatColor.WHITE, "Owner", RankProperty.HIGHEST, "whiteegg.owner");
		r.add();
	}

	/**
	 * バージョンチェックメソッド
	 */
	private void versionCheck(){
		String packageName = getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);
		if(!version.equalsIgnoreCase("v1_8_R3")){
			getLogger().warning("Unsupported CraftBukkit Version >_< : " + version);
			getLogger().warning("Please use 1.8.8");
			pm.disablePlugin(instance);
			return;
		}
	}

	/**
	 * 言語ファイルの読み込み
	 * @param send 読み込み時にログに出力するか
	 */
	private void copyLangFiles(boolean send){
		for(LanguageType type : LanguageType.values()){
			if(new File(WhiteEggCore.getInstance().getDataFolder() + File.separator + "lang" + File.separator + type.getString() + ".yml").exists()){
				continue;
			}
			Util.copyFileFromJar(
					new File(WhiteEggCore.getInstance().getDataFolder() + File.separator + "lang" + File.separator),
					WhiteEggCore.getInstance().getFile(), "lang" + File.separator + type.getString() + ".yml");
		}
	}

	@Override
	public File getFile() {
		return super.getFile();
	}

	/**
	 * このプラグインに登録されているコマンドの取得
	 * @return
	 */
	public Map<String, AbstractWhiteEggCoreCommand> getCommands(){
		return WhiteEggCoreCommandHandler.getCommans();
	}

}