package com.github.niwaniwa.we.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.google.gson.JsonObject;

import net.md_5.bungee.api.ChatColor;

public class Util {

	private Util(){}

	/**
	 * 特定のJarFileから任意のファイルを任意の階層にコピーします
	 * @param target ファイルネーム
	 * @param jarFile JarFile
	 * @param path Jar内のファイル
	 */
	public static void copyFileFromJar(File target, File jarFile, String path){
		if(!jarFile.exists()){return;}
		BufferedWriter writer = null;
		BufferedReader reader = null;
		JarFile jar = null;
		try {
			if(!path.contains(".")){
				new File(target, File.separator + path).mkdirs();
				return;
			}
			jar = new JarFile(jarFile);
			JarEntry entry = jar.getJarEntry(path);
			if (entry != null) {
				reader = new BufferedReader(new InputStreamReader(jar.getInputStream(entry), "UTF-8"));
				if (!target.exists()) {
					target.mkdirs();
				}
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(new File(target, path.split("/")[path.split("/").length - 1])) ,"UTF-8"));
				String s;
				while ((s = reader.readLine()) != null) {
					writer.write(s);
					writer.newLine();
				}
			}
		} catch (IOException e) {
		} finally {
			try {
				if(writer != null){ writer.close(); }
				if(reader != null){ reader.close(); }
				if(jar != null){ jar.close(); }
			} catch (IOException e){}
		}
	}

	/**
	 * このプラグインのJarファイル内にあるリソースをコピーします
	 * @param target コピーパス
	 * @param path ファイルパス
	 */
	public static void copyFileFromJar(File target, String path){
		copyFileFromJar(WhiteEggCore.getInstance().getFile(), target, path);
	}

	/**
	 * オンラインプレイヤーを取得します
	 * @param uuid プレイヤーのUUID
	 * @return プレイヤー
	 */
	public static Player getOnlinePlayer(UUID uuid){
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getUniqueId().equals(uuid)){
				return player;
			}
		}
		return null;
	}

	/**
	 * オンラインプレイヤーを取得します
	 * @param name プレイヤーのID
	 * @return プレイヤー
	 */
	public static Player getOnlinePlayer(String name){
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getName().equals(name)){
				return player;
			}
		}
		return null;
	}

	/**
	 * 文字列からマップを返します
	 * @param str 文字列
	 * @return Map
	 */
	public static Map<String, Object> toMap(String str){
		Properties props = new Properties();
		try {
			props.load(new StringReader(str.substring(1, str.length() - 1).replace(", ", "\n")));
		} catch (IOException e1) {
		}
		Map<String, Object> map = new HashMap<>();
		props.entrySet().forEach(entry -> map.put(String.valueOf(entry.getKey()), entry.getValue()));
		return map;
	}

	/**
	 * 指定された名前のプラグインが存在するか返します
	 * @param str プラグインの文字列
	 * @return 存在するか
	 */
	public static Plugin getPlugin(String str){
		for(Plugin p : Bukkit.getPluginManager().getPlugins()){
			if(p.getName().equalsIgnoreCase(str)){
				return p;
			}
		}
		return null;
	}

	/**
	 * JsonからMapを返します
	 * @param j json
	 * @return Map
	 */
	public static Map<String, Object> toMap(JsonObject j){
		Map<String, Object> map = new HashMap<>();
		j.entrySet().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
		return map;
	}

	/**
	 * 配列を指定したところから再構成し、文字列を返します
	 * @param strings 配列
	 * @param start スタート位置
	 * @return 再構成された文字列
	 */
	public static String build(String[] strings, int start) {
		StringBuilder sb = new StringBuilder();
		if (strings.length >= start + 1) {
			sb.append(strings[start]);
			if (strings.length >= start + 2) {
				for (int i = start + 1; i < strings.length; i++) {
					sb.append(" ").append(strings[i]);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * カラーコードの置き換え
	 * @param s 文字列
	 * @return 置換された文字列
	 */
	public static String replaceColorCode(String s){
		if(s == null){ return null; }
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	/**
	 * イベントを呼び出します
	 * @param event Eventを継承したイベント
	 */
	public static void callEvent(Event event){
		Bukkit.getPluginManager().callEvent(event);
	}

}
