package com.github.niwaniwa.we.core.util.hologram;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Hologram {

	private Location location;
	private String text;
	private ArmorStand armorStand;

	public Hologram(Location location, String text) {
		this.location = location;
		this.text = text;
	}

	public void spawn() {
		this.armorStand = (ArmorStand) this.location.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);
		this.armorStand.setCustomName(this.text);
		this.armorStand.setCustomNameVisible(true);
		this.armorStand.setGravity(false);
		this.armorStand.setVisible(false);
	}

	public void spawn(Player player) {
	}

	public void remove() {
		armorStand.remove();
	}

	public void move(Location location) {
		armorStand.teleport(location);
	}

}
