package com.github.niwaniwa.we.core.util.message;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.util.Reflection;
import com.github.niwaniwa.we.core.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.jar.JarFile;

/**
 * yaml形式のファイルを読み込みます
 * @author mmott
 *
 */
public class MessageManager {

	private File path;
	private final Map<LanguageType, YamlConfiguration> langs = new HashMap<>();
	private final List<MessageExtension> extension = new ArrayList<>();
	private LanguageType type;
	private boolean replaceDefaultLanguage = false;

	/**
	 * Constructor
	 * @param langPathFolder 言語ファイルが格納されている階層
	 */
	public MessageManager(File langPathFolder) {
		this(langPathFolder, WhiteEggCore.getLanguage());
	}

	public MessageManager(File langPathFolder, LanguageType defaultLanguage) {
		this.path = langPathFolder;
		this.type = defaultLanguage;
	}

	/**
	 * Constructor
	 * @param langPathFolder 言語ファイルが格納されている(される)階層
	 * @param jarPath 言語ファイルが格納されているJarファイルのパス
	 * @param jar 言語ファイルが格納されているJarファイルのパスのJarインスタンス
	 * @param jarLangPathFolder jar内の言語ファイルが格納されているフォルダーを差すパス
	 * @param defaultLanguage デフォルトで表示される言語
	 */
	private MessageManager(File langPathFolder, String jarPath, JarFile jar, String jarLangPathFolder, LanguageType defaultLanguage) {
		if(langPathFolder == null){ throw new IllegalArgumentException("file is not null"); }
		this.path = langPathFolder;
		this.type = defaultLanguage;
		File targetPath = new File(jarPath);
		Arrays.asList(LanguageType.values()).forEach(type -> Util.copyFileFromJar(langPathFolder, targetPath, jarLangPathFolder + "/" + type.getString() + ".yml"));
	}

	/**
	 * Constructor
	 * @param langPathFolder 言語ファイルが格納されている(される)階層
	 * @param jarPath 言語ファイルが格納されているJarファイルのパス
	 * @param jarLangPathFolder jar内の言語ファイルが格納されているフォルダーを差すパス
	 * @param defaultLanguage デフォルトで表示される言語
	 * @throws IOException 入出力エラー
	 */
	public MessageManager(File langPathFolder, String jarPath, String jarLangPathFolder, LanguageType defaultLanguage) throws IOException{
		this(langPathFolder, jarPath, new JarFile(jarPath), jarLangPathFolder, defaultLanguage);
	}

	/**
	 * Constructor
	 * @param path 言語ファイルが格納されている階層
	 */
	public MessageManager(String path) {
		this(new File(path));
	}

	/**
	 * このJarファイル(WhiteEggCore)に含まれている言語ファイルを使用して初期化します
	 */
	public MessageManager() {
		try {
			loadLangFile(WhiteEggCore.getLanguage(),
					new BufferedReader(
							new InputStreamReader(
									getClass().getClassLoader().getResourceAsStream(
											"lang/" + WhiteEggCore.getLanguage().toString() + ".yml"))));
		} catch (IOException | InvalidConfigurationException e) {}
	}

	/**
	 * 拡張
	 * @return List 使用した拡張クラスのインスタンスを返す
	 */
	public List<MessageExtension> getExtension() {
		return extension;
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
		return getMessage(lang, key, prefix, replaceColorCode, replaceDefaultLanguage);
	}

	/**
	 * プレイヤーの言語に合わせたメッセージを返します。
	 * @param sender WhiteCommandSenderを継承したクラスのインスタンス
	 * @param key 取得するkey
	 * @param prefix prefix
	 * @param replaceColorCode カラーコードの置換
	 * @return value
	 */
	public String getMessage(WhiteCommandSender sender, String key, String prefix, boolean replaceColorCode){
		if(!(sender instanceof WhitePlayer)){
			return getMessage(WhiteEggCore.getLanguage(), key, prefix, replaceColorCode);
		}
		return getMessage(getLanguage((WhitePlayer) sender), key, prefix, replaceColorCode);
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
			return getMessage(WhiteEggCore.getLanguage(), key, prefix, replaceColorCode);
		}
		return getMessage(WhitePlayerFactory.getInstance((Player) sender), key, prefix, replaceColorCode);
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
			File langF = new File(path + File.separator + type.getString() + ".yml");
			if(!langF.exists()){ continue; }
			YamlConfiguration yaml = new YamlConfiguration();
			yaml.load(new BufferedReader(new InputStreamReader(new FileInputStream(langF), Charset.forName("UTF-8"))));
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
	 * 言語ファイルのリロード
	 * @return リロードが成功したか
     */
	public boolean reload(){
		if(path == null){
			try {
				loadLangFile(WhiteEggCore.getLanguage(), new BufferedReader(new InputStreamReader(getClass()
						.getClassLoader().getResourceAsStream("lang/" + WhiteEggCore.getLanguage().toString() + ".yml"))));
			} catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
			return true;
		} else if(!path.exists()){ return false; }
		clear();
		try {
			return loadLangFile();
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return false;
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
	 * @param isReplaceDefaultLanguage boolean
	 */
	public void replaceDefaultLanguage(boolean isReplaceDefaultLanguage){
		this.replaceDefaultLanguage = isReplaceDefaultLanguage;
	}

	/**
	 * デフォルトでメッセージを取得できない場合にデファオルトの言語を取得するか
	 * @return boolean
	 */
	public boolean isReplaceDefaultLanguage(){
		return replaceDefaultLanguage;
	}

	/**
	 * 拡張機能を使用しメッセージを改変します
	 * @param message 元となる文字列
	 * @param extension 使用する機能
	 * @param add 履歴に追加するか
	 * @return 改変された文字列
	 */
	public String extension(String message, MessageExtension extension, boolean add){
		if(add){ this.extension.add(extension); }
		return extension.execute(message);
	}

	/**
	 * 既に使用した機能を検索し使用します
	 * @param message 元となる文字列
	 * @param extension MessageExtensionを継承した使用する機能のクラス
	 * @return 改変された文字列
	 */
	public String extension(String message, Class<?> extension){
		for(MessageExtension e : this.extension){
			if(e.getClass().equals(extension)){
				return extension(message, e, false);
			}
		}
		return message;
	}

	/**
	 * 読み込まれている言語データを返します。
	 * @return Map langs
	 */
	public Map<LanguageType, YamlConfiguration> getLangs(){
		return langs;
	}

	public void clear(){
		langs.clear();
	}

	/**
	 * プレイヤーが現在設定している言語を返します
	 * @param player プレイヤー
	 * @return LanguageType 言語
	 */
	public static LanguageType getLanguage(WhitePlayer player){
		return getLanguage(player.getPlayer());
	}

	/**
	 * プレイヤーが現在設定している言語を返します
	 * @param player プレイヤー
	 * @return LanguageType 言語
	 */
	public static LanguageType getLanguage(Player player){
		Object entity = Reflection.getEntityPlayer(player);
		Object locale = null;
		try {
			locale = entity.getClass().getField("locale").get(entity);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			WhiteEggCore.logger.info(e.getMessage());
			return WhiteEggCore.getLanguage();
		}
		if(locale == null){ return WhiteEggCore.getLanguage(); }
		LanguageType type = LanguageType.valueOf(String.valueOf(locale));
		return type == null ? WhiteEggCore.getLanguage() : type;
	}

}
