package com.github.niwaniwa.we.core.command.twitter;

import java.util.Arrays;

import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.api.Callback;
import com.github.niwaniwa.we.core.command.abstracts.AbstractWhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.abstracts.ConsoleCancellable;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.util.message.LanguageType;

public class WhiteEggTwitterCommand extends AbstractWhiteEggCoreCommand implements ConsoleCancellable {

	private final String key = commandMessageKey + ".twitter";
	private final String permission = commandPermission + ".twitter";

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission(permission)){
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
		Arrays.asList(args).forEach(s -> sb.append(s));
		// tweet
		player.getTwitterManager().tweet(sb.toString(), new Callback() {
			@Override
			public void onTwitter(Boolean twitter) {
				if(twitter){
					player.sendMessage(msg.getMessage(player, key + ".successfull", msgPrefix, true));
					return;
				}
				player.sendMessage(msg.getMessage(player, key + ".failure", msgPrefix, true));
			}
		});
		return true;
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		sender.sendMessage("&7----- &6/twitter &7-----");
		sender.sendMessage("&7/twitter <tweet> &f: ツイートします");
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
