package com.github.niwaniwa.we.core.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreLowCommandExecutor;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.util.Util;

/**
 * Whisperのコマンドクラス
 * @author nwianiwa
 *
 */
public class WhiteEggWhisperCommand extends WhiteEggCoreLowCommandExecutor implements ConsoleCancellable{

	private static final Map<WhitePlayer, WhitePlayer> replay = new HashMap<>();

	private final String key = commandMessageKey + ".whisper";
	private final String permission = commandPermission + ".whisper";

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission(permission)){
			sender.sendMessage(msg.getMessage(sender, error_Permission, "", true));
			return true;
		}
		if(args.length <= 1){
			sendUsing((WhitePlayer) sender);
			return true;
		}
		WhitePlayer player = (WhitePlayer) sender;
		WhitePlayer target = WhiteEggAPI.getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(msg.getMessage(player, error_Player, msgPrefix, true));
			return true;
		}
		if(player.getUniqueId().equals(target.getUniqueId())){
			return true;
		}
		String message = Util.build(args, 1);
		target.sendMessage(replace(msg.getMessage(target, key + ".format", "", true), player, target, message));
		player.sendMessage(replace(msg.getMessage(player, key + ".format", "", true), player, target, message));
		target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
		replay.put(target, player);
		return true;
	}

	private String replace(String s, WhitePlayer from, WhitePlayer to, String message){
		return s.replace("%from%", from.getName()).replace("%to%", to.getName()).replace("%message%", message);
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		sender.sendMessage("&6! ω !");
	}

	@Override
	public String getPermission() {
		return permission;
	}

	public static Map<WhitePlayer, WhitePlayer> getPlayer(){
		return replay;
	}

	@Override
	public String getCommandName() {
		return "whisper";
	}

	@Override
	public List<String> getUsing() {
		return Arrays.asList("");
	}

}
