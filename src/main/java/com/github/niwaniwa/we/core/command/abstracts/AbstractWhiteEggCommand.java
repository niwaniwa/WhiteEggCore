package com.github.niwaniwa.we.core.command.abstracts;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public abstract class AbstractWhiteEggCommand {

	protected final String commandMessageKey = "whiteegg.command";
	protected final String error_Console = commandMessageKey + ".console";
	protected final String error_Permission = commandMessageKey + ".notpermission";
	protected final String error_Player = commandMessageKey + ".notfound";
	protected WhiteEggAPI api = WhiteEggCore.getAPI();

	public abstract void sendUsing(WhitePlayer sender);

	public abstract String getPermission();

}
