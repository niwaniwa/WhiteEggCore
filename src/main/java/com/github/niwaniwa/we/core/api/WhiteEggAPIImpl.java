package com.github.niwaniwa.we.core.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.toggle.type.ToggleType;
import com.github.niwaniwa.we.core.player.OfflineWhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.util.Util;

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
		return WhitePlayerFactory.newInstance((Player) offline);
	}

	@Override
	public WhitePlayer getPlayer(String name) {
		Player player = Util.getOnlinePlayer(name);
		if(player == null){
			return null;
		}
		return WhitePlayerFactory.newInstance(player);
	}

	@Override
	public WhitePlayer getPlayer(UUID uuid) {
		Player player = Util.getOnlinePlayer(uuid);
		if(player == null){
			WhitePlayer white = WhitePlayerFactory.newInstance((Player) Bukkit.getPlayer(uuid));
			return white;
		}
		return WhitePlayerFactory.newInstance(player);
	}

	@Override
	public WhitePlayer getPlayer(Player player) {
		return WhitePlayerFactory.newInstance(player);
	}

	@Deprecated
	@Override
	public boolean registerToggle(Plugin plugin, ToggleType type, String permission, String custam,
			Map<String, Object> toggles, boolean isHide) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean useDataBase() {
		return false;
	}


}
