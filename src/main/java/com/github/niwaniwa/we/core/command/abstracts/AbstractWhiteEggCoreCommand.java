package com.github.niwaniwa.we.core.command.abstracts;

import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.util.message.MessageManager;

/**
 * Coreコマンドの抽象クラス
 * @author niwaniwa
 *
 */
public abstract class AbstractWhiteEggCoreCommand extends AbstractWhiteEggCommand {

	protected final String commandPermission = "whiteegg.core.command";
	protected final String msgPrefix = WhiteEggCore.msgPrefix;
	protected final String logPrefix = "[WEC]";
	protected MessageManager msg = WhiteEggCore.getMessageManager();

	/**
	 * コマンドの実行
	 * @param sender sender
	 * @param cmd command
	 * @param label label
	 * @param args args
	 * @return boolean
	 */
	public abstract boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args);

	/**
	 * コマンドの説明
	 * @return 説明文
	 * @deprecated 正常な動作をしない場合があるので使用しないでください {@link #description(WhiteCommandSender)}
	 */
	protected String description(){ return new String(); }

	/**
	 * コマンドの説明
	 * @param sender プレイヤー
	 * @return String 説明文
	 */
	public String description(WhiteCommandSender sender){ return new String(); }

}
