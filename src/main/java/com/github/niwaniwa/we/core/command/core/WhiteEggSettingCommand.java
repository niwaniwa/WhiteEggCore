package com.github.niwaniwa.we.core.command.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreChildCommandExecutor;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;

public class WhiteEggSettingCommand extends WhiteEggCoreChildCommandExecutor {

	private final String permission = commandPermission + ".whiteegg.setting";
	private final String parentCommand = "whiteeggcore";

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("&7 ----- &6Settings &7-----");
		sender.sendMessage("&7 : &6|  key  | &7: &6| value | &7:");
		for(Entry<String, Object> entry : WhiteEggCore.getConf().getConfig().getValues(false).entrySet()){
			sender.sendMessage("&7: &6" + entry.getKey() + " &7: &6" + entry.getValue() + " &7:");
		}
		return true;
	}

	@Override
	public String getParentCommand() {
		return parentCommand;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public List<String> getUsing() {
		return Arrays.asList();
	}

	@Override
	public String getCommandName() {
		return "settings";
	}

}
