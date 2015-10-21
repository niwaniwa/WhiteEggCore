package com.github.niwaniwa.we.core.listener;

import java.security.PrivilegedActionException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.script.JavaScript;

public class ScriptListener implements Listener {

	private JavaScript script = WhiteEggCore.getInstance().getScript();

	public ScriptListener() {
		if(script == null){
			HandlerList.unregisterAll(this);
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		try {
			script.run("call", event); // TODO: 動いたらいいな(白目)
		} catch (PrivilegedActionException e) {
			e.printStackTrace();
		}
	}

}
