package com.github.niwaniwa.we.core.command.twitter;

import org.bukkit.command.Command;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.command.abstracts.AbstractWhiteEggCoreCommand;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.twitter.PlayerTwitterManager;
import com.github.niwaniwa.we.core.util.message.LanguageType;

public class WhiteEggTwitterCommand extends AbstractWhiteEggCoreCommand {

	private final String key = commandMessageKey + ".twitter";
	private final String permission = commandPermission + ".twitter";

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof WhitePlayer)){
			sender.sendMessage(msg.getMessage(sender, error_Console, "", true));
			return true;
		} else if(!sender.hasPermission(permission)){
			sender.sendMessage(msg.getMessage(sender, error_Permission, "", true));
			return true;
		}
		final WhitePlayer player = (WhitePlayer) sender;
		if(args.length == 0){
			this.sendUsing(player);
			return true;
		}
		if(player.getTwitterManager().getAccessToken() == null){
			player.sendMessage(
					msg.getMessage(player, key + ".notAcccess", msgPrefix, true));
			return true;
		}
		StringBuilder sb = new StringBuilder();
		for(String str : args){
			sb.append(str).append(" ");
		}
		// tweet
		player.getTwitterManager().tweet(sb.toString());
		new BukkitRunnable() {
			@Override
			public void run() {
				if(((PlayerTwitterManager) player.getTwitterManager()).isSuccessfull()){
					player.sendMessage(msg.getMessage(player, key + ".successfull", msgPrefix, true));
					return;
				}
				player.sendMessage(msg.getMessage(player, key + ".failure", msgPrefix, true));
			}
		}.runTaskLater(WhiteEggCore.getInstance(), 20);
		((PlayerTwitterManager) player.getTwitterManager()).set(false);
		return true;
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		sender.sendMessage("&7----- &6/twitter &7-----");
		sender.sendMessage("");
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public String description() {
		return msg.getMessage(LanguageType.en_US, key + ".description", "", true);
	}

	@Override
	public String description(WhiteCommandSender sender) {
		return msg.getMessage(sender, key + ".description", "", true);
	}

}
