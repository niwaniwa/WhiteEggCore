package com.github.niwaniwa.we.core.command;

import java.util.Arrays;
import java.util.List;

import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreBaseCommandExecutor;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;

/**
 * MobHeadのコマンドクラス
 *
 * @author niwaniwa
 */
public class WhiteEggHeadCommand extends WhiteEggCoreBaseCommandExecutor implements ConsoleCancellable {

    private final String key = commandMessageKey + ".head";
    private final String permission = commandPermission + ".head";

    @Override
    public boolean onCommand(WhiteCommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(msg.getMessage(sender, error_Permission, "", true));
            return true;
        }
        if (args.length == 0) {
            this.sendUsing((WhitePlayer) sender);
            return true;
        }
        WhitePlayer player = (WhitePlayer) sender;
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(target == null){
            sender.sendMessage(msg.getMessage(sender, key + ".fail", "", true).replace("%target%", args[0]));
            return true;
        }

        player.getPlayer().getInventory().addItem(getPlayerHead(target));
        player.sendMessage(msgPrefix + "&asuccessfull!");
        return true;
    }

    @Override
    public void sendUsing(WhitePlayer sender) {
        String ukey = key + ".using";
        sender.sendMessage("&7----- &6/head &7-----");
        sender.sendMessage("&6/head <target player name>  &f: &7"
                + msg.getMessage(sender, ukey + ".line_1", "", true));
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Deprecated
    private ItemStack getMobHead(String target) {
        /*
        ItemStack is = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) is.getItemMeta();
        meta.setOwner(target);
        is.setItemMeta(meta);
        return is;
        */

        return null;
    }

    private ItemStack getPlayerHead(OfflinePlayer player){
        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) is.getItemMeta();
        meta.setOwningPlayer(player);
        is.setItemMeta(meta);
        return is;
    }

    @Override
    public String getCommandName() {
        return "head";
    }

    @Override
    public List<String> getUsing() {
        return Arrays.asList("/head <target name>");
    }

}
