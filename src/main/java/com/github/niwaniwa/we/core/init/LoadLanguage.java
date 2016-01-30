package com.github.niwaniwa.we.core.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.InvalidConfigurationException;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.util.Util;
import com.github.niwaniwa.we.core.util.message.LanguageType;
import com.github.niwaniwa.we.core.util.message.MessageManager;

public class LoadLanguage implements Base{

	private MessageManager manager ;

	private LoadLanguage(){
		this.manager = new MessageManager(WhiteEggCore.getInstance().getDataFolder() + File.separator +"lang" + File.separator);
	}

	@Override
	public boolean start(boolean debug) {
		this.settingLanguage();
		return true;
	}

	/**
	 * 言語ファイルの読み込み、言語設定
	 */
	private void settingLanguage(){
		this.copyLangFiles(false);
		try {
			manager.loadLangFile();
		} catch (IOException | InvalidConfigurationException e) {}
		manager.replaceDefaultLanguage(true);
		if(!manager.getLangs().isEmpty()){ return; }
		this.load(manager, LanguageType.ja_JP);
	}

	/**
	 * 言語ファイルの読み込み
	 * @param msg 保存先のインスタンス
	 * @param type 言語種類 {@link LanguageType}
	 */
	private void load(MessageManager msg, LanguageType type){
		BufferedReader buffer = null;
		try {
			InputStreamReader reader = new InputStreamReader(
					this.getClass().getClassLoader()
					.getResourceAsStream(
							"lang/" + type.getString() +".yml"), StandardCharsets.UTF_8);
			buffer = new BufferedReader(reader);
			msg.loadLangFile(type, buffer);
		} catch (InvalidConfigurationException | IOException e) {
		} finally {
			try{ if(buffer != null){ buffer.close(); }
			} catch (IOException e){}
		}
	}

	/**
	 * 言語ファイルの読み込み
	 * @param send 読み込み時にログに出力するか
	 */
	private void copyLangFiles(boolean send){
		for(LanguageType type : LanguageType.values()){
			File path = new File(WhiteEggCore.getInstance().getDataFolder() + File.separator + "lang" + File.separator  + type.getString() + ".yml");
			if(path.exists()){
				if(send){ WhiteEggCore.logger.info(" " + type.getString() + " : file already exists "); }
				continue;
			}
			if(send){ WhiteEggCore.logger.info(" " + type.getString() + " : loading now..."); }
			Util.copyFileFromJar(new File(WhiteEggCore.getInstance().getDataFolder() + File.separator + "lang" + File.separator),
					WhiteEggCore.getInstance().getFile(), "lang/" + type.getString() + ".yml");
			if(send){ WhiteEggCore.logger.info(" " + type.getString() + " : " + (path.exists() ? "complete" : "failed")); }
		}
	}

	public MessageManager getManager(){
		return manager;
	}

	public static LoadLanguage getInstance(){
		if(Initialization.isEnable()){ return null; }
		return new LoadLanguage();
	}

}
