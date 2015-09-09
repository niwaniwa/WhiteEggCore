package com.github.niwaniwa.we.core.command.twitter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.command.AbstractWhiteEggCommand;
import com.github.niwaniwa.we.core.event.WhiteEggTweetEvent;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;

public class WhiteEggTwitterCommand extends AbstractWhiteEggCommand {

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
		WhitePlayer player = (WhitePlayer) sender;
		if(args.length == 0){
			this.sendUsing(player);
			return true;
		}
		if(player.getTwitterManager().getAccessToken() == null){
			player.sendMessage(
					msg.getMessage(player, key + "notAcccess", msgPrefix, true));
			return true;
		}
		StringBuilder sb = new StringBuilder();
		for(String str : args){
			sb.append(str+ " ");
		}
		WhiteEggTweetEvent event = new WhiteEggTweetEvent(player, sb.toString());
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()){
			return true;
		}

		if(player.getTwitterManager().tweet(event.getTweet())){
			player.sendMessage(msg.getMessage(player, key + ".successfull", msgPrefix, true));
			return true;
		}
		player.sendMessage(msg.getMessage(player, key + ".failure", msgPrefix, true));
		return true;
	}

	@Override
	public void sendUsing(WhitePlayer sender) {

	}

	@Override
	public String getPermission() {
		return permission;
	}

}
