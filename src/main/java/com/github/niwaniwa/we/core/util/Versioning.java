package com.github.niwaniwa.we.core.util;

import org.bukkit.Bukkit;

public class Versioning {

    public static final String JAVA_SUPPORT_VERSION = "1.8";
    public static final String SEVER_SUPPORT_VERSION = "v1_8_R";

    private static Versioning instance = new Versioning();

    private boolean support = false;
    private String craftBukkitVersion;
    private double javaVersion;

    private Versioning() {
        this.support = versionCheck();
        instance = this;
    }

    private boolean versionCheck() {
        String tempJavaVersion = System.getProperty("java.version").split("_")[0];
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        craftBukkitVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
        if (tempJavaVersion.lastIndexOf(".") == -1) {
            this.javaVersion = 0;
            return false;
        }
        this.javaVersion = Double.valueOf(tempJavaVersion.substring(0, tempJavaVersion.lastIndexOf(".")));
        // TODO: CraftBukkit
        if (!craftBukkitVersion.equalsIgnoreCase("v1_8_R3")
                || javaVersion <= 1.7) {
            return false;
        }
        return true;
    }

    public static Versioning getInstance() {
        return instance;
    }

    public boolean isSupport() {
        return support;
    }

    public String getCraftBukkitVersion() {
        return craftBukkitVersion;
    }

    public double getJavaVersion() {
        return javaVersion;
    }


}
