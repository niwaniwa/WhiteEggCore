package com.github.niwaniwa.we.core.util.lib;

import com.github.niwaniwa.we.core.util.Extension;
import com.github.niwaniwa.we.core.util.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class Title extends Extension {

    String mainTitle;
    String subTitle;

    int fadeInTime;
    int stayTime;
    int fadeOutTick;

    ChatColor mainColor;
    ChatColor subColor;

    public Title(String mainTitle, String subTitle,
                 int fadeInTime, int stayTime, int fadeOutTick,
                 ChatColor mainTitleColor, ChatColor subTitleColor) {
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.fadeInTime = fadeInTime;
        this.stayTime = stayTime;
        this.fadeOutTick = fadeOutTick;
    }

    public Title(String mainTitle, String subTitle,
                 ChatColor mainTitleColor, ChatColor subTitleColor) {
        this(mainTitle, subTitle, 1 * 20, 4 * 20, 3 * 20, mainTitleColor, subTitleColor);
    }

    public Title(String mainTitle, String subTitle) {
        this(mainTitle, subTitle, ChatColor.WHITE, ChatColor.WHITE);
    }

    public Title(String mainTitle, ChatColor color) {
        this(mainTitle, "", color, ChatColor.WHITE);
    }

    public Title(String mainTitle) {
        this(mainTitle, ChatColor.WHITE);
    }

    public void setMainTitle(String title) {
        this.mainTitle = title;
    }

    public void setSubTitle(String title) {
        this.subTitle = title;
    }

    public void setMainTitleColor(ChatColor color) {
        this.mainColor = color;
    }

    public void setSubTitleColor(ChatColor color) {
        this.subColor = color;
    }

    public void setFadeInTime(int time) {
        this.fadeInTime = time;
    }

    public void setFadeOutTime(int time) {
        this.fadeOutTick = time;
    }

    public void setStayTime(int time) {
        this.stayTime = time;
    }

    @Override
    public void send(Player player) {
        Class<?> iChatBaseComponentClass = Reflection.getNMSClass("IChatBaseComponent");
        Class<?> enumTitle = Reflection.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
        Object timePacket = Reflection.createPacketInstance("PacketPlayOutTitle", new Class[]{ enumTitle, iChatBaseComponentClass, int.class, int.class, int.class}, getTitleEnum("TIMES"), null,
                fadeInTime, stayTime, fadeOutTick);
        this.sendPacket(player, timePacket);

        if (!mainTitle.isEmpty()) {
            Object mainTitlePacket = Reflection.createPacketInstance("PacketPlayOutTitle", new Class[]{ enumTitle, iChatBaseComponentClass }, getTitleEnum("TITLE"), build(mainTitle));
            this.sendPacket(player, mainTitlePacket);
        }
        if (!subTitle.isEmpty()) {
            Object subTitlePacket = Reflection.createPacketInstance("PacketPlayOutTitle", new Class[]{ enumTitle, iChatBaseComponentClass }, getTitleEnum("SUBTITLE"), build(subTitle));
            this.sendPacket(player, subTitlePacket);
        }

    }

    private Object getTitleEnum(String name){
        Class<?> enumTitle = Reflection.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
        Object em = null;
        try {
            em = enumTitle.getDeclaredMethod("a", String.class).invoke(null, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return em;
    }

}
