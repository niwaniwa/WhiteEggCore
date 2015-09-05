package com.github.niwaniwa.we.core.player.rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

public class Rank {

	private static List<Rank> ranks = new ArrayList<>();

	private String prefix;
	private String rankName;
	private RankProperty property;
	private String permission;

	public Rank(String prefix, ChatColor color, String name, RankProperty property, String permission) {
		this.prefix = "§" + color.getChar() + prefix;
		this.rankName = name;
		this.property = property;
		this.setPermission(permission);
	}

	public Rank(String prefix, String name, RankProperty property,String permission){
		this(prefix, ChatColor.WHITE, name, property,permission);
	}

	public Rank(String prefix, String name){
		this(prefix, name, RankProperty.NORMAL, "");
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

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public void add(){
		ranks.add(this);
	}

	public static List<Rank> getRanks(){
		return ranks;
	}

	public static Rank parserRank(Map<String, Object> o){
		Rank rank = new Rank(String.valueOf(o.get("prefix")), String.valueOf(o.get("name")),
				RankProperty.valueOf(String.valueOf(o.get("property"))),
				String.valueOf(o.get("permission")));
		for(Rank r : ranks){
			if(r.equals(rank)){
				return r;
			}
		}
		return null;
	}

}
