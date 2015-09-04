package com.github.niwaniwa.we.core.command.twitter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.twitter.TwitterManager;
import com.github.niwaniwa.we.core.util.clickable.ChatExtra;
import com.github.niwaniwa.we.core.util.clickable.ChatFormat;
import com.github.niwaniwa.we.core.util.clickable.ClickEventType;
import com.github.niwaniwa.we.core.util.clickable.Clickable;
import com.github.niwaniwa.we.core.util.clickable.HoverEventType;

public class WhiteEggTwitterRegisterCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(!(sender instanceof Player)){
			// console
			return true;
		}
		WhitePlayer player = WhitePlayerFactory.newInstance((Player) sender);
		TwitterManager tw = player.getTwitterManager();
		if(args.length == 0){
			if(tw.getAccessToken() != null){
				//
				return true;
			}
			// message
			this.sendURL(player);
		} else if(args.length == 1){
			if(!(args[0].length() > 0)){
				//
				return true;
			}
			if(tw.OAuthRequest(args[0])){
				// success
				return true;
			}
			// failure
			return true;
		}
		return true;
	}

	private void sendURL(WhitePlayer p){
		List<ChatFormat> f = new ArrayList<ChatFormat>();
		f.add(ChatFormat.BOLD);
		Clickable click = new Clickable("Click -->", ChatColor.GOLD, f);
		ChatExtra extra = new ChatExtra("Open URL", ChatColor.GRAY, f);
		extra.setClickEvent(ClickEventType.OPEN_URL, p.getTwitterManager().getOAuthRequestURL());
		extra.setHoverEvent(HoverEventType.SHOW_TEXT, "§bTwitter OAuth Request URL");
		click.addExtra(extra);
		click.send(p.getPlayer());
	}

}
