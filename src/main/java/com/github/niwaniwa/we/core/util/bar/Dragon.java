package com.github.niwaniwa.we.core.util.bar;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.scheduler.BukkitTask;

import com.github.niwaniwa.we.core.player.WhitePlayer;

import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

/**
 * 1.8
 * @author niwaniwa
 *
 */
public class Dragon {

	private static Map<WhitePlayer, Dragon> dragons = new HashMap<>();

	private WhitePlayer player;

	private EntityEnderDragon dragon;

	private float maxHealth = 200;
	private float health = 0;
	private Location location;
	private CraftWorld world;
	private String name;

	private double x;
	private double y;
	private double z;

	private int id;

	private boolean visible = false;

	private BukkitTask scheduler;

	private Dragon(WhitePlayer white, Location loc, String name) {
		this.player = white;
		this.location = loc;
		this.world = (CraftWorld) loc.getWorld();
		this.name = name;
		dragons.put(white, this);
	}

	public WhitePlayer getPlayer() {
		return player;
	}

	public String getName(){
		return name;
	}

	public EntityEnderDragon getDragon(){
		return dragon;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public Location getLocation() {
		return location;
	}

	public World getWorld() {
		return world;
	}

	public float getHealth() {
		return health;
	}

	public BukkitTask getScheduler() {
		return scheduler;
	}

	public void setPlayer(WhitePlayer player) {
		this.player = player;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setMaxHealth(float maxHealt) {
		this.maxHealth = maxHealt;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setWorld(World world){
		this.world = (CraftWorld) world;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setScheduler(BukkitTask scheduler) {
		this.scheduler = scheduler;
	}

	public void sendSpawnPacket(){
		Packet<?> packet;
		dragon = new EntityEnderDragon(world.getHandle());
		dragon.setLocation(
				location.getBlockX(), location.getBlockY(), location.getBlockZ(),
				location.getYaw(), location.getPitch());
		dragon.setCustomName(name);
		dragon.setHealth(health);
		dragon.motX = x;
		dragon.motY = y;
		dragon.motZ = z;
		this.id = dragon.getId();
		packet = new PacketPlayOutSpawnEntityLiving(dragon);
		player.getHandle().playerConnection.sendPacket(packet);
	}

	public void sendDestroyPacket(boolean cancel) {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy();
		try {
			Field a = packet.getClass().getDeclaredField("a");
			a.setAccessible(true);
			a.set(packet, new int[]{ id });
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
		}
		player.getHandle().playerConnection.sendPacket(packet);
		if(cancel){
			remove();
		}
	}

	public DataWatcher getDataWatcher(){
		DataWatcher watcher = new DataWatcher(dragon);
		watcher.a(0, (Byte) (byte) 0);
		watcher.a(1, (Short) (short) 0);
		watcher.a(5, isVisible() ? (byte) 0 : (byte) 0x20);
		watcher.a(6, (Float) health);
		watcher.a(7, (Integer) 0);
		watcher.a(8, (Byte) (byte) 0);
		// String passed into the rename function
		watcher.a(10 , name);
		// 11 is "always show nameplate" according to the Protocol website
		watcher.a(11, (byte) 0);
		return watcher;
	}

	public void sendMetadata(){
		PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(id, getDataWatcher(), true);
		player.getHandle().playerConnection.sendPacket(packet);
	}

	public PacketPlayOutEntityTeleport getTeleportPacket(Location location){
		Location loc = new Location(location.getWorld(), (double) location.getBlockX() * 32,(double) location.getBlockY() * 32, (double) location.getBlockZ() * 32,
				(float) ((int) location.getYaw() * 256 / 360), (float) ((int) location.getPitch() * 256 / 360));
		this.location = loc;
		return new PacketPlayOutEntityTeleport(
				this.id,
				location.getBlockX() * 32, location.getBlockY() * 32, location.getBlockZ() * 32,
				(byte) ((int) location.getYaw() * 256 / 360), (byte) ((int) location.getPitch() * 256 / 360), false);
	}

	public void remove(){
		dragons.remove(player);
		if(getScheduler() != null){ getScheduler().cancel(); }
	}

	public static Dragon newInstance(WhitePlayer player, Location loc, String name){
		Dragon dragon = dragons.get(player);
		return dragon != null ? dragon : new Dragon(player, loc, name);
	}

	public static boolean hasDragon(WhitePlayer player){
		return dragons.containsKey(player);
	}

	public static void disable(){
		for(Dragon d : dragons.values()){
			if(d.getScheduler() != null){
				if(d.getPlayer().isOnline()){
					d.sendDestroyPacket(true);
				}
			}
		}
	}


}