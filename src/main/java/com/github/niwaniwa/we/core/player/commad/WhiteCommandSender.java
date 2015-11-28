package com.github.niwaniwa.we.core.player.commad;

import org.bukkit.permissions.Permission;

public interface WhiteCommandSender {

	public abstract void sendMessage(String message);

	public abstract void sendMessage(String message, boolean replaceColorCode);

	public abstract boolean hasPermission(String permission);

	public abstract boolean hasPermission(Permission permission);

}
