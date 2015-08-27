package com.github.niwaniwa.we.core.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.toggle.ToggleSettings;
import com.github.niwaniwa.we.core.player.OfflineWhitePlayer;
import com.github.niwaniwa.we.core.player.OfflineWhitePlayerImpl;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.util.Other;

public class WhiteEggAPIImpl extends WhiteEggAPI {

	public WhiteEggAPIImpl() {
	}

	@Override
	public List<WhitePlayer> getOnlinePlayers() {
		List<WhitePlayer> result = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers()){
			result.add(WhitePlayerFactory.newInstance(p));
		}
		return result;
	}

	@Override
	public List<Rank> getRanks() {
		return Rank.getRanks();
	}

	@Override
	public void RankBroadcastMessage(Rank rank, String message) {
		for(WhitePlayer p :  WhiteEggCore.getAPI().getOnlinePlayers()){
			if(p.getRanks().contains(rank)){
				p.sendMessage(message);
			}
		}
	}

	@Override
	public void WorldBroadcastMessage(World world, String message) {
		for(Player p : world.getPlayers()){
			p.sendMessage(message);
		}
	}

	@Override
	public OfflineWhitePlayer getOfflinePlayer(String name) {
		@SuppressWarnings("deprecation")
		OfflinePlayer offline = Bukkit.getOfflinePlayer(name);
		return new OfflineWhitePlayerImpl(offline);
	}

	@Override
	public WhitePlayer getPlayer(String name) {
		Player player = Other.getOnlinePlayer(name);
		if(player == null){
			return null;
		}
		return WhitePlayerFactory.newInstance(player);
	}

	@Override
	public WhitePlayer getPlayer(UUID uuid) {
		Player player = Other.getOnlinePlayer(uuid);
		if(player == null){
			WhitePlayer off = (WhitePlayer) Bukkit.getOfflinePlayer(uuid);
			return off;
		}
		return WhitePlayerFactory.newInstance(player);
	}

	@Override
	public WhitePlayer getPlayer(Player player) {
		return WhitePlayerFactory.newInstance(player);
	}

	@Override
	public boolean registerToggleType(String key, Object value) {
		Map<String, Object> map = new HashMap<>();
		map.put(key, value);
		return this.registerToggleType(map);
	}

	@Override
	public boolean registerToggleType(Map<String, Object> map){
		ToggleSettings toggle = new ToggleSettings();
		toggle.addToggleSetting(map);
		return true;
	}

	@Override
	public boolean useDataBase() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}


}
