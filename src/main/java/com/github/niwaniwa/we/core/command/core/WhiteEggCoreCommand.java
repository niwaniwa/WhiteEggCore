package com.github.niwaniwa.we.core.command.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.abstracts.AbstractWhiteEggCoreCommand;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.util.Versioning;

/**
 * Coreコマンドクラス
 * @author niwaniwa
 *
 */
public class WhiteEggCoreCommand extends AbstractWhiteEggCoreCommand implements TabCompleter {

	private final String permission = commandPermission + ".whiteegg";

	public WhiteEggCoreCommand() {
		WhiteEggCore.getInstance().getCommand("whiteeggcore").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0){
			this.sendVersion(sender);
			return true;
		}
		if(args[0].equalsIgnoreCase("reload")){
			return new WhiteEggReloadCommand().onCommand(sender, cmd, label, args);
		}
		return true;
	}

	private void sendVersion(WhiteCommandSender sender){
		sender.sendMessage("&7 ----- &6WhiteEggCore &7-----");
		sender.sendMessage("&7 : &6Version &7: &r" + WhiteEggCore.getInstance().getDescription().getVersion());
		sender.sendMessage("&7 : &6Author &7: &rKokekoKko_");
		sender.sendMessage("&7 : &6Server Version &7: &r" + Bukkit.getVersion());
		sender.sendMessage("&7 : &6Bukkit Version &7: &r" + Bukkit.getBukkitVersion());
		sender.sendMessage("&7 : &6CraftBukkit Version &7: &r" + Versioning.getInstance().getCraftBukkitVersion());
		sender.sendMessage("&7 ----- ----- ----- ----- -----");
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		sender.sendMessage("&7 ----- &6WhiteEggCore &7-----");
		sender.sendMessage("&6/whiteeggcore reload &f: &7Serverをリロードします。");
		sender.sendMessage("&6/whiteeggcore lock &f: &7プラグインをロックします。");
		sender.sendMessage("&6/whiteeggcore tweet &f: &7サーバの投稿としてツイートします");
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
