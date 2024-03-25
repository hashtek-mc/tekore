package fr.hashtek.tekore.bungee.listener;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.tekore.bungee.Tekord;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DisconnectEvent implements Listener, HashLoggable {

	private Tekord cord;
	
	
	/**
	 * Creates a new instance of DisconnectEvent.
	 * Used to register the event.
	 * 
	 * @param	cord	Tekord instance
	 */
	public DisconnectEvent(Tekord cord)
	{
		this.cord = cord;
	}
	
	
	/**
	 * Called when a player logs out of the proxy.
	 */
	@EventHandler
	public void onQuit(PlayerDisconnectEvent event)
	{
		/* ... */
	}
	
}
