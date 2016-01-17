package com.github.niwaniwa.we.core.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreLowCommandExecutor;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.commad.WhiteConsoleSender;

/**
 * CommandHandler
 * @author niwaniwa
 *
 */
public class WhiteEggCoreCommandHandler {

	private static final Map<String, WhiteEggCoreLowCommandExecutor> commands = new HashMap<>();

	private static final String msgPrefix = "§7[§bWEC§7]§r";
	private static final String error_Console = "whiteegg.command.console";

	/**
	 * コマンドの登録
	 * @param command コマンドの登録名
	 * @param instance 呼び出すインスタンス
	 * @return true
	 */
	public boolean registerCommand(String command, WhiteEggCoreLowCommandExecutor instance){
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
	 * コマンドの呼び出し
	 * @param sender プレイヤー
	 * @param cmd コマンド
	 * @param label ラベル
	 * @param args 引数
	 * @return 成功したか
	 */
	public static boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args){
		if(WhiteEggCore.getConf().isLock()){
			sender.sendMessage(msgPrefix + "&cプラグインはロックされています");
			return true;
		}
		for(String key : commands.keySet()){
			if(key.equalsIgnoreCase(cmd.getName())){
				WhiteEggCoreLowCommandExecutor instance = commands.get(key);
				if(isConsoleCancel(instance)){
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

	/**
	 * 登録されているコマンドを返す
	 * @return Map コマンド
	 */
	public static Map<String, WhiteEggCoreLowCommandExecutor> getCommans(){
		return commands;
	}

	/**
	 * プレイヤーにコマンド一覧を返します
	 * @param sender プレイヤー
	 */
	private static void sendCommands(WhiteCommandSender sender){
		sender.sendMessage("&7----- &bWhiteEggCore &7-----");
//		for(String command : commands.keySet()){
//			sender.sendMessage("&6/" + command + " &f:" + commands.get(command).description(sender));
//		}
	}

	public static boolean isConsoleCancel(final WhiteEggCoreLowCommandExecutor command){
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
}
