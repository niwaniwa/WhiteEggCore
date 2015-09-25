package com.github.niwaniwa.we.core.player;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;

import com.github.niwaniwa.we.core.WhiteEggCore;

public class WhiteConsoleSender implements WhiteCommandSender {

	private boolean b = false;
	private Logger logger;

	public WhiteConsoleSender(boolean b) {
		this.b = b;
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
		return b;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return b;
	}

}
