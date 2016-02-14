package com.github.niwaniwa.we.core.command.core;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.List;

import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreChildCommandExecutor;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.sun.management.OperatingSystemMXBean;

public class WhiteEggSystemCommand extends WhiteEggCoreChildCommandExecutor {

	private final String permission = commandPermission + ".whiteegg.system";
	private final String parentCommand = "whiteeggcore";
	private final String commandName = "system";

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command command, String label, String[] args) {
		OperatingSystemMXBean systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		long totalPhysicalMemorySize = systemMXBean.getTotalPhysicalMemorySize();
		long usedMemory = (totalPhysicalMemorySize - systemMXBean.getFreePhysicalMemorySize());
//		int kb = 1000;
		int kbi = 1024;
		System.out.println(systemMXBean.getFreePhysicalMemorySize() / kbi);
		System.out.println(totalPhysicalMemorySize / kbi);
		sender.sendMessage("&7----- &6SYSTEM -----");
		sender.sendMessage("&7CPU Usage : &6" + new BigDecimal(systemMXBean.getSystemCpuLoad() * 100.0D).setScale(1, 4).doubleValue() + "&f%");
		sender.sendMessage("&7Use Memory : &6" + ((usedMemory / kbi) / kbi) + "&7MB ( " + (100 * systemMXBean.getFreePhysicalMemorySize() / totalPhysicalMemorySize) + "% )");
		return true;
	}

	@Override
	public String getParentCommand() {
		return parentCommand;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public List<String> getUsing() {
		return null;
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

}
