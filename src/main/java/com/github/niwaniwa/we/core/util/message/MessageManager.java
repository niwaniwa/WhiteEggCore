package com.github.niwaniwa.we.core.util.message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;

public class MessageManager {

	private File path;
	private final Map<LanguageType, YamlConfiguration> langs = new HashMap<>();
	private LanguageType type;
	private boolean b = false;

	public MessageManager(File langPathFolder) {
		this.path = langPathFolder;
		this.type = WhiteEggCore.getType();
	}

	public MessageManager(String string) {
		this(new File(string));
	}

	@SuppressWarnings("unused")
	@Deprecated
	private MessageManager() {
	}

	/**
	 * 指定された言語のメッセージを返します。
	 * @param lang 言語
	 * @param key キー
	 * @param prefix prefix
	 * @param replaceColorCode カラーコードの置換。
	 * @param loop 見つからなかった場合、デフォルトの言語で検索し取得します。
	 * @return メッセージ
	 */
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

	/**
	 * 指定された言語のメッセージを返します。
	 * @param lang 言語
	 * @param key キー
	 * @param prefix prefix
	 * @param replaceColorCode カラーコードの置換。
	 * @return メッセージ
	 */
	public String getMessage(LanguageType lang, String key, String prefix, boolean replaceColorCode){
		return getMessage(lang, key, prefix, replaceColorCode, b);
	}

	/**
	 * プレイヤーの言語に合わせたメッセージを返します。
	 * @param <T>
	 * @param sender WhiteCommandSenderを継承したクラスのインスタンス
	 * @param key 取得するkey
	 * @param prefix prefix
	 * @param replaceColorCode カラーコードの置換
	 * @return value
	 */
	public <T extends WhiteCommandSender> String getMessage(T sender, String key, String prefix, boolean replaceColorCode){
		if(!(sender instanceof WhitePlayer)){
			return getMessage(WhiteEggCore.getType(), key, prefix, replaceColorCode);
		}
		return getMessage(WhiteEggCore.getType(), key, prefix, replaceColorCode);
	}

	/**
	 * プレイヤーの言語に合わせたメッセージを返します。
	 * @param sender WhiteCommandSenderを継承したクラスのインスタンス
	 * @param key 取得するkey
	 * @param prefix prefix
	 * @param replaceColorCode カラーコードの置換
	 * @return value
	 */
	public String getMessage(CommandSender sender, String key, String prefix, boolean replaceColorCode){
		if(!(sender instanceof Player)){
			return getMessage(WhiteEggCore.getType(), key, prefix, replaceColorCode);
		}
		return getMessage(WhitePlayerFactory.newInstance((Player) sender), key, prefix, replaceColorCode);
	}

	/**
	 * 指定されたパスにある言語ファイルを取得します。
	 * @return 読み込みが成功したか
	 * @throws FileNotFoundException Exception
	 * @throws IOException Exception
	 * @throws InvalidConfigurationException Exception
	 */
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

	/**
	 * BufferedReaderから取得します
	 * @param type 対象言語
	 * @param buffer ばっふぁ
	 * @return 成功したか
	 * @throws IOException Exception
	 * @throws InvalidConfigurationException Exception
	 */
	public boolean loadLangFile(LanguageType type, BufferedReader buffer) throws IOException, InvalidConfigurationException{
		YamlConfiguration yaml = new YamlConfiguration();
		yaml.load(buffer);
		langs.put(type, yaml);
		return true;
	}

	/**
	 * デフォルトで取得する言語を設定します
	 * @param type 指定言語
	 */
	public void setDefaultLanguage(LanguageType type){
		this.type = type;
	}

	/**
	 * 現在設定されているでふぁおるとの言語を取得します。
	 * @return 言語
	 */
	public LanguageType getType(){
		return type;
	}

	/**
	 * デフォルトでメッセージを取得できない場合にデファオルトの言語を取得するかを設定します
	 * @param b boolean
	 */
	public void replaceDefaultLanguage(boolean b){
		this.b = b;
	}

	/**
	 * デフォルトでメッセージを取得できない場合にデファオルトの言語を取得するか
	 * @return boolean
	 */
	public boolean isReplaceDefaultLanguage(){
		return b;
	}

	/**
	 * 読み込まれている言語データを返します。
	 * @return Map<LanguageType, YamlConfiguration> langs
	 */
	public Map<LanguageType, YamlConfiguration> getLangs(){
		return langs;
	}

	/**
	 * プレイヤーが現在設定している言語を返します
	 * @param player
	 * @return LanguageType 言語
	 */
	public static <T extends WhitePlayer> LanguageType getLanguage(T player){
		return getLanguage(player.getPlayer());
	}

	/**
	 * プレイヤーが現在設定している言語を返します
	 * @param player
	 * @return LanguageType 言語
	 */
	public static LanguageType getLanguage(Player player){
		String locale = ((CraftPlayer) player).getHandle().locale;
		LanguageType type = LanguageType.valueOf(locale);
		return type == null ? LanguageType.en_US : type;
	}

}
