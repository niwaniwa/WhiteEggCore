package com.github.niwaniwa.we.core.config;

import java.io.File;

import com.github.niwaniwa.we.core.WhiteEggCore;

public class WhiteEggCoreConfig extends WhiteConfig {

    private boolean lock;

    private static final String settingPath = "setting.";

    public WhiteEggCoreConfig() {
        super(WhiteEggCore.getInstance().getDataFolder(), "config.yml");
    }

    public void reload() {
        File file = new File(path, name);
        if (!file.exists()) {
            WhiteEggCore.getInstance().saveDefaultConfig();
        }
        load();
        lock = yaml.getBoolean("lock", false);
    }

    public void set(String key, Object value) {
        yaml.set(key, value);
    }

    public boolean isLock() {
        return lock;
    }

    public boolean useDataBase() {
        return yaml.getBoolean(settingPath + "database.enable", false);
    }

    public boolean useTwitter() {
        return yaml.getBoolean(settingPath + "twitter.useTwitter", true);
    }

    public boolean savePlayerData() {
        return yaml.getBoolean(settingPath + "player.savePlayerData", true);
    }

    public boolean isEnableCommand() {
        return yaml.getBoolean(settingPath + "enableCommands", true);
    }

    public boolean isEnableListener() {
        return yaml.getBoolean(settingPath + "enableListener", true);
    }

    public boolean isEnableScript() {
        return yaml.getBoolean(settingPath + "script.enable", false);
    }

}
