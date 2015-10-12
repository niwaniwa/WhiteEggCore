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

import net.md_5.bungee.api.ChatColor;
import net.sf.json.JSONObject;

public class Util {

	private Util(){}

	public static void copyFileFromJar(File target, File jarFile, String path){
		if(!jarFile.exists()){return;}
		BufferedWriter writer = null;
		BufferedReader reader = null;
		JarFile jar = null;
		try {
			if(!path.contains(".")){
				new File(target, "/"+path).mkdirs();
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
						new FileOutputStream(new File(target, path.split("/")[path.split("/").length - 1]))));
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

	public static Player getOnlinePlayer(UUID uuid){
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getUniqueId().equals(uuid)){
				return player;
			}
		}
		return null;
	}

	public static Player getOnlinePlayer(String name){
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getName().equals(name)){
				return player;
			}
		}
		return null;
	}

	/**
	 * 要学習
	 * @param str
	 * @return Map
	 * @throws IOException
	**/
	public static Map<String, Object> toMap(String str) throws IOException{
		Properties props = new Properties();
		props.load(new StringReader(str.substring(1, str.length() - 1).replace(", ", "\n")));
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<Object, Object> e : props.entrySet()) {
			map.put((String)e.getKey(), e.getValue());
		}
		return map;
	}

	public static Plugin getPlugin(String str){
		for(Plugin p : Bukkit.getPluginManager().getPlugins()){
			if(p.getName().equalsIgnoreCase(str)){
				return p;
			}
		}
		return null;
	}

	public static Map<String, Object> toMap(JSONObject j){
		Map<String, Object> map = new HashMap<>();
		for(Object o : j.keySet()){
			map.put(String.valueOf(o), j.get(o));
		}
		return map;
	}

	public static String build(String[] strings, int start) {
		if (strings.length >= start + 1) {
			String str = strings[start];
			if (strings.length >= start + 2) {
				for (int i = start + 1; i < strings.length; i++) {
					str = str + " " + strings[i];
				}
			}
			return str;
		}
		return null;
	}

	public static String replaceColorCode(String s){
		if(s == null){ return null; }
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static void callEvent(Event event){
		Bukkit.getPluginManager().callEvent(event);
	}

}
