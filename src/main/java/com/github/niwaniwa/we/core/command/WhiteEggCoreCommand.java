package com.github.niwaniwa.we.core.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public class WhiteEggCoreCommand extends AbstractWhiteEggCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0){
			this.sendVersion(sender);
			return true;
		}
		return true;
	}

	private void sendVersion(CommandSender sender){
		sender.sendMessage("§7 ----- §aWhiteEggCore §7-----");
		sender.sendMessage(" : Version : " + WhiteEggCore.getInstance().getDescription().getVersion());
		sender.sendMessage(" : Use Library : " + "1.8.7-R0.1 Spigot");
		sender.sendMessage(" : Author : " + WhiteEggCore.getInstance().getDescription().getAuthors().get(0));
		sender.sendMessage(" : Running Minecraft Version : " + Bukkit.getVersion());
		sender.sendMessage("§7 ----- ----- ----- ----- -----");
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public String getPermission() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
