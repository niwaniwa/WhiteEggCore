package com.github.niwaniwa.we.core.player;

import java.util.ArrayList;
import java.util.List;

import com.github.niwaniwa.we.core.WhiteEggCore;

public class SubAccount {

	private List<WhiteEggPlayer> players;

	public SubAccount() {
		this.players = new ArrayList<>();
	}

	public List<WhiteEggPlayer> get(){
		return players;
	}

	public boolean add(WhitePlayer player){
		if(!(player instanceof WhiteEggPlayer)){ return false; }
		return players.add((WhiteEggPlayer) player);
	}

	public boolean contains(WhitePlayer player){
		if(!(player instanceof WhiteEggPlayer)){ return false; }
		return players.contains(player);
	}

	public boolean remove(Object o){
		return players.remove(o);
	}

	public static void determine(WhitePlayer player) {
		if(!(player instanceof WhiteEggPlayer)){ return; }
		WhiteEggPlayer egg = (WhiteEggPlayer) player;
		for(WhitePlayer p : WhiteEggCore.getAPI().getOnlinePlayers()){
			if(!(p instanceof WhiteEggPlayer)){ continue; }
			WhiteEggPlayer egg2 = (WhiteEggPlayer) p;
			String address1 = egg2.getAddress().getAddress().toString().replaceAll("/", "");
			String address2 = egg.getAddress().getAddress().toString().replaceAll("/", "");
			if(address1.equals(address2)){
				egg.addAccount(egg);
			}
		}
	}

}
