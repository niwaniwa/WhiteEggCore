package com.github.niwaniwa.we.core.command;

import com.github.niwaniwa.we.core.player.WhitePlayer;

public abstract class AbstractWhiteEggCommand {

	public final String commandMessageKey = "whiteegg.command";
	public final String error_Console = commandMessageKey + ".console";
	public final String error_Permission = commandMessageKey + ".notpermission";

	public abstract void sendUsing(WhitePlayer sender);

	public abstract String getPermission();

}
