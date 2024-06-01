package fr.hashtek.tekore.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bukkit.Tekore;

public class ListenerQuit implements Listener, HashLoggable
{

	private final Tekore core;
	private final HashLogger logger;
	
	
	/**
	 * Creates a new instance of QuitEvent.
	 * 
	 * @param	core	Tekore instance
	 */
	public ListenerQuit(Tekore core)
	{
		this.core = core;
		this.logger = this.core.getHashLogger();
	}
	
	
	/**
	 * Called when a player logs out of the server.
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		
		this.logger.info(this, "\"" + player.getName() + "\" logged out, launching logout sequence...");

		this.core.updatePlayerAccount(player, this);
		this.core.removePlayerManager(player);
		
		logger.info(this, "Logout sequence successfully executed for \"" + player.getName() + "\".");
	}
	
}
