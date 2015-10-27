package com.github.niwaniwa.we.core.player;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AltAccount implements ConfigurationSerializable {

	private final List<String> players;

	public AltAccount() {
		this.players = new ArrayList<>();
	}

	public List<String> get(){
		return players;
	}

	public boolean add(UUID player){
		return players.add(player.toString());
	}

	public boolean contains(WhitePlayer player){
		return players.contains(player.getUniqueId().toString());
	}

	public boolean remove(Object o){
		return players.remove(o);
	}

	public static void determine(WhitePlayer player) {
		if(!(player instanceof WhiteEggPlayer)){ return; }
		WhiteEggPlayer egg = (WhiteEggPlayer) player;
		for(WhitePlayer p : WhitePlayerFactory.getPlayers()){
			if(!(p instanceof WhiteEggPlayer)){ continue; }
			WhiteEggPlayer egg2 = (WhiteEggPlayer) p;
			if(getAddress(egg.getAddress()).equalsIgnoreCase(getAddress(egg2.getAddress()))){
				egg.addAccount(p);
				egg2.addAccount(egg);
			}
		}
	}

	private static String getAddress(InetSocketAddress address){
		String[] loginAdress = address.getAddress().toString().split("/");
		loginAdress = loginAdress[1].split(":");
		return loginAdress[0];
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

	public static AltAccount parser(JsonObject json){
		AltAccount alt = new AltAccount();
		JsonElement obj = json.get("account");
		if(obj == null
				|| !obj.isJsonArray()){ return alt; }
		JsonArray array = obj.getAsJsonArray();
		for(JsonElement o : array){
			alt.add(UUID.fromString(o.getAsString()));
		}
		return alt;
	}

}
