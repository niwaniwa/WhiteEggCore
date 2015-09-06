package com.github.niwaniwa.we.core.command.twitter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.command.AbstractWhiteEggCommand;
import com.github.niwaniwa.we.core.event.WhiteEggTweetEvent;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;

public class WhiteEggTwitterCommand extends AbstractWhiteEggCommand implements CommandExecutor {

	public WhiteEggTwitterCommand() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// message
			return true;
		}
		WhitePlayer player = WhitePlayerFactory.newInstance((Player) sender);
		if(args.length == 0){
			this.sendUsing(player);
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
		return player.getTwitterManager().tweet(event.getTweet());
	}

	@Override
	public void sendUsing(WhitePlayer sender) {

	}

	@Override
	public String getPermission() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
