package com.github.niwaniwa.we.core.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.niwaniwa.we.core.WhiteEggCore;
import org.bukkit.Sound;
import org.bukkit.command.Command;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreBaseCommandExecutor;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.github.niwaniwa.we.core.util.Util;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

/**
 * Whisperのコマンドクラス
 *
 * @author nwianiwa
 */
public class WhiteEggWhisperCommand extends WhiteEggCoreBaseCommandExecutor implements ConsoleCancellable {

    private final String key = commandMessageKey + ".whisper";
    private final String permission = commandPermission + ".whisper";

    @Override
    public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(msg.getMessage(sender, error_Permission, "", true));
            return true;
        }
        if (args.length <= 1) {
            sendUsing((WhitePlayer) sender);
            return true;
        }
        WhitePlayer player = (WhitePlayer) sender;
        WhitePlayer target = WhiteEggAPI.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(msg.getMessage(player, error_Player, msgPrefix, true).replace("", ""));
            return true;
        }
        if (player.getUniqueId().equals(target.getUniqueId())) {
            return true;
        }
        String message = Util.build(args, 1);
        target.sendMessage(replace(msg.getMessage(target, key + ".format", "", true), player, target, message, true));
        player.sendMessage(replace(msg.getMessage(player, key + ".format", "", true), player, target, message, false));
        target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        target.getPlayer().setMetadata("replay",new FixedMetadataValue(WhiteEggCore.getInstance(), player.getFullName()));
        player.getPlayer().setMetadata("replay",new FixedMetadataValue(WhiteEggCore.getInstance(), target.getFullName()));
        return true;
    }

    private String replace(String s, WhitePlayer from, WhitePlayer to, String message, boolean isFrom) {
        return s.replace("%from%", from.getFullName()).replace("%to%", to.getFullName()).replace("%message%", message).replace("%isfrom%", isFrom ? "from" : "to").replace("%player%", isFrom ? from.getFullName() : to.getFullName()).replace("%command%", getCommandName());
    }

    @Override
    public void sendUsing(WhitePlayer sender) {
        sender.sendMessage(msg.getMessage(sender, key + ".using", "&f[&6Using&7]", true).replace("%command%", getCommandName()));
    }

    @Override
    public String getPermission() {
        return permission;
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
