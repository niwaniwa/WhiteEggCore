package com.github.niwaniwa.we.core.command;

import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.command.abstracts.AbstractWhiteEggCoreCommand;
import com.github.niwaniwa.we.core.command.abstracts.ConsoleCancellable;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.util.Util;

public class WhiteEggReplayCommand extends AbstractWhiteEggCoreCommand implements ConsoleCancellable{

	private final String key = commandMessageKey + ".replay";
	private final String permission = commandPermission + ".replay";

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission(permission)){
			sender.sendMessage(msg.getMessage(sender, error_Permission, "", true));
			return true;
		}
		if(args.length == 0){
			sendUsing((WhitePlayer) sender);
			return true;
		}

		WhitePlayer player = (WhitePlayer) sender;
		Map<WhitePlayer, WhitePlayer> replay = WhiteEggWhisperCommand.getPlayer();
		if(!replay.containsKey(player)){
			player.sendMessage(msg.getMessage(player, key + ".notfound", msgPrefix, true));
			return true;
		}
		WhitePlayer target = replay.get(player);
		if(target == null){ return true; }
		String message = Util.build(args, 0);
		target.sendMessage(replace(msg.getMessage(target, key + ".format", "", true), player, target, message));
		player.sendMessage(replace(msg.getMessage(player, key + ".format", "", true), player, target, message));
		target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
		return false;
	}

	private String replace(String s, WhitePlayer from, WhitePlayer to, String message){
		return s.replace("%from", from.getName()).replace("%to", to.getName()).replace("%message", message);
	}

	@Override
	protected String description() {
		return null;
	}

	@Override
	public String description(WhiteCommandSender sender) {
		return msg.getMessage(sender, key, "", true);
	}

	@Override
	public void sendUsing(WhitePlayer sender) {

	}

	@Override
	public String getPermission() {
		return permission;
	}

}
