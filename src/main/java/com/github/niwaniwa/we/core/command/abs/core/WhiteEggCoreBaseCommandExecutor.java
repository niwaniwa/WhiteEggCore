package com.github.niwaniwa.we.core.command.abs.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.abs.WhiteEggCommandExecutor;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.commad.WhiteConsoleSender;
import com.github.niwaniwa.we.core.util.CommandUtil;
import com.github.niwaniwa.we.core.util.message.MessageManager;

public abstract class WhiteEggCoreBaseCommandExecutor implements WhiteEggCommandExecutor {


	protected final String commandMessageKey = "whiteegg.command";
	protected final String error_Console = commandMessageKey + ".console";
	protected final String error_Permission = commandMessageKey + ".notpermission";
	protected final String error_Player = commandMessageKey + ".notfound";
	protected final String commandPermission = "whiteegg.core.command";
	protected final String msgPrefix = WhiteEggCore.msgPrefix;
	protected final String logPrefix = "[WEC]";
	protected MessageManager msg = WhiteEggCore.getMessageManager();

	/**
	 * コマンドの実行
	 * @param sender sender
	 * @param command command
	 * @param label label
	 * @param args args
	 * @return boolean
	 */
	public abstract boolean onCommand(WhiteCommandSender sender, Command command, String label, String[] args);

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return this.onCommand((CommandUtil.isConsole(sender) ? new WhiteConsoleSender(true) : WhitePlayerFactory.getInstance((Player) sender)), command, label, args);
	}

	public void sendUsing(Player player){ this.sendUsing(WhitePlayerFactory.getInstance(player)); }

	public void sendUsing(WhitePlayer player){}

}
