package com.github.niwaniwa.we.core.util;

import org.bukkit.Bukkit;

public class Versioning {

	private static Versioning instance = new Versioning();

	private boolean support = false;
	private String craftBukkitVersion = "v1_8_R3";
	private int javaVersion;

	private Versioning() {
		versionCheck();
		instance = this;
	}

	private boolean versionCheck(){
		int javaVersion = Integer.valueOf(System.getProperty("java.version").split("_")[1]);
		this.javaVersion = javaVersion;
		if(javaVersion <= 1.7){
			return false;
		}
		// TODO: CraftBukkit
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);
		if(!version.equalsIgnoreCase("v1_8_R3")){
			craftBukkitVersion = version;
			return false;
		}
		this.support = true;
		return true;
	}

	public static Versioning getInstance(){
		return instance;
	}

	public boolean isSupport() {
		return support;
	}

	public String getCraftBukkitVersion() {
		return craftBukkitVersion;
	}

	public int getJavaVersion() {
		return javaVersion;
	}


}
