package com.github.niwaniwa.we.core.command.core;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.abstracts.WhiteEggChildCommand;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.rank.Rank;

/**
 * Coreコマンドのリロードクラス
 * @author niwaniwa
 *
 */
public class WhiteEggReloadCommand extends WhiteEggChildCommand {

	private final String permission = commandPermission + ".whiteegg.reload";
	private final String parentCommand = "whiteeggcore";

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission(permission)){
			sender.sendMessage(msg.getMessage(sender, error_Permission, msgPrefix, true));
			return true;
		}
		WhitePlayerFactory.saveAll();
		WhitePlayerFactory.load();
		Rank.saveAll();
		Rank.load();
		if(args.length >= 2){
			if(args[1].equalsIgnoreCase("-b")){
				Bukkit.broadcastMessage(msgPrefix + "§aReload complete.");
				return true;
			}
		}
		for(WhitePlayer p : WhiteEggCore.getAPI().getOnlinePlayers())
			if(p.isOp())
				p.sendMessage(msgPrefix + "&aReload complete.");
		return true;
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

	@Override
	public String description() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String description(WhiteCommandSender sender) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
