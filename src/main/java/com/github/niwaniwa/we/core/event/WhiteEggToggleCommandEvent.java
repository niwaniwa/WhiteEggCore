package com.github.niwaniwa.we.core.event;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;

public class WhiteEggToggleCommandEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private Player player;
	private String togglekey;
	private Object toggleValue;
	private boolean cancelled = false;

	public WhiteEggToggleCommandEvent(CommandSender sender, String togglekey, Object toggleValue) {
		this.player = (Player) sender;
		this.togglekey = togglekey;
		this.toggleValue = toggleValue;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Player getPlayer(){
		return player;
	}

	public String getKey(){
		return togglekey;
	}

	public Object getValue(){
		return toggleValue;
	}

	public boolean setKey(String key){
		if(ToggleSettings.getToggleSettings().containsKey(key)){
			this.togglekey = key;
			return true;
		}
		return false;
	}

	public boolean setValue(Object value){
		this.toggleValue = value;
		return true;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

}
