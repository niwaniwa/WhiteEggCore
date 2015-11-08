package com.github.niwaniwa.we.core.player;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;

import com.github.niwaniwa.we.core.WhiteEggCore;

public class WhiteConsoleSender implements WhiteCommandSender {

	private boolean permission = false;
	private Logger logger;

	public WhiteConsoleSender(boolean permission) {
		this.permission = permission;
		this.logger = WhiteEggCore.getInstance().getLogger();
	}

	@Override
	public void sendMessage(String message) {
		sendMessage(message, true);
	}

	@Override
	public void sendMessage(String message, boolean replaceColorCode) {
		logger.info(ChatColor.stripColor(
				replaceColorCode == true ?
						ChatColor.translateAlternateColorCodes('&', message) : message));
	}

	@Override
	public boolean hasPermission(String permission) {
		return this.permission;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return this.permission;
	}

}
