package com.github.niwaniwa.we.core.command.toggle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreLowCommandExecutor;
import com.github.niwaniwa.we.core.event.WhiteEggToggleCommandEvent;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;

public class WhiteEggToggleCommand extends WhiteEggCoreLowCommandExecutor implements ConsoleCancellable, TabCompleter {

	private final String permission = commandPermission + ".toggle";
	private final String cmdPath = commandMessageKey + ".toggle";

	public WhiteEggToggleCommand() {
		WhiteEggCore.getInstance().getCommand("toggle").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission(permission)){
			// message
			return true;
		}
		WhitePlayer player = (WhitePlayer) sender;
		if(args.length == 0){
			this.sendInformation(player);
			return true;
		}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("using")){
				this.sendUsing(player);
				return true;
			}
			WhitePlayer target = WhiteEggAPI.getPlayer(args[0]);
			if(target != null){
				this.sendInformation(target);
				return true;
			}
			Object value = ToggleSettings.getValue(player, args[0]);
			if(value != null){
				player.sendMessage("&7----- &6" + args[0] + " &7------");
				player.sendMessage("&7: &6value &7: " + value);
				return true;
			}
			return true;
		}
		this.changeToggleSetting(player, args[0], args[1]);
		return true;
	}

	private void sendInformation(WhitePlayer player){
		List<ToggleSettings> t = player.getToggleSettings();
		List<ToggleSettings> d = ToggleSettings.getList();
		if(player.isOp()){
			player.sendMessage("&7 ----- &bServer Settings &7-----");
			for(ToggleSettings toggle : ToggleSettings.getList("SERVER", d)){
				for(String key : toggle.getToggles().keySet()){
					player.sendMessage("&7 : " + key + " &7: &6" + toggle.getToggles().get(key));
				}
			}
		}
		player.sendMessage("&7 ----- &6Settings &7-----");
		for(ToggleSettings toggle : ToggleSettings.getList("DEFAULT", d)){
			if(!player.hasPermission(toggle.getPermission())){ continue; }
			if(toggle.isHide()){ continue; }
			ToggleSettings pt = ToggleSettings.getSetting(t, toggle);
			ToggleSettings ds = ToggleSettings.getSetting(d, toggle);
			if(pt == null){ continue; }
			for(String key : toggle.getToggles().keySet()){
				player.sendMessage("&7 : " + key + " &7: &6" +
						(pt.getToggles().get(key) == null ? ds.getToggles().get(key) : pt.getToggles().get(key)));
			}
		}
	}

	private boolean changeToggleSetting(WhitePlayer player, String key, Object value){
		WhiteEggToggleCommandEvent event = new WhiteEggToggleCommandEvent(player.getPlayer(), key, value);
		Bukkit.getPluginManager().callEvent(event);
		if(!ToggleSettings.contains(event.getKey())){
			// message
			player.sendMessage(msg.getMessage(player, cmdPath + ".notkey", "", true));
			return true;
		}
		if(!event.isCancelled()){
			if(ToggleSettings.set(player, event.getKey(), event.getValue())){
				return true;
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		if(!sender.hasPermission(permission)){
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

	@Override
	public void sendUsing(WhitePlayer sender) {
		sender.sendMessage("&7 ----- &6Using &7-----");
		sender.sendMessage("&6/toggle <key> <value> &f: &7設定");
		sender.sendMessage("&6/toggle <plugin>:<key> <value> &f: &7設定");
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public String getCommandName() {
		return "toggle";
	}

	@Override
	public List<String> getUsing() {
		return new ArrayList<String>(0);
	}

}
