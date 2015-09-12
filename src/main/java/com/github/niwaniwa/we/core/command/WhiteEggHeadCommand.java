package com.github.niwaniwa.we.core.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public class WhiteEggHeadCommand extends AbstractWhiteEggCoreCommand {

	private final String key = commandMessageKey + ".head";
	private final String permission = commandPermission + ".head";

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof WhitePlayer)){
			sender.sendMessage(msg.getMessage(sender, error_Console, "", true));
			return true;
		}
		if(!sender.hasPermission(permission)){
			sender.sendMessage(msg.getMessage(sender, error_Permission, "", true));
			return true;
		}
		if(args.length == 0){
			this.sendUsing((WhitePlayer) sender);
			return true;
		}
		WhitePlayer player = (WhitePlayer) sender;
		player.getPlayer().getInventory().addItem(getMobHead(args[0]));
		player.sendMessage(msgPrefix + "&asuccessfull!");
		return true;
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		String ukey = key + ".using";
		sender.sendMessage("&7----- &6/head &7-----");
		sender.sendMessage("&6/head <target name> &f: &7"
				+ msg.getMessage(sender, ukey + ".description", "", true));
	}

	@Override
	public String getPermission() {
		return permission;
	}

	private ItemStack getMobHead(String target){
		ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) is.getItemMeta();
		meta.setOwner(target);
		is.setItemMeta(meta);
		return is;
	}

	@Override
	public String description() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String description(WhiteCommandSender sender) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
