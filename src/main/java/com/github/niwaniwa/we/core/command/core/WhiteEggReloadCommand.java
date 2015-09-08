package com.github.niwaniwa.we.core.command.core;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.WhiteEggChildCommand;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public class WhiteEggReloadCommand extends WhiteEggChildCommand {

	private final String permission = commandPermission + ".whiteegg.reload";
	private final String parentCommand = "whiteeggcore";

	@Override
	public void runCommand(WhiteCommandSender player, Command cmd, String[] args) {
		if(!player.hasPermission(permission)){
			player.sendMessage(msg.getMessage(player, error_Permission, msgPrefix, true));
			return;
		}
		Bukkit.reload();
		if(args.length >= 2){
			if(args[1].equalsIgnoreCase("-b")){
				Bukkit.broadcastMessage(msgPrefix + "§aReload complete.");
				return;
			}
		}
		for(WhitePlayer p : WhiteEggCore.getAPI().getOnlinePlayers())
			if(p.isOp())
				p.sendMessage(msgPrefix + "&aReload complete.");
		return;
	}

	@Override
	public String getParentCommand() {
		return parentCommand;
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
	}

	@Override
	public String getPermission() {
		return permission;
	}

}
