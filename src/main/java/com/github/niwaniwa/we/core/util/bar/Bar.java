package com.github.niwaniwa.we.core.util.bar;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public class Bar {

	public Bar(){
	}

	public static void setDragon(WhitePlayer player, String message){
		setDragon(player, message, 300);
	}

	public static void setDragon(WhitePlayer player, String message, int seconds) {
		if(seconds <= 1){ return; }
		Dragon dragon = Dragon.newInstance(player, getLocation(player), message);
		dragon.setName(message);
		dragon.setHealth(dragon.getMaxHealth());
		float minus = dragon.getMaxHealth() / seconds;
		if(dragon.getScheduler() != null){
			dragon.getScheduler().cancel();
		}
		dragon.sendSpawnPacket();
		dragon.setScheduler(new BukkitRunnable() {
			@Override
			public void run() {
				dragon.setHealth(dragon.getHealth() - minus);
				if(dragon.getHealth() <= 0){
					dragon.sendDestroyPacket(true);
				} else {
					dragon.sendMetadata();
					player.getHandle().playerConnection.sendPacket(dragon.getTeleportPacket(getLocation(player)));
				}
			}
		}.runTaskTimer(WhiteEggCore.getInstance(), 20, 20));
	}

	private static Location getLocation(WhitePlayer player){
		@SuppressWarnings("deprecation")
		Block block = player.getPlayer().getTargetBlock((HashSet<Byte>) null, 100);
		if(!block.getType().equals(Material.AIR)){
			return block.getLocation().add(0, 100, 0);
		}
		return block.getLocation();
	}

}
