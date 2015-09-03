package com.github.niwaniwa.we.core.util.message;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.player.WhitePlayer;

public class MessageManager {

	private File path;
	private Map<LanguageType, YamlConfiguration> langs = new HashMap<>();

	public MessageManager(File langPathFolder) {
		this.path = langPathFolder;
	}

	public MessageManager(String string) {
		this(new File(string));
	}

	public String getMessage(LanguageType lang, String key, String prefix, boolean replaceColorCode){
		if(langs.isEmpty()){ return null; }
		if(langs.get(lang) == null){ return null; }
		YamlConfiguration yaml = langs.get(lang);
		if(yaml.getString(key) == null){ return null; }
		String string = prefix + yaml.getString(key);
		if(replaceColorCode){
			string = ChatColor.translateAlternateColorCodes('&', string);
		}
		return string;
	}

	public <T extends WhitePlayer> String getMessage(T player, String key, String prefix, boolean replaceColorCode){
		return this.getMessage(getLanguage(player), key, prefix, replaceColorCode);
	}

	public boolean loadLangFile(){
		if(!path.exists()){ return false; }
		Map<LanguageType, YamlConfiguration> result = new HashMap<>();
		for(LanguageType type : LanguageType.values()){
			File langF = new File(path + "/" + type.getString());
			if(!langF.exists()){ continue; }
			result.put(type, YamlConfiguration.loadConfiguration(langF));
			continue;
		}
		if(result.isEmpty()){ return false; }
		langs.putAll(result);
		return true;
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
