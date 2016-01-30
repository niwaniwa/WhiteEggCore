package com.github.niwaniwa.we.core.command.twitter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.callback.Callback;
import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreBaseCommandExecutor;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.twitter.TwitterManager;
import com.github.niwaniwa.we.core.util.CommandUtil;
import com.github.niwaniwa.we.core.util.lib.clickable.ChatExtra;
import com.github.niwaniwa.we.core.util.lib.clickable.ChatFormat;
import com.github.niwaniwa.we.core.util.lib.clickable.ClickEventType;
import com.github.niwaniwa.we.core.util.lib.clickable.Clickable;
import com.github.niwaniwa.we.core.util.lib.clickable.HoverEventType;

public class WhiteEggTwitterRegisterCommand extends WhiteEggCoreBaseCommandExecutor implements ConsoleCancellable {

	private final String key = commandMessageKey + ".twitter.register";
	private final String permission = commandPermission + ".twitter.register";
	private final String commandName = "register";

	public WhiteEggTwitterRegisterCommand() {
		if(WhiteEggCore.getConf().useTwitter()){ CommandUtil.registerCommand(WhiteEggCore.getInstance(), WhiteEggCore.msgPrefix, commandName, null, null, null, permission, WhiteEggCore.getInstance(), null); }
	}

	@Override
	public boolean onCommand(final WhiteCommandSender sender, final Command cmd, final String label,
			final String[] args) {
		if(!sender.hasPermission(permission)){
			sender.sendMessage(msg.getMessage(sender, error_Permission, "", true));
			return true;
		}
		final WhitePlayer player = (WhitePlayer) sender;
		final TwitterManager tw = player.getTwitterManager();
		if(tw == null){
			sender.sendMessage("&cAn internal error occurred while attempting to perform this command");
			return true;
		}
		if(tw.getAccessToken() != null){
			//
			return true;
		}
		if(args.length == 0){
			// message
			this.sendURL(player);
		} else if(args.length == 1){
			tw.OAuthAccess(args[0], new Callback(){
				@Override
				public void onTwitter(boolean isSuccess) {
					if(isSuccess){
						sender.sendMessage(msg.getMessage(player, key + ".fail", msgPrefix, true)); // success
						return;
					}
					sender.sendMessage(msg.getMessage(player, key + ".success", msgPrefix, true)); // failure
				}
			});
			return true;
		}
		return true;
	}

	private void sendURL(WhitePlayer p){
		// 要修正
		List<ChatFormat> f = new ArrayList<>();
		f.add(ChatFormat.BOLD);
		Clickable click = new Clickable("->", ChatColor.GOLD, f);
		ChatExtra extra = new ChatExtra("Click", ChatColor.GOLD, f);
		extra.setClickEvent(ClickEventType.OPEN_URL, p.getTwitterManager().getOAuthRequestURL());
		extra.setHoverEvent(HoverEventType.SHOW_TEXT, "§bTwitter OAuth Request URL");
		click.addExtra(extra);
		click.send(p.getPlayer());
	}

	@Override
	public void sendUsing(WhitePlayer sender) {
		sender.sendMessage("&7----- &6/tweet &7-----");
		sender.sendMessage("&6/register &f: &7"
				+ msg.getMessage(sender, key + ".using.line_1", "", true));
		sender.sendMessage("&6/register <pin> &f: &7"
				+ msg.getMessage(sender, key + ".using.line_2", "", true));
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public List<String> getUsing() {
		return new ArrayList<String>(0);
	}

}
