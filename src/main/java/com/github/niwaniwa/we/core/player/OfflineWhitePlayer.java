package com.github.niwaniwa.we.core.player;

import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public interface OfflineWhitePlayer extends ConfigurationSerializable {

	public abstract String getName();

	public abstract Player getPlayer();

	public abstract UUID getUniqueId();

	public abstract boolean isOnline();

}
