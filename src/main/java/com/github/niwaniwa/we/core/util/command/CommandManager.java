package com.github.niwaniwa.we.core.util.command;

import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.WhiteBaseCommandExeutor;
import com.github.niwaniwa.we.core.player.commad.WhiteConsoleSender;

public class CommandManager {

	private CommandManager() {  }

	/**
	 * consoleをキャンセルするクラスか判定
	 * @param command 実行クラス
	 * @return boolean キャンセルする場合true、しない場合はfalse
	 */
	public static boolean isConsoleCancel(final WhiteBaseCommandExeutor command){
		Class<?>[] clazz = command.getClass().getInterfaces();
		if(clazz.length != 0){
			for(Class<?> s : clazz){
				if(s.equals(ConsoleCancellable.class)){ return true; }
			}
		}
		return false;
	}

	/**
	 * 実行者がconsoleか判定
	 * @param sender 実行者
	 * @return  boolean consoleの場合true、他はfalse
	 */
	public static boolean isConsole(Object sender){
		return (!(sender instanceof Player) || sender instanceof WhiteConsoleSender);
	}

}
