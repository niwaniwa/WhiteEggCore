package com.github.niwaniwa.we.core.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.player.WhiteCommandSender;

public class WhiteEggCoreCommandHandler {

	private static Map<String, AbstractWhiteEggCoreCommand> commands = new HashMap<>();

	public WhiteEggCoreCommandHandler(){}

	public boolean registerCommand(String command, AbstractWhiteEggCoreCommand instance){
		commands.put(command, instance);
		return true;
	}

	public void unregisterCommand(String name){
		commands.remove(name);
	}

	public static boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args){
		for(String key : commands.keySet()){
			if(key.equalsIgnoreCase(cmd.getName())){
				return commands.get(key).onCommand(sender, cmd, label, args);
			}
		}
		return true;
	}

	public static Map<String, AbstractWhiteEggCoreCommand> getCommans(){
		return commands;
	}

}