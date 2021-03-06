package com.github.niwaniwa.we.core;

import com.github.niwaniwa.we.core.command.WhiteEggCoreCommandHandler;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreBaseCommandExecutor;
import com.github.niwaniwa.we.core.config.WhiteEggCoreConfig;
import com.github.niwaniwa.we.core.database.DataBase;
import com.github.niwaniwa.we.core.init.Initialize;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.commad.WhiteConsoleSender;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.script.JavaScript;
import com.github.niwaniwa.we.core.util.Versioning;
import com.github.niwaniwa.we.core.util.message.LanguageType;
import com.github.niwaniwa.we.core.util.message.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

/**
 * メインクラス
 *
 * @author niwaniwa
 * @version 2.0.0
 *
 * created on 2015/08/25
 */
public class WhiteEggCore extends JavaPlugin {

    private static WhiteEggCore instance;
    private static MessageManager messageManager;
    private static LanguageType defaultLanguage = LanguageType.en_US;
    ;
    private static WhiteEggCoreConfig config;
    private static DataBase database;

    public static final String logPrefix = "[WhiteEggCore]";
    public static final String msgPrefix = "§7[§bWhiteEggCore§7]§r";

    public static Logger logger;
    public static Versioning version;

    public static boolean isLock = false;

    private PluginManager pluginManager = Bukkit.getPluginManager();
    private JavaScript script;

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
    public void onEnable() {
        instance = this;
        this.versionCheck();
        if (!version.isSupport()) {
            if (this.isEnabled()) {
                Bukkit.getPluginManager().disablePlugin(this);
            }
            return;
        }
        long time = System.nanoTime();
        this.init();
        long finish = (System.nanoTime() - time);
        logger.info(String.format("Done : %.3f s", new Object[]{Double.valueOf(finish / 1.0E9D)}));
    }

    /**
     * プラグインの終了処理
     */
    @Override
    public void onDisable() {
        if (!version.isSupport()) {
            return;
        }
        logger.info("Saving players (WhiteEgg)");
        WhitePlayerFactory.saveAll();
        Rank.saveAll();
        config.save();
    }

    /**
     * instanceの取得
     *
     * @return このプラグインのインスタンス
     */
    public static WhiteEggCore getInstance() {
        return instance;
    }

    /**
     * メッセージマネージャーの取得
     *
     * @return メッセージマネージャー
     */
    public static MessageManager getMessageManager() {
        return messageManager;
    }

    /**
     * デフォルトの言語を取得
     *
     * @return 言語
     */
    public static LanguageType getLanguage() {
        return defaultLanguage;
    }

    /**
     * コンフィグの取得
     *
     * @return コンフィグ
     */
    public static WhiteEggCoreConfig getConf() {
        return config;
    }

    private void init() {
        this.saveDefaultConfig();
        config = new WhiteEggCoreConfig();
        config.load();
        Initialize init = Initialize.getInstance(this);
        messageManager = init.getMessageManager();
        init.start(false);
        database = init.getDatabase();
        isLock = config.isLock();
        this.script = init.getScript();
        runTask();
    }

    /**
     * command
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        WhiteCommandSender whiteCommandSender;
        if (sender instanceof Player) {
            whiteCommandSender = WhitePlayerFactory.getInstance((Player) sender);
        } else {
            whiteCommandSender = new WhiteConsoleSender(true);
        }
        return WhiteEggCoreCommandHandler.onCommand(whiteCommandSender, command, label, args);
    }

    private void runTask() {
        final int run = config.getConfig().getInt("setting.player.save.autoSave.time", 3600 * 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!config.getConfig().getBoolean("setting.player.save.autoSave.enable", false)) {
                    this.cancel();
                    return;
                }
                WhitePlayerFactory.saveAll();
            }
        }.runTaskTimerAsynchronously(instance, run, run);
    }

    /**
     * バージョンチェック
     */
    private void versionCheck() {
        if (version.getJavaVersion() < Double.valueOf(Versioning.JAVA_SUPPORT_VERSION)) {
            logger.warning("Unsupported Java version >_< : " + version.getJavaVersion());
            logger.warning("Please use 1.8");
            pluginManager.disablePlugin(instance);
            return;
        }
/*        if (!version.getCraftBukkitVersion().equalsIgnoreCase("v1_8_R3")) {
            logger.warning("Unsupported CraftBukkit version >_< : " + version.getCraftBukkitVersion());
            logger.warning("Please use v1_8_R3");
            pluginManager.disablePlugin(instance);
            return;
        }
*/
        return;
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
     *
     * @return Map
     */
    public Map<String, WhiteEggCoreBaseCommandExecutor> getCommands() {
        return WhiteEggCoreCommandHandler.getCommans();
    }

    public JavaScript getScript() {
        return script;
    }

    public static DataBase getDataBase() {
        return database;
    }

    public void setScript(JavaScript s) {
        this.script = s;
    }


}