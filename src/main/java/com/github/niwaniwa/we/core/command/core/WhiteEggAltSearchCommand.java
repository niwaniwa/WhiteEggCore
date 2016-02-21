package com.github.niwaniwa.we.core.command.core;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.command.abs.core.WhiteEggCoreChildCommandExecutor;
import com.github.niwaniwa.we.core.json.JsonManager;
import com.github.niwaniwa.we.core.player.AltAccount;
import com.github.niwaniwa.we.core.player.WhiteEggPlayer;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.player.WhitePlayerFactory;
import com.github.niwaniwa.we.core.player.commad.WhiteCommandSender;
import com.google.gson.JsonObject;

public class WhiteEggAltSearchCommand extends WhiteEggCoreChildCommandExecutor {

    private final String permission = commandPermission + ".whiteegg.alt";
    private final String parentCommand = "whiteeggcore";

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(final WhiteCommandSender sender, Command command, String label, final String[] args) {
        if (args.length <= 1) {
            // error
            System.out.println(args.length);
            return true;
        }
        WhitePlayer player = this.getPlayer(args[1]);
        if (player != null) {
            if (!(player instanceof WhiteEggPlayer)) {
                throw new IllegalArgumentException(String.format("Class %s does not extends WhiteEggPlayer", new Object[]{player.getClass().getSimpleName()}));
            }
            WhiteEggPlayer eggPlayer = (WhiteEggPlayer) player;
            this.callback(sender, eggPlayer.getAccounts(), player.getFullName(), player.getUniqueId());
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    UUID uuid = null;
                    if (args[1].startsWith("$")) {
                        String uuidString = args[1].replace("$", "");
                        if (uuidString.length() != 36) {
                            // error
                            System.out.println("not 36");
                            return;
                        }
                        uuid = Bukkit.getOfflinePlayer(UUID.fromString(uuidString)).getUniqueId();
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (offlinePlayer == null) {
                            sender.sendMessage(msg.getMessage(sender, error_Player, "", true));
                            return;
                        }
                        uuid = offlinePlayer.getUniqueId();
                    }
                    String jsonString = WhitePlayerFactory.getPlayerData(uuid.toString(), WhiteEggPlayer.class);
                    if (jsonString.isEmpty() || jsonString == null) {
                        // player not found
                        System.out.println("not found");
                        return;
                    }
                    JsonObject json = new JsonManager().createJsonObject(jsonString);
                    if (json == null) {
                        sender.sendMessage(msg.getMessage(sender, error_Player, "", true));
                        return;
                    }
                    callback(sender, AltAccount.parser(json.getAsJsonObject("player").toString()), json.getAsJsonObject("player").get("name").getAsString(), uuid);
                }
            }.runTaskAsynchronously(WhiteEggCore.getInstance());
        }
        return true;
    }

    private void callback(WhiteCommandSender sender, AltAccount account, String name, UUID target) {
        if (account.get().isEmpty()) {
            sender.sendMessage("&cEmpty");
            return;
        }
        int loop = 1;
        sender.sendMessage("&7 ----- &6" + name + "'s account &7(" + target.toString() + ") -----");
        for (String uuid : account.get()) {
            sender.sendMessage("&7: &b" + loop + " &7: &6" + uuid + " &7: &6" + this.getPlayerName(uuid) + " &7:");
            loop++;
        }
    }

    public String getPlayerName(String uuid) {
        WhitePlayer player = this.getPlayer("$" + uuid);
        if (player == null) {
            return this.getOfflinePlayerName(uuid);
        }
        return player.getFullName();
    }

    public String getOfflinePlayerName(String uuid) {
        return Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
    }

    @Override
    public String getParentCommand() {
        return parentCommand;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public List<String> getUsing() {
        return Arrays.asList();
    }

    @Override
    public String getCommandName() {
        return "alt";
    }

    private WhitePlayer getPlayer(String name) {
        WhitePlayer player = null;
        if (name.startsWith("$")) {
            String uuidString = name.replace("$", "");
            if (uuidString.length() != 36) {
                return null;
            }
            UUID uuid = UUID.fromString(uuidString);
            player = WhiteEggAPI.getPlayer(uuid);
            return player;
        }
        player = WhiteEggAPI.getPlayer(name);
        return player;
    }

}
