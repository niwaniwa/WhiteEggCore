package com.github.niwaniwa.we.core.command.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreChildCommandExecutor;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreLowCommandExecutor;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.util.Versioning;

/**
 * Coreコマンドクラス
 * @author niwaniwa
 *
 */
public class WhiteEggCoreCommand extends WhiteEggCoreLowCommandExecutor implements TabCompleter {

	private final String permission = commandPermission + ".whiteegg";

	private final Map<String, WhiteEggCoreChildCommandExecutor> commands = new HashMap<>();

	public WhiteEggCoreCommand() {
		WhiteEggCore.getInstance().getCommand("whiteeggcore").setTabCompleter(this);
		this.register();
	}

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0){
			this.sendVersion(sender);
			return true;
		}
		WhiteEggCoreChildCommandExecutor commandObject = commands.get(args[0]);
		if(commandObject != null){ return commandObject.onCommand(sender, command, label, args); }
		sendUsing(sender);
		return true;
	}

	private void register(){
		commands.put("reload", new WhiteEggReloadCommand());
		commands.put("alt", new WhiteEggAltSearchCommand());
		commands.put("settings", new WhiteEggSettingCommand());
	}

	private void sendVersion(WhiteCommandSender sender){
		sender.sendMessage("&7 ----- - &6WhiteEggCore &7- -----");
		sender.sendMessage("&7 : &6Version &7: &r" + WhiteEggCore.getInstance().getDescription().getVersion());
		sender.sendMessage("&7 : &6Author &7: &rKokekoKko_");
		sender.sendMessage("&7 : &6Server Version &7: &r" + Bukkit.getVersion());
		sender.sendMessage("&7 : &6Bukkit Version &7: &r" + Bukkit.getBukkitVersion());
		sender.sendMessage("&7 : &6CraftBukkit Version &7: &r" + Versioning.getInstance().getCraftBukkitVersion());
		sender.sendMessage("&7 ----- ----- ----- ----- -----");
	}

	public void sendUsing(WhiteCommandSender sender) {
		sender.sendMessage("&7 ----- &6WhiteEggCore &7-----");
		sender.sendMessage("&6/whiteeggcore reload &f: &7Serverをリロードします。");
		sender.sendMessage("&6/whiteeggcore lock &f: &7プラグインをロックします。");
		sender.sendMessage("&6/whiteeggcore tweet <message> &f: &7サーバの投稿としてツイートします");
		sender.sendMessage("&6/whiteeggcore alt <name or '$' + uuid> &f: &7指定したプレイヤーの他のアカウントを調べます");
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
			tabs.add("alt");
			tabs.add("settings");
			StringUtil.copyPartialMatches(args[0], tabs, list);
		} else if(args.length >= 2){
			List<String> players = new ArrayList<>();
			if(args[1].startsWith("$")){
				Bukkit.getOnlinePlayers().forEach(p -> players.add("$" + p.getUniqueId().toString()));
			} else {
				Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
			}
			StringUtil.copyPartialMatches(args[1], players, list);
		}
		return list;
	}

	@Override
	public String getCommandName() {
		return "whiteeggcore";
	}

	@Override
	public List<String> getUsing() {
		return new ArrayList<String>(0);
	}

}
