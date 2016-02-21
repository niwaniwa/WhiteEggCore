package com.github.niwaniwa.we.core.util.lib;

import com.github.niwaniwa.we.core.util.Extension;
import com.github.niwaniwa.we.core.util.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ActionBar extends Extension {

    private String str;

    public ActionBar(String str) {
        this.str = str;
    }

    public ActionBar(String str, ChatColor color) {
        this("§" + color.getChar() + str);
    }

    public ActionBar() {
        this("");
    }

    public void setMessage(String str) {
        this.str = str;
    }

    @Override
    public void send(Player player) {
        Object packet = Reflection.createPacketInstance("PacketPlayOutChat", new Class[]{String.class, Byte.class}, build(str), (byte) 2);
        super.sendPacket(player, packet);
    }

}
