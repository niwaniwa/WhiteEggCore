package com.github.niwaniwa.we.core.player;

import com.github.niwaniwa.we.core.api.callback.Callback;
import com.github.niwaniwa.we.core.twitter.TwitterManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.Permission;
import twitter4j.Status;
import twitter4j.StatusUpdate;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class EggPlayer implements Twitter, WhitePlayer {

    private UUID uuid;
    private Player player;

    public EggPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
    }

    public EggPlayer(UUID uuid){
        this.uuid = uuid;
        this.player = getPlayer();
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
        return Bukkit.getPlayer(uuid);
    }

    public void update(){
        this.player = getPlayer();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public boolean isOp() {
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
    public void sendMessage(String[] message) {
        Arrays.asList(message).forEach(msg -> sendMessage(msg, true));
    }

    @Override
    public void sendMessage(String message, boolean replaceColorCode) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @Override
    public TwitterManager getTwitterManager() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return player.hasPermission(permission);
    }

    @Override
    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    @Override
    public Object getHandle() {
        Object object = null;
        try {
            object = player.getClass().getMethod("getHandle").invoke(player);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return object;
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
    public void updateStatus(StatusUpdate update) {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public void updateStatus(StatusUpdate update, Callback callback) {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public void updateStatus(String tweet) {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public List<Status> getTimeLine() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

}
