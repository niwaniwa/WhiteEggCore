package com.github.niwaniwa.we.core.command.toggle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.event.WhiteEggToggleCommandEvent;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.util.Other;

public class WhiteEggToggleCommand implements CommandExecutor, TabCompleter {

	public WhiteEggToggleCommand() {
		WhiteEggCore.getInstance().getCommand("toggle").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// console
			return true;
		}
		if(!sender.hasPermission(WhiteEggCore.commandPermission + ".toggle")){
			// message
			return true;
		}
		WhitePlayer player = WhitePlayerFactory.newInstance((Player) sender);
		if(args.length == 0){
			this.sendInformation(player);
			return true;
		}
		if(args.length == 1){
			Player target = Other.getOnlinePlayer(args[0]);
			if(target == null){
				// null message;
				return true;
			}
			this.sendInformation(WhitePlayerFactory.newInstance(target));
			return true;
		}
		List<String> keys = new ArrayList<String>(ToggleSettings.getToggleSettings().keySet());
		if(!keys.contains(args[0])){
			// message
			return true;
		}
		WhiteEggToggleCommandEvent event = new WhiteEggToggleCommandEvent(sender, args[0], args[1]);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			player.getToggleSettings().put(event.getKey(), event.getValue());
			// message
		}
		return true;
	}

	private void sendInformation(WhitePlayer player){
		Map<String, Object> defaultS = ToggleSettings.getToggleSettings();
		Map<String, Object> pS = player.getToggleSettings();
		player.sendMessage("§7--- §6Toggle §7---");
		for(String key : defaultS.keySet()){
			player.sendMessage(" §6" + key + " §f: §7" + (pS.get(key) == null ? defaultS.get(key) : pS.get(key)));
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		if(!sender.hasPermission(WhiteEggCore.commandPermission + "toggle")){
			return null;
		}

		List<String> list = new ArrayList<>();

		if (args.length == 1) {
			List<String> tabs = new ArrayList<>();
			for (String key : ToggleSettings.getToggleSettings().keySet()) {
				tabs.add(key);
			}
			StringUtil.copyPartialMatches(args[0], tabs, list);
		}
		return list;
	}

}
