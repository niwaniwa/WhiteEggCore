package com.github.niwaniwa.we.core.command.twitter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.command.abstracts.AbstractWhiteEggCoreCommand;
import com.github.niwaniwa.we.core.player.WhiteCommandSender;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.twitter.TwitterManager;
import com.github.niwaniwa.we.core.util.clickable.ChatExtra;
import com.github.niwaniwa.we.core.util.clickable.ChatFormat;
import com.github.niwaniwa.we.core.util.clickable.ClickEventType;
import com.github.niwaniwa.we.core.util.clickable.Clickable;
import com.github.niwaniwa.we.core.util.clickable.HoverEventType;
import com.github.niwaniwa.we.core.util.message.LanguageType;

public class WhiteEggTwitterRegisterCommand extends AbstractWhiteEggCoreCommand  {

	private final String key = commandMessageKey + ".twitter.register";
	private final String permission = commandPermission + ".twitter.register";

	@Override
	public boolean onCommand(WhiteCommandSender sender, Command cmd, String label,
			String[] args) {
		if(!(sender instanceof WhitePlayer)){
			sender.sendMessage(msg.getMessage(sender, error_Console, "", true));
			return true;
		}
		if(!sender.hasPermission(permission)){
			sender.sendMessage(msg.getMessage(sender, error_Permission, "", true));
			return true;
		}
		WhitePlayer player = (WhitePlayer) sender;
		TwitterManager tw = player.getTwitterManager();
		if(tw.getAccessToken() != null){
			//
			return true;
		}
		if(args.length == 0){
			// message
			this.sendURL(player);
		} else if(args.length == 1){
			if(!(args[0].length() > 0)){
				//
				return true;
			}
			if(args[0].equalsIgnoreCase("reset")){
				tw.reset();
				sender.sendMessage("りせっとしました");
				return true;
			}
			if(tw.OAuthRequest(args[0])){
				sender.sendMessage(msg.getMessage(player, key + ".success", msgPrefix, true));
				// success
				return true;
			}
			sender.sendMessage(msg.getMessage(player, key + ".failure", msgPrefix, true));
			// failure
			return true;
		}
		return true;
	}

	private void sendURL(WhitePlayer p){
		// 要修正
		List<ChatFormat> f = new ArrayList<ChatFormat>();
		f.add(ChatFormat.BOLD);
		Clickable click = new Clickable("Click -->", ChatColor.GOLD, f);
		ChatExtra extra = new ChatExtra("Open URL", ChatColor.GRAY, f);
		extra.setClickEvent(ClickEventType.OPEN_URL, p.getTwitterManager().getOAuthRequestURL());
		extra.setHoverEvent(HoverEventType.SHOW_TEXT, "§bTwitter OAuth Request URL");
		click.addExtra(extra);
		click.send(p.getPlayer());
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		sender.sendMessage("&7----- &6/tweet &7-----");
		sender.sendMessage("&6/register &f: &7"
				+ msg.getMessage(sender, key + ".using.description_1", "", true));
		sender.sendMessage("&6/register <pin> &f: &7"
				+ msg.getMessage(sender, key + ".using.description_2", "", true));
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
