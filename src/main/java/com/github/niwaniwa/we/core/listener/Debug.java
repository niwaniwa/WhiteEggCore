package com.github.niwaniwa.we.core.listener;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import com.github.niwaniwa.we.core.util.lib.Title;
import com.github.niwaniwa.we.core.util.lib.clickable.ChatExtra;
import com.github.niwaniwa.we.core.util.lib.clickable.ClickEventType;
import com.github.niwaniwa.we.core.util.lib.clickable.HoverEventType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.command.abs.WhiteEggCommandExecutor;
import com.github.niwaniwa.we.core.event.WhiteEggPostTweetEvent;
import com.github.niwaniwa.we.core.event.WhiteEggPreTweetEvent;
import com.github.niwaniwa.we.core.player.WhitePlayer;
import com.github.niwaniwa.we.core.util.command.CommandFactory;
import com.github.niwaniwa.we.core.util.lib.clickable.Clickable;

import twitter4j.TwitterException;

public class Debug implements Listener {

    private CachedServerIcon icon;

    public Debug() {
        URL imageUrl = null;
        InputStream input = null;
        try {
            imageUrl = new URL("https://minotar.net/helm/KokekoKko_");
            input = imageUrl.openConnection().getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage bufferedImage;
        CachedServerIcon icon = null;
        try {
            bufferedImage = ImageIO.read(input);
            Image tmp = bufferedImage.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            icon = Bukkit.getServer().loadServerIcon(dimg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        icon = null;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        if (icon == null) {
            return;
        }
        event.setServerIcon(icon);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final WhitePlayer player = WhiteEggAPI.getPlayer(event.getPlayer());
        if (event.getPlayer().getUniqueId().toString()
                .equalsIgnoreCase("f010845c-a9ac-4a04-bf27-61d92f8b03ff")) {
            WhiteEggCore.getInstance().getLogger().info(
                    "-- " + player.getPlayer().getName() + "Join the game. --");
        }
        Title title = new Title("§6>>Main Title<<", "§7sub title");
        title.send(player.getPlayer());
        Clickable clickable = new Clickable("§6おめでとうございます！\nあなたは当選しました！");
        ChatExtra extra = new ChatExtra("\n§6このテキストをクリックすると豪華賞品が手に入ります！");
        extra.setClickEvent(ClickEventType.OPEN_URL, "https://twitter.com/");
        extra.setHoverEvent(HoverEventType.SHOW_TEXT, "ここをクリック！");
        clickable.addExtra(extra);
        clickable.send(player.getPlayer());
        CommandFactory commandFactory = new CommandFactory(WhiteEggCore.getInstance(), "debug");
        commandFactory.setExecutor(new WhiteEggCommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                sender.sendMessage("test");
                return true;
            }

            @Override
            public List<String> getUsing() {
                return null;
            }

            @Override
            public String getPermission() {
                return "whiteegg.core.command.debug";
            }

            @Override
            public String getCommandName() {
                return "debug";
            }
        });
        commandFactory.register();
    }

    @EventHandler
    public void onTweet(WhiteEggPreTweetEvent event) {
        System.out.println(" : Event " + event.getEventName() + " : "
                + " Tweet " + event.getTweet() + " : Player " + event.getPlayer().getFullName());
    }

    @EventHandler
    public void postTweetEvent(WhiteEggPostTweetEvent event) throws IllegalStateException, TwitterException {
        System.out.println(" : Event " + event.getEventName() + " : "
                + " Tweet " + event.getStatus().getText()
                + " : Twitter ID " + event.getTwitter().getTwitter().getScreenName());
    }

}
