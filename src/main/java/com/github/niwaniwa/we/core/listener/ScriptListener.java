package com.github.niwaniwa.we.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.script.JavaScript;

/**
 * Script用リスナー
 * @author niwaniwa
 *
 */
public class ScriptListener implements Listener {

	public ScriptListener() {
		if(WhiteEggCore.getInstance().getScript() != null){
			Bukkit.getPluginManager().registerEvents(this, WhiteEggCore.getInstance());
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){ JavaScript.callEvent("command", event); }

	@EventHandler
	public void onJoin(PlayerJoinEvent event){ JavaScript.callEvent("join", event); }

}
