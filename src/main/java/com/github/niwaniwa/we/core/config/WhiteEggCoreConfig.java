package com.github.niwaniwa.we.core.config;

import java.io.File;

import com.github.niwaniwa.we.core.WhiteEggCore;

public class WhiteEggCoreConfig extends WhiteConfig {

	private boolean lock;
	private boolean useDataBase;
	private boolean useTwitter;
	private boolean savePlayerData;
	private boolean disableListener;

	public WhiteEggCoreConfig() {
		super(WhiteEggCore.getInstance().getDataFolder(), "config.yml");
	}

	public void reload(){
		File file = new File(path, name);
		if(!file.exists()){
			WhiteEggCore.getInstance().saveDefaultConfig();
		}
		load();
		lock = yaml.getBoolean("lock", false);
		useDataBase = yaml.getBoolean("useDataBase", false);
		useTwitter = yaml.getBoolean("useTwitter", true);
		savePlayerData = yaml.getBoolean("savePlayerData", true);
		disableListener = yaml.getBoolean("disableListener", false);

	}

	public boolean isLock(){
		return lock;
	}

	public boolean useDataBase(){
		return useDataBase;
	}

	public boolean useTwitter(){
		return useTwitter;
	}

	public boolean savePlayerData(){
		return savePlayerData;
	}

	public boolean isDisableListener(){
		return disableListener;
	}

}
