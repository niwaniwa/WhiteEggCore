package com.github.niwaniwa.we.core.command;

import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.player.WhiteCommandSender;

public abstract class WhiteEggChildCommand extends AbstractWhiteEggCommand {

	public abstract void runCommand(WhiteCommandSender player, Command cmd, String[] args);

	public abstract String getParentCommand();

}
