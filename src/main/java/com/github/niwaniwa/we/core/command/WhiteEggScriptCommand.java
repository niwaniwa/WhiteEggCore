package com.github.niwaniwa.we.core.command;

import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.abstracts.AbstractWhiteEggCoreCommand;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.script.JavaScript;

public class WhiteEggScriptCommand extends AbstractWhiteEggCoreCommand {

	private final String key = commandMessageKey + ".script";
	private final String permission = commandPermission + ".script";

//	private final String[] arg = new String[]{"reload", "off"};

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0){
			if(sender instanceof WhitePlayer){ sendUsing((WhitePlayer) sender); return true;}
			sender.sendMessage(description(sender));
			return true;
		}
		switch(args[0]){
		case "reload":
			WhiteEggCore.getInstance().setScript(JavaScript.loadScript());
			sender.sendMessage(msgPrefix + "&aReload complete.");
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	protected String description() {
		return null;
	}

	@Override
	public String description(WhiteCommandSender sender) {
		return msg.getMessage(sender, key, "", true);
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		sender.sendMessage("&7----- &6/script -----");
	}

	@Override
	public String getPermission() {
		return permission;
	}

}
