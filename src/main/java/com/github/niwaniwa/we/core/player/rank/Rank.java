package com.github.niwaniwa.we.core.player.rank;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class Rank {

	private static List<Rank> ranks = new ArrayList<>();

	private String prefix;
	private String rankName;
	private RankProperty property;

	public Rank(String prefix, ChatColor color, String name, RankProperty property) {
		this.prefix = "§" + color.getChar() + prefix;
		this.rankName = name;
		this.property = property;
		Rank.ranks.add(this);
	}

	public Rank(String prefix, String name, RankProperty property){
		this(prefix, ChatColor.WHITE, name, property);
	}

	public Rank(String prefix, String name){
		this(prefix, name, RankProperty.NORMAL);
	}

	public String getName(){
		return rankName;
	}

	public String getPrefix(){
		return prefix;
	}

	public RankProperty getProperty() {
		return property;
	}

	public void setProperty(RankProperty property) {
		this.property = property;
	}

	public static List<Rank> getRanks(){
		return ranks;
	}

}
