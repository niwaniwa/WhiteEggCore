package com.github.niwaniwa.we.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.niwaniwa.we.core.player.WhitePlayer;

public class WhiteEggSubAccountJoinEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final WhitePlayer player;
	private final WhitePlayer subAccount;

	public WhiteEggSubAccountJoinEvent(WhitePlayer player, WhitePlayer subAccount) {
		this.player = player;
		this.subAccount = subAccount;
	}

	public WhitePlayer getSubAccount() {
		return subAccount;
	}

	public WhitePlayer getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
