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
import com.github.niwaniwa.we.core.player.OfflineWhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.rank.Rank;
import com.github.niwaniwa.we.core.twitter.ServerTwitterManager;
import com.github.niwaniwa.we.core.util.Util;

/**
 * WhiteEggAPIの実装クラス
 * @author niwaniwa
 *
 */
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
		WhiteEggCore.getAPI().getOnlinePlayers().stream()
						.filter(p -> p.getRanks().contains(rank))
						.forEach(p -> p.sendMessage(message));
	}

	@Override
	public void WorldBroadcastMessage(World world, String message) {
		world.getPlayers().forEach(p -> p.sendMessage(message));
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
	public boolean registerToggle(Plugin plugin, String tag, String permission, String custam,
			Map<String, Object> toggles, boolean isHide) {
		return false;
	}

	@Override
	public boolean useDataBase() {
		return false;
	}

	@Override
	public void tweet(String tweet) {
		ServerTwitterManager.getInstance().tweet(tweet);
	}


}
