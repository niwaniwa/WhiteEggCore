package com.github.niwaniwa.we.core.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class OfflineWhitePlayerImpl implements OfflineWhitePlayer {

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
	public boolean isBanned() {
		return player.isBanned();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setBanned(boolean paramBoolean) {
		player.setBanned(paramBoolean);
	}

	@Override
	public boolean isWhitelisted() {
		return player.isWhitelisted();
	}

	@Override
	public void setWhitelisted(boolean paramBoolean) {
		player.setWhitelisted(paramBoolean);
	}

	@Override
	public long getFirstPlayed() {
		return player.getFirstPlayed();
	}

	@Override
	public long getLastPlayed() {
		return player.getLastPlayed();
	}

	@Override
	public boolean hasPlayedBefore() {
		return player.hasPlayedBefore();
	}

	@Override
	public Location getBedSpawnLocation() {
		return player.getBedSpawnLocation();
	}

	@Override
	public boolean isOp() {
		return player.isOp();
	}

	@Override
	public void setOp(boolean paramBoolean) {
		player.setOp(paramBoolean);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<>();
		return result;
	}

}
