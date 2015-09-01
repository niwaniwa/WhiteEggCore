package com.github.niwaniwa.we.core.command.toggle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.util.Util;

public class WhiteEggToggleCommand implements CommandExecutor, TabCompleter {

	public WhiteEggToggleCommand() {
		WhiteEggCore.getInstance().getCommand("toggle").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Date da = new Date();
		if(!(sender instanceof Player)){
			// console
			return true;
		}
		System.out.println(da.getTime());
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
			if(args[0].equalsIgnoreCase("using")){
				player.sendMessage("&7 ----- &6Using &7-----");
				player.sendMessage("&6/toggle <key> <value> &f: &7設定");
				player.sendMessage("&6/toggle <plugin>:<key> <value> &f: &7設定");
				return true;
			}
			Player target = Util.getOnlinePlayer(args[0]);
			if(target == null){
				// null message;
				return true;
			}
			WhitePlayer white = WhitePlayerFactory.newInstance(target);
			this.sendInformation(white);
			return true;
		}

		return true;
	}

	private void sendInformation(WhitePlayer player){
		List<ToggleSettings> t = player.getToggleSettings();
		List<ToggleSettings> d = ToggleSettings.getList();
		player.sendMessage("&7 ----- &6Settings &7-----");
		if(player.isOp()){
			player.sendMessage("&7 ----- &bServer Settings -----");
			for(ToggleSettings toggle : ToggleSettings.getServerSetting(d)){
				for(String key : toggle.getToggles().keySet()){
					player.sendMessage("&7 : &b" + key + " &7: &6" + toggle.getToggles().get(key));
				}
			}
		}
		if(player.hasPermission("whiteegg.moderator")){
			player.sendMessage(" &7----- &Moderator Settings -----");
			for(ToggleSettings toggle : ToggleSettings.getModeratorSetting(d)){
				if(!player.hasPermission(toggle.getPermission())){ continue; }
				ToggleSettings pt = ToggleSettings.getSetting(t, toggle);
				ToggleSettings ds = ToggleSettings.getSetting(d, toggle);
				if(pt == null){ continue; }
				for(String key : toggle.getToggles().keySet()){
					player.sendMessage("&7 : &b" + key + " &7: &6" +
							(pt.getToggles().get(key) == null ? ds.getToggles().get(key) : pt.getToggles().get(key)));
				}
			}
		}
		player.sendMessage("&7 ----- &6Settings &7-----");
		for(ToggleSettings toggle : ToggleSettings.getDefaltSetting(d)){
			if(!player.hasPermission(toggle.getPermission())){ continue; }
			if(toggle.isHide()){ continue; }
			ToggleSettings pt = ToggleSettings.getSetting(t, toggle);
			ToggleSettings ds = ToggleSettings.getSetting(d, toggle);
			if(pt == null){ continue; }
			for(String key : toggle.getToggles().keySet()){
				player.sendMessage("&7 : &b" + key + " &7: &6" +
						(pt.getToggles().get(key) == null ? ds.getToggles().get(key) : pt.getToggles().get(key)));
			}
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
				if(!sender.hasPermission(t.getPermission())){ continue; }
				for(String key : t.getToggles().keySet()){
					tabs.add(key);
				}
			}
			StringUtil.copyPartialMatches(args[0], tabs, list);
		}
		return list;
	}

}
