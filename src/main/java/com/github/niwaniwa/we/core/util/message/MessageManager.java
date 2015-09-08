package com.github.niwaniwa.we.core.util.message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;

public class MessageManager {

	private File path;
	private Map<LanguageType, YamlConfiguration> langs = new HashMap<>();
	private LanguageType type;
	private boolean b = false;

	public MessageManager(File langPathFolder) {
		this.path = langPathFolder;
		this.type = WhiteEggCore.getType();
	}

	public MessageManager(String string) {
		this(new File(string));
	}

	@Deprecated
	public MessageManager() {
	}

	private String getMessage(LanguageType lang, String key, String prefix, boolean replaceColorCode, boolean loop){
		String string = "";
		if(!langs.isEmpty()
				&& langs.get(lang) != null){
			YamlConfiguration yaml = langs.get(lang);
			if(yaml.getString(key) != null){
				string = prefix + yaml.getString(key);
				if(replaceColorCode){
					string = ChatColor.translateAlternateColorCodes('&', string);
				}
			}
		}
		if(string.isEmpty()
				&& loop){
			string = getMessage(type, key, prefix, replaceColorCode, false);
		}
		return string;
	}

	public String getMessage(LanguageType lang, String key, String prefix, boolean replaceColorCode){
		return getMessage(lang, key, prefix, replaceColorCode, b);
	}

	public <T extends WhitePlayer> String getMessage(T player, String key, String prefix, boolean replaceColorCode){
		return getMessage(getLanguage(player), key, prefix, replaceColorCode);
	}

	public <T extends WhiteCommandSender> String getMessage(T player, String key, String prefix, boolean replaceColorCode){
		if(!(player instanceof WhitePlayer)){
			return getMessage(WhiteEggCore.getType(), key, prefix, replaceColorCode);
		}
		return getMessage(WhiteEggCore.getType(), key, prefix, replaceColorCode);
	}

	public String getMessage(CommandSender sender, String key, String prefix, boolean replaceColorCode){
		if(!(sender instanceof Player)){
			return getMessage(WhiteEggCore.getType(), key, prefix, replaceColorCode);
		}
		return getMessage(WhitePlayerFactory.newInstance((Player) sender), key, prefix, replaceColorCode);
	}

	public boolean loadLangFile() throws FileNotFoundException, IOException, InvalidConfigurationException{
		Map<LanguageType, YamlConfiguration> result = new HashMap<>();
		for(LanguageType type : LanguageType.values()){
			File langF = new File(path + "/" + type.getString() + ".yml");
			if(!langF.exists()){ continue; }
			YamlConfiguration yaml = new YamlConfiguration();
			yaml.load(langF);
			result.put(type, yaml);
			continue;
		}
		if(result.isEmpty()){ return false; }
		langs.putAll(result);
		return true;
	}

	public boolean loadLangFile(LanguageType type, BufferedReader buffer) throws IOException, InvalidConfigurationException{
		YamlConfiguration yaml = new YamlConfiguration();
		yaml.load(buffer);
		langs.put(type, yaml);
		return true;
	}

	public void setDefaultLanguage(LanguageType type){
		this.type = type;
	}

	public LanguageType getType(){
		return type;
	}

	public void replaceDefaultLanguage(boolean b){
		this.b = b;
	}

	public boolean isReplaceDefaultLanguage(){
		return b;
	}

	public Map<LanguageType, YamlConfiguration> getLangs(){
		return langs;
	}

	public static <T extends WhitePlayer> LanguageType getLanguage(T player){
		return getLanguage(player.getPlayer());
	}

	public static LanguageType getLanguage(Player player){
		try{
			Object o = player.getClass().getMethod("getHandle").invoke(player);
			String s = (String) getValue(o, "locale");
			return LanguageType.valueOf(s);
		} catch(Exception e) {}
		return LanguageType.en_US;
	}

	private static Object getValue(Object instance, String fieldName) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}

}
