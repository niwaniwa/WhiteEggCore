package com.github.niwaniwa.we.core.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public class OfflineWhitePlayerImpl implements OfflineWhitePlayer, ConfigurationSerializable {

	private OfflinePlayer player;

	public OfflineWhitePlayerImpl(OfflinePlayer off) {
		this.player = off;
	}

	@Override
	public String getName() {
		return player.getName();
	}

	@Override
	public Player getPlayer() {
		return (Player) player;
	}

	@Override
	public UUID getUniqueId() {
		return player.getUniqueId();
	}

	@Override
	public boolean isOnline() {
		return player.isOnline();
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<>();
		return result;
	}

}
