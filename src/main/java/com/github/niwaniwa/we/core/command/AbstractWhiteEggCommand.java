package com.github.niwaniwa.we.core.command;

import com.github.niwaniwa.we.core.player.WhitePlayer;

public abstract class AbstractWhiteEggCommand {

	public final String commandPermission = "whiteegg.core.command";
	public final String msgPrefix = "§7[§bWEC§7]§r";
	public final String logPrefix = "[WEC]";
	public final String commandMessageKey = "whiteegg.command";

	public abstract void sendUsing(WhitePlayer sender);

	public abstract String getPermission();

}
