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

	protected File path;
	protected String name;
	protected YamlConfiguration yaml;

	public WhiteConfig(File path, String fileName) {
		this.path = path;
		this.name = fileName;
	}

	public void createConfig(){
		if(!path.exists()){ path.mkdirs(); }
		File file = new File(path, File.separator + name);
		if(file.exists()){ return; }
		try {
			file.createNewFile();
		} catch (IOException e) {
		}
	}

	public boolean load(){
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(new File(path, File.separator + name));
		} catch (IOException | InvalidConfigurationException e) {
		}
		return true;
	}

	public boolean save(){
		try {
			yaml.save(path + File.separator + name);
		} catch (IOException e) {
		}
		return true;
	}

	public YamlConfiguration getConfig(){
		return yaml;
	}

}
