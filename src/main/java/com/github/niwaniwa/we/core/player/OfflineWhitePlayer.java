package com.github.niwaniwa.we.core.player;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface OfflineWhitePlayer extends OfflinePlayer {

	public abstract String getName();

	public abstract Player getPlayer();

	public abstract UUID getUniqueId();

	public abstract boolean isOnline();

}
