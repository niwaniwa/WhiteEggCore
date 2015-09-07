package com.github.niwaniwa.we.core.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.util.message.MessageManager;

public abstract class AbstractWhiteEggCommand {

	public final String commandPermission = "whiteegg.core.command";
	public final String msgPrefix = "§7[§bWEC§7]§r";
	public final String logPrefix = "[WEC]";
	public final String commandMessageKey = "whiteegg.command";
	public final String error_Console = commandMessageKey + ".console";
	public final String error_Permission = commandMessageKey + ".notpermission";

	protected MessageManager msg = WhiteEggCore.getMessageManager();

	public abstract void sendUsing(WhitePlayer sender);

	public abstract String getPermission();

	protected WhitePlayer get(CommandSender sender){
		return WhitePlayerFactory.newInstance((Player) sender);
	}

}
