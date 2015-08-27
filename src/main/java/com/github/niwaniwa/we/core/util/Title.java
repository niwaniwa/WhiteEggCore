package com.github.niwaniwa.we.core.util;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class Title extends Util{

	String mainTitle;
	String subTitle;

	int fadeInTime;
	int stayTime;
	int fadeOutTick;

	ChatColor mainColor;
	ChatColor subColor;

	public Title(String mainTitle, String subTitle,
			int fadeInTime, int stayTime, int fadeOutTick,
			ChatColor mainTitleColor, ChatColor subTitleColor){
		this.mainTitle = mainTitle;
		this.subTitle = subTitle;
		this.fadeInTime = fadeInTime;
		this.stayTime = stayTime;
		this.fadeOutTick = fadeOutTick;
	}

	public Title(String mainTitle, String subTitle,
			ChatColor mainTitleColor, ChatColor subTitleColor){
		this(mainTitle,subTitle,1*20,4*20,3*20,mainTitleColor,subTitleColor);
	}

	public Title(String mainTitle, String subTitle){
		this(mainTitle,subTitle,ChatColor.WHITE,ChatColor.WHITE);
	}

	public Title(String mainTitle, ChatColor color){
		this(mainTitle,"",color,ChatColor.WHITE);
	}

	public Title(String mainTitle){
		this(mainTitle,ChatColor.WHITE);
	}

	public void setMainTitle(String title){
		this.mainTitle = title;
	}

	public void setSubTitle(String title){
		this.subTitle = title;
	}

	public void setMainTitleColor(ChatColor color){
		this.mainColor = color;
	}

	public void setSubTitleColor(ChatColor color){
		this.subColor = color;
	}

	public void setFadeInTime(int time){
		this.fadeInTime = time;
	}

	public void setFadeOutTime(int time) {
		this.fadeOutTick = time;
	}

	public void setStayTime(int time) {
		this.stayTime = time;
	}

	@Override
	public void send(Player player){

		PacketPlayOutTitle time = new PacketPlayOutTitle(
				PacketPlayOutTitle.EnumTitleAction.TIMES, null,
				fadeInTime, stayTime, fadeOutTick);

		cast(player).getHandle().playerConnection.sendPacket(time);

		if(!subTitle.isEmpty()){
			PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, build(subTitle));
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitlePacket);
		}

		if(!mainTitle.isEmpty()){
			PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, build(mainTitle));
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
		}

	}

}
