package com.github.niwaniwa.we.core.player;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.event.WhiteEggSubAccountJoinEvent;
import com.github.niwaniwa.we.core.json.JsonManager;
import com.github.niwaniwa.we.core.util.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AltAccount implements ConfigurationSerializable {

    private final List<String> players;

    public AltAccount() {
        this.players = new ArrayList<>();
    }

    public List<String> get() {
        return players;
    }

    public boolean add(UUID player) {
        return players.add(player.toString());
    }

    public boolean contains(WhitePlayer player) {
        return players.contains(player.getUniqueId().toString());
    }

    public boolean remove(Object o) {
        return players.remove(o);
    }

    public static void determine(WhitePlayer player) {
        if (!(player instanceof WhiteEggPlayer)) {
            throw new IllegalArgumentException("Class " + player.getClass().getSimpleName() + " does not extends WhiteEggPlayer");
        }
        WhiteEggPlayer target = (WhiteEggPlayer) player;
        List<WhitePlayer> result = determine(player.getPlayer()).stream().map(accountUUID -> {
            WhitePlayer account = WhiteEggAPI.getPlayer(accountUUID);
            WhiteEggPlayer.class.cast(account).addAccount(player);
            target.addAccount(account);
            return account;
        }).collect(Collectors.toList());
        WhiteEggSubAccountJoinEvent event = new WhiteEggSubAccountJoinEvent(target, result);
        Util.callEvent(event);
    }

    public static List<UUID> determine(Player target) {
        List<UUID> result = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (getAddress(target.getAddress()).equalsIgnoreCase(getAddress(online.getAddress()))) {
                result.add(online.getUniqueId());
            }
        }
        return result;
    }

    private static String getAddress(InetSocketAddress address) {
        return address.getAddress().toString().split("/")[1].split(":")[0];
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialize = new HashMap<>();
        serialize.put("player", players);
        return null;
    }

    public static AltAccount parser(String jsonString) {
        JsonObject json = new JsonManager().createJsonObject(jsonString);
        AltAccount alt = new AltAccount();
        JsonElement obj = json.get("account");
        if (obj == null || !obj.isJsonArray()) {
            return alt;
        }
        JsonArray array = obj.getAsJsonArray();
        array.forEach(element -> alt.add(UUID.fromString(element.getAsString())));
        return alt;
    }

}
