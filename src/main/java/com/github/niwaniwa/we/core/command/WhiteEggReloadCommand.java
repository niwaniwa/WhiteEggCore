package com.github.niwaniwa.we.core.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public class WhiteEggReloadCommand extends AbstractWhiteEggCommand implements CommandExecutor, TabCompleter {

	private final String permission = commandPermission + ".reload";

	public WhiteEggReloadCommand() {
		WhiteEggCore.getInstance().getCommand("reload").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// reload message
		if(!sender.hasPermission(permission)){
			// msg
			return true;
		}
		Bukkit.reload();
		this.send((args.length == 0 ? false : Boolean.parseBoolean(args[0])));
		return true;
	}

	private void send(boolean b){
		if(b){
			Bukkit.broadcastMessage(msgPrefix + "Reload complete.");
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.isOp()){
				p.sendMessage(msgPrefix + "Reload complete.");
			}
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission(permission)){
			return null;
		}
		List<String> list = new ArrayList<>();

		if (args.length == 1) {
			List<String> tabs = new ArrayList<>();
			tabs.add("false");
			tabs.add("true");
			StringUtil.copyPartialMatches(args[0], tabs, list);
		}
		return list;
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public String getPermission() {
		return permission;
	}

}
