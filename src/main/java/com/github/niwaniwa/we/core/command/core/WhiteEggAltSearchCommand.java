package com.github.niwaniwa.we.core.command.core;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreChildCommandExecutor;
import com.github.niwaniwa.we.core.json.JsonManager;
import com.github.niwaniwa.we.core.player.AltAccount;
import com.github.niwaniwa.we.core.player.WhiteEggPlayer;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.google.gson.JsonObject;

public class WhiteEggAltSearchCommand extends WhiteEggCoreChildCommandExecutor {

	private final String permission = commandPermission + ".whiteegg.alt";
	private final String parentCommand = "whiteeggcore";

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(WhiteCommandSender sender, Command command, String label, String[] args) {
		WhitePlayer player = this.getPlayer(args[1]);
		String name;
		AltAccount account = null;
		if(player != null){
			if(!(player instanceof WhiteEggPlayer)){ throw new IllegalArgumentException(String.format("Class %s does not extends WhiteEggPlayer", new Object[] { player.getClass().getSimpleName() })); }
			WhiteEggPlayer eggPlayer = (WhiteEggPlayer)  player;
			account = eggPlayer.getAccounts();
			name = player.getFullName();
		} else {
			UUID uuid = null;
			if(args[1].startsWith("$")){ uuid = Bukkit.getOfflinePlayer(UUID.fromString(args[1].replace("$", ""))).getUniqueId(); }
			else {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
				if(offlinePlayer == null){
					sender.sendMessage(msg.getMessage(sender, error_Player, "", true));
					return true;
				}
				uuid = offlinePlayer.getUniqueId();
			}
			JsonObject json = new JsonManager().createJsonObject(WhitePlayerFactory.getPlayerData(uuid.toString(), WhiteEggPlayer.class));
			if(json == null){
				sender.sendMessage(msg.getMessage(sender, error_Player, "", true));
				return true;
			}
			account = AltAccount.parser(json.getAsJsonObject("player").toString());
			name = json.getAsJsonObject("player").get("name").getAsString();
		}
		if(account.get().isEmpty()){
			sender.sendMessage("none");
			return true;
		}
		int loop = 1;
		sender.sendMessage("&7 ----- &6" + name + "'s account");
		for(String uuid : account.get()){
			sender.sendMessage("&7: &b" + loop + " &7: &6" + uuid + " &7: &6" + this.getPlayerName(uuid) + " &7:");
			loop++;
		}
		return true;
	}

	public String getPlayerName(String uuid){
		WhitePlayer player = this.getPlayer("$" + uuid);
		if(player == null){ return this.getOfflinePlayerName(uuid); }
		return player.getFullName();
	}

	public String getOfflinePlayerName(String uuid){
		return Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
	}

	@Override
	public String getParentCommand() {
		return parentCommand;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public List<String> getUsing() {
		return Arrays.asList();
	}

	@Override
	public String getCommandName() {
		return "alt";
	}

	private WhitePlayer getPlayer(String name){
		WhitePlayer player = null;
		if(name.startsWith("$")){
			UUID uuid = UUID.fromString(name.replace("$", ""));
			player = WhiteEggAPI.getPlayer(uuid);
			return player;
		}
		player = WhiteEggAPI.getPlayer(name);
		return player;
	}

}
