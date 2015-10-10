package com.github.niwaniwa.we.core.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.abstracts.AbstractWhiteEggCommand;
import com.github.niwaniwa.we.core.command.abstracts.AbstractWhiteEggCoreCommand;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;

/**
 * CommandHandler
 * @author niwaniwa
 *
 */
public class WhiteEggCoreCommandHandler {

	private static final Map<String, AbstractWhiteEggCoreCommand> commands = new HashMap<>();

	private static final String msgPrefix = "§7[§bWEC§7]§r";
	private static final String error_Console = "whiteegg.command.console";

	/**
	 * コマンドの登録
	 * @param command コマンドの登録名
	 * @param instance 呼び出すインスタンス
	 * @return
	 */
	public boolean registerCommand(String command, AbstractWhiteEggCoreCommand instance){
		commands.put(command, instance);
		return true;
	}

	/**
	 * 登録されているコマンドの削除
	 * @param name コマンドの登録名
	 */
	public void unregisterCommand(String name){
		commands.remove(name);
	}

	/**
	 * 呼び出し
	 * @param sender player
	 * @param cmd command
	 * @param label
	 * @param args
	 * @return
	 */
	public static boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args){
		if(WhiteEggCore.getConf().isLock()){
			sender.sendMessage(msgPrefix + "&cプラグインはロックされています");
			return true;
		}
		for(String key : commands.keySet()){
			if(key.equalsIgnoreCase(cmd.getName())){
				AbstractWhiteEggCoreCommand instance = commands.get(key);
				if(AbstractWhiteEggCommand.isConsoleCancel(instance)){
					if(!(sender instanceof WhitePlayer)){
						sender.sendMessage(WhiteEggCore.getMessageManager().getMessage(sender, error_Console, "", true));
						return true;
					}
				}
				return instance.onCommand(sender, cmd, label, args);
			}
		}
		sendCommands(sender);
		return true;
	}

	public static Map<String, AbstractWhiteEggCoreCommand> getCommans(){
		return commands;
	}

	private static void sendCommands(WhiteCommandSender sender){
		sender.sendMessage("&7----- &bWhiteEggCore &7-----");
		for(String command : commands.keySet()){
			sender.sendMessage("&6/" + command + " &f:" + commands.get(command).description(sender));
		}
	}
}
