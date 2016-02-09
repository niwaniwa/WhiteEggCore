package com.github.niwaniwa.we.core.command.twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.callback.Callback;
import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreBaseCommandExecutor;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.util.command.CommandFactory;

public class WhiteEggTwitterCommand extends WhiteEggCoreBaseCommandExecutor implements ConsoleCancellable {

	private final String key = commandMessageKey + ".twitter";
	private final String permission = commandPermission + ".twitter";
	private final String commandName = "twitter";

	public WhiteEggTwitterCommand() {
		if(WhiteEggCore.getConf().useTwitter()){ CommandFactory.newCommand(WhiteEggCore.getInstance(), WhiteEggCore.msgPrefix, commandName, Arrays.asList("tw"), null, null, permission, WhiteEggCore.getInstance(), null); }
	}

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(!WhiteEggCore.getConf().useTwitter()){ return true; /* error */ }
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
			player.sendMessage(msg.getMessage(player, key + ".notAcccess", msgPrefix, true));
			return true;
		}

		StringBuilder sb = new StringBuilder();
		Arrays.asList(args).forEach(s -> sb.append(s));
		// tweet
		player.getTwitterManager().tweet(sb.toString(), new Callback() {
			@Override
			public void onTwitter(boolean twitter) {
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
	public String getCommandName() {
		return "twitter";
	}

	@Override
	public List<String> getUsing() {
		return new ArrayList<String>(0);
	}

}
