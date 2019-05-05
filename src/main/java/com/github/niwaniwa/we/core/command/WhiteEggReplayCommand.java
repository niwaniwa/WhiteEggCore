package com.github.niwaniwa.we.core.command;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import org.bukkit.Sound;
import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreBaseCommandExecutor;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.util.Util;
import org.bukkit.metadata.MetadataValue;

/**
 * リプライのコマンドクラス
 *
 * @author niwaniwa
 */
public class WhiteEggReplayCommand extends WhiteEggCoreBaseCommandExecutor implements ConsoleCancellable {

    private final String key = commandMessageKey + ".replay";
    private final String permission = commandPermission + ".replay";

    @Override
    public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(msg.getMessage(sender, error_Permission, "", true));
            return true;
        }
        if (args.length == 0) {
            sendUsing((WhitePlayer) sender);
            return true;
        }

        WhitePlayer player = (WhitePlayer) sender;
        if(!player.getPlayer().hasMetadata("replay")){
            sendUsing((WhitePlayer) sender);
            return true;
        }
        List<MetadataValue> meta = player.getPlayer().getMetadata("replay");
        WhitePlayer target = WhiteEggAPI.getPlayer(meta.get(0).asString());
        if (target == null) {
            player.sendMessage(msg.getMessage(player, error_Player, msgPrefix, true));
            return true;
        }
        String message = Util.build(args, 0);
        target.sendMessage(replace(msg.getMessage(target, key + ".format", "", true), player, target, message, true));
        player.sendMessage(replace(msg.getMessage(player, key + ".format", "", true), player, target, message, false));
        target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        return false;
    }

    private String replace(String s, WhitePlayer from, WhitePlayer to, String message, boolean isFrom) {
        return s.replace("%from%", from.getFullName()).replace("%to%", to.getFullName()).replace("%message%", message).replace("%isfrom%", isFrom ? "from" : "to").replace("%player%", isFrom ? from.getFullName() : to.getFullName()).replace("%command%", getCommandName());
    }

    @Override
    public void sendUsing(WhitePlayer sender) {
        sender.sendMessage(msg.getMessage(sender, key + ".using", "&f[&6Using&7]", true));
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getCommandName() {
        return "reply";
    }

    @Override
    public List<String> getUsing() {
        return Arrays.asList("");
    }

}
