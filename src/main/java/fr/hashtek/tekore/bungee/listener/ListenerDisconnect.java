package fr.hashtek.tekore.bungee.listener;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bungee.Tekord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerDisconnect implements Listener, HashLoggable
{

	private Tekord cord;
	private final HashLogger logger;

	
	/**
	 * Creates a new instance of DisconnectEvent.
	 * Used to register the event.
	 * 
	 * @param	cord	Tekord instance
	 */
	public ListenerDisconnect(Tekord cord)
	{
		this.cord = cord;
		this.logger = this.cord.getHashLogger();
	}
	
	
	/**
	 * Called when a player logs out of the proxy.
	 */
	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent event)
	{
		final ProxiedPlayer player = event.getPlayer();

		this.logger.info(this, "\"" + player.getName() + "\" logged out, launching logout sequence...");

		this.cord.removePlayerManager(player);

		logger.info(this, "Logout sequence successfully executed for \"" + player.getName() + "\".");
	}
	
}
