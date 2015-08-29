package com.github.niwaniwa.we.core.command.toggle;

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
import com.github.niwaniwa.we.core.event.WhiteEggToggleCommandEvent;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.util.Util;

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
			Player target = Util.getOnlinePlayer(args[0]);
			if(target == null){
				// null message;
				return true;
			}
			this.sendInformation(WhitePlayerFactory.newInstance(target));
			return true;
		}
		List<ToggleSettings> toggle = player.getToggleSettings();
		for(ToggleSettings t : toggle){
			// permission判定して
			String[] strs = args[0].split(":");
			if(strs.length != 1){
				if(t.getToggles().containsKey(strs[1])
						&& t.getPlugin().getName().equalsIgnoreCase(strs[0])){
					WhiteEggToggleCommandEvent event = new WhiteEggToggleCommandEvent(sender, args[0], args[1]);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()){
						t.getToggles().put(event.getKey(), event.getValue());
					}
					return true;
				}
			}
			if(!t.getToggles().containsKey(args[0])){
				// null message
				continue;
			}
			WhiteEggToggleCommandEvent event = new WhiteEggToggleCommandEvent(sender, args[0], args[1]);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				t.getToggles().put(event.getKey(), event.getValue());
			}
			return true;
		}
		// not toggle key
		// message
		return true;
	}

	private void sendInformation(WhitePlayer player){
		List<ToggleSettings> toggle = player.getToggleSettings();
		if(!ToggleSettings.getPluginSettings(ToggleSettings.getList()).isEmpty()){
			player.sendMessage("§7----- §aToggle Settings(P) §7-----");
			for(ToggleSettings t : toggle){
				if(t.isHide()){ continue; }
				if(t.getType().isDefault()){ continue; }
				if(!player.hasPermission(t.getType().getPermission())){ continue; }
				player.sendMessage("§7----- §a" + t.getName() + "§7-----");
				for(String key : t.getToggles().keySet()){
					player.sendMessage(" §6" + key + " §f: §7" + t.getToggles().get(key));
				}
			}
		}
		player.sendMessage("§7----- §6Toggle Settings(V) §7-----");
		ToggleSettings t = ToggleSettings.getDefault(toggle);
		for(String key : t.getToggles().keySet()){
			player.sendMessage(" §6" + key + " §f: §7" + t.getToggles().get(key));
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
			for (ToggleSettings t : ToggleSettings.getList()) {
				if(t.isHide()){ continue; }
				if(!sender.hasPermission(t.getType().getPermission())){ continue; }
				for(String key : t.getToggles().keySet()){
					tabs.add(key);
				}
			}
			StringUtil.copyPartialMatches(args[0], tabs, list);
		}
		return list;
	}

}
