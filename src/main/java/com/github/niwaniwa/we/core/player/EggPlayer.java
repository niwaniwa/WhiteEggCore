package com.github.niwaniwa.we.core.player;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.Permission;

import com.github.niwaniwa.we.core.api.callback.Callback;
import com.github.niwaniwa.we.core.twitter.TwitterManager;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public abstract class EggPlayer implements Tweet, WhitePlayer {

	private Player player;

	public EggPlayer(Player player) {
		this.player = player;
	}

	@Override
	public void remove() {
		player.remove();
	}

	@Override
	public String getName() {
		return player.getName();
	}

	@Override
	public String getFullName() {
		return getPrefix() + getName();
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public UUID getUniqueId() {
		return player.getUniqueId();
	}

	@Override
	public boolean isOp(){
		return player.isOp();
	}

	@Override
	public boolean isOnline() {
		return player.isOnline();
	}

	@Override
	public void sendMessage(String message) {
		this.sendMessage(message, true);
	}

	@Override
	public void sendMessage(String message, boolean replaceColorCode) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	@Override
	public TwitterManager getTwitterManager() {  throw new UnsupportedOperationException(getClass().getSimpleName());  }

	@Override
	public boolean hasPermission(String permission) {
		return player.hasPermission(permission);
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return player.hasPermission(permission);
	}

	public void update(){
		player = Bukkit.getPlayer(getUniqueId());
	}

	@Override
	public InetSocketAddress getAddress() {
		return player.getAddress();
	}

	@Override
	public EntityPlayer getHandle(){
		return ((CraftPlayer) player).getHandle();
	}


	@Override
	public Location getLocation() {
		return player.getLocation();
	}

	@Override
	public Inventory getInventory() {
		return player.getInventory();
	}

	@Override
	public void teleport(Location loc) {
		player.teleport(loc);
	}

	@Override
	public void teleport(Entity entity) {
		player.teleport(entity);
	}

	@Override
	public void updateStatus(StatusUpdate update) {  throw new UnsupportedOperationException(getClass().getSimpleName());  }

	@Override
	public void updateStatus(StatusUpdate update, Callback callback) {  throw new UnsupportedOperationException(getClass().getSimpleName());  }

	@Override
	public void updateStatus(String tweet) {  throw new UnsupportedOperationException(getClass().getSimpleName());  }

	@Override
	public List<Status> getTimeLine() { throw new UnsupportedOperationException(getClass().getSimpleName());  }

}
