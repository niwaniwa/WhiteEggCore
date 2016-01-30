package com.github.niwaniwa.we.core.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreBaseCommandExecutor;
import com.github.niwaniwa.we.core.player.commad.WhiteConsoleSender;

public class CommandUtil {

	private CommandUtil(){}

	public static boolean isConsoleCancel(final WhiteEggCoreBaseCommandExecutor command){
		Class<?>[] clazz = command.getClass().getInterfaces();
		if(clazz.length != 0){
			for(Class<?> s : clazz){
				if(s.equals(ConsoleCancellable.class)){ return true; }
			}
		}
		return false;
	}

	public static boolean isConsole(Object sender){
		return (!(sender instanceof Player) || sender instanceof WhiteConsoleSender);
	}

	public static void registerCommand(Plugin pluginInstance, String prefix, String commandName, List<String> aliases, String using, String description, String permission, CommandExecutor commandExecutor, TabCompleter tabInstance){
		if(pluginInstance == null ){ throw new IllegalArgumentException("instance is null."); }
		try {
			PluginCommand commandInstance = null;
			Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			constructor.setAccessible(true);
			commandInstance = constructor.newInstance(commandName, pluginInstance);
			if (commandInstance == null) { return; }
			commandInstance.setAliases((aliases == null ? new ArrayList<>() : aliases));
			commandInstance.setDescription((description == null ? new String() : description));
			commandInstance.setUsage((using == null ? new String() : using));
			commandInstance.setPermission((permission == null ? new String() : permission));
			if(commandExecutor != null){ commandInstance.setExecutor(commandExecutor); }
			if (tabInstance != null) { commandInstance.setTabCompleter(tabInstance); }
			((CraftServer) Bukkit.getServer()).getCommandMap().register(prefix, commandInstance);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
