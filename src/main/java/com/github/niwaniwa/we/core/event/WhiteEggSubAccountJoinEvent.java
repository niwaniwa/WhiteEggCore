package com.github.niwaniwa.we.core.event;

import com.github.niwaniwa.we.core.player.WhitePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Arrays;
import java.util.List;

public class WhiteEggSubAccountJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final WhitePlayer player;
    private final List<WhitePlayer> subAccounts;

    public WhiteEggSubAccountJoinEvent(WhitePlayer player, WhitePlayer subAccount) {
        this(player, Arrays.asList(subAccount));
    }

    public WhiteEggSubAccountJoinEvent(WhitePlayer player, List<WhitePlayer> subAccounts) {
        this.player = player;
        this.subAccounts = subAccounts;
    }

    public List<WhitePlayer> getSubAccounts() {
        return subAccounts;
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
