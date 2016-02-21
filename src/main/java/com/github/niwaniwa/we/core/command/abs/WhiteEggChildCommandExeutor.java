package com.github.niwaniwa.we.core.command.abs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface WhiteEggChildCommandExeutor extends WhiteBaseCommandExeutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    public String getParentCommand();

}