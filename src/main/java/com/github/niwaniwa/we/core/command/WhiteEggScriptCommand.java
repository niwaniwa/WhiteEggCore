package com.github.niwaniwa.we.core.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreLowCommandExecutor;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.script.JavaScript;

public class WhiteEggScriptCommand extends WhiteEggCoreLowCommandExecutor {

	private final String key = commandMessageKey + ".script";
	private final String permission = commandPermission + ".script";

//	private final String[] arg = new String[]{"reload", "off"};

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0){
			if(sender instanceof WhitePlayer){ sendUsing((WhitePlayer) sender); return true;}
			sender.sendMessage(getUsing().toArray(new String[0]));
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
	public void sendUsing(WhitePlayer sender) {
		sender.sendMessage("&7----- &6/script &7-----");
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public String getCommandName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<String> getUsing() {
		return new ArrayList<String>(0);
	}

}
