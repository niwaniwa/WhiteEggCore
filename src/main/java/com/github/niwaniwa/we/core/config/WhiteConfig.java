package com.github.niwaniwa.we.core.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author configgggg
 *
 */
public class WhiteConfig {

	File path;
	String name;
	YamlConfiguration yaml;

	public WhiteConfig(File path, String fileName) {
		this.path = path;
		this.name = fileName;
	}

	public void createConfig(){
		if(!path.exists()){ path.mkdirs(); }
		File file = new File(path, "/" + name);
		if(file.exists()){ return; }
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean load(){
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(new File(path, "/" + name));
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public YamlConfiguration getConfig(){
		return yaml;
	}

}
