package com.github.niwaniwa.we.core.util;

import org.bukkit.Bukkit;

public class Versioning {

	private static Versioning instance = new Versioning();

	private boolean support = false;
	private String craftBukkitVersion = "v1_8_R3";
	private int javaVersion;

	private Versioning() {
		this.support = versionCheck();
		instance = this;
	}

	private boolean versionCheck(){
		int javaVersion = Integer.valueOf(System.getProperty("java.version").split("_")[1]);
		this.javaVersion = javaVersion;
		// TODO: CraftBukkit
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		craftBukkitVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
		if(!craftBukkitVersion.equalsIgnoreCase("v1_8_R3")
				|| javaVersion <= 1.7){ return false; }
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
