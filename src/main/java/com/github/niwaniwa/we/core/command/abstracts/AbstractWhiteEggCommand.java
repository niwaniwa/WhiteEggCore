package com.github.niwaniwa.we.core.command.abstracts;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.commad.WhiteConsoleSender;

/**
 * コマンドの抽象クラス
 * @author niwaniwa
 *
 */
public abstract class AbstractWhiteEggCommand {

	protected final String commandMessageKey = "whiteegg.command";
	protected final String error_Console = commandMessageKey + ".console";
	protected final String error_Permission = commandMessageKey + ".notpermission";
	protected final String error_Player = commandMessageKey + ".notfound";
	protected WhiteEggAPI api = WhiteEggCore.getAPI();

	/**
	 * 使い方の送信
	 * @param sender プレイヤー
	 */
	public abstract void sendUsing(final WhitePlayer sender);

	/**
	 * コマンドのpermission
	 * @return String
	 */
	public abstract String getPermission();

	/**
	 * Consoleの実行をブロックするか返す
	 * @param command 実行コマンドのインスタンス
	 * @return ブロックするか
	 */
	public static boolean isConsoleCancel(final AbstractWhiteEggCoreCommand command){
		Class<?>[] clazz = command.getClass().getInterfaces();
		if(clazz.length != 0){
			for(Class<?> s : clazz){
				if(s.equals(ConsoleCancellable.class)){ return true; }
			}
		}
		return false;
	}

	/**
	 * Consoleからの実行か判定
	 * @param sender WhiteCommandSender
	 * @return Consoleからの実行か
	 */
	public static boolean isConsole(WhiteCommandSender sender){
		return (sender instanceof WhiteConsoleSender);
	}

}
