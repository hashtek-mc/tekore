package fr.hashtek.tekore.bungee.events;

import fr.hashtek.hashlogger.HashLoggable;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DisconnectEvent implements Listener, HashLoggable {
	
	/**
	 * Called when a player logs out of the proxy.
	 */
	@EventHandler
	public void onQuit(PlayerDisconnectEvent event)
	{
		
	}
	
}
