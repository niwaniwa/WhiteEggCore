package com.github.niwaniwa.we.core.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public class WhiteEggCoreCommand extends AbstractWhiteEggCommand implements CommandExecutor, TabCompleter {

	private final String permission = commandPermission + ".whiteegg";

	public WhiteEggCoreCommand() {
		WhiteEggCore.getInstance().getCommand("whiteeggcore").setTabCompleter(this);
	}

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
		sender.sendMessage(" : Use Library : " + "1.8.8-R0.1 Spigot");
		sender.sendMessage(" : Author : KokekoKko_");
		sender.sendMessage(" : Running CraftBukkit Version : " + Bukkit.getVersion());
		sender.sendMessage("§7 ----- ----- ----- ----- -----");
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		sender.sendMessage("&7 ----- &6WhiteEggCore &7-----");
		sender.sendMessage("&6/whiteeggcore reload &f: &7Serverをリロードします。");
		sender.sendMessage("&6/whiteeggcore lock &f: &7プラグインをロックします。");
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission(permission)){
			return null;
		}
		List<String> list = new ArrayList<>();

		if (args.length == 1) {
			List<String> tabs = new ArrayList<>();
			tabs.add("reload");
			tabs.add("lock");
			StringUtil.copyPartialMatches(args[0], tabs, list);
		}
		return list;
	}

}
