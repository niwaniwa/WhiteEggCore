package com.github.niwaniwa.we.core.player;

import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;

public class WhiteConsoleSender implements WhiteCommandSender {

	@Override
	public void sendMessage(String message) {
		sendMessage(message, true);
	}

	@Override
	public void sendMessage(String message, boolean replaceColorCode) {
		System.out.println(ChatColor.translateAlternateColorCodes('&', message));
	}

	@Override
	public boolean hasPermission(String permission) {
		return true;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return true;
	}

}
