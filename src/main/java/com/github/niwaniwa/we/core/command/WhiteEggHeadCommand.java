package com.github.niwaniwa.we.core.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.niwaniwa.we.core.player.WhitePlayer;

public class WhiteEggHeadCommand extends AbstractWhiteEggCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// console
			return true;
		} else if(args.length == 0){
			// using
			return true;
		}

		Player player = (Player) sender;

		ItemStack is = new ItemStack(Material.SKULL_ITEM,  (short) 3);
		SkullMeta meta = (SkullMeta) is.getItemMeta();
		meta.setOwner(args[0]);
		is.setItemMeta(meta);
		player.getInventory().addItem(is);
		// message
		return true;
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public String getPermission() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
