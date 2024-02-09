package fr.hashtek.tekore.bukkit.events;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bukkit.Tekore;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.account.AccountManager;

public class QuitEvent implements Listener, HashLoggable {

	private Tekore core;
	private HashLogger logger;
	
	
	/**
	 * Creates a new instance of QuitEvent.
	 * 
	 * @param	core	Tekore instance
	 */
	public QuitEvent(Tekore core)
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
		AccountManager accountManager = this.core.getAccountManager();
		
		Player player = event.getPlayer();
		PlayerData playerData = this.core.getPlayerData(player);
		
		this.logger.info(this, "\"" + player.getName() + "\" logged out, launching logout sequence...");
		
		try {
			accountManager.updatePlayerAccount(playerData);
		} catch (SQLException exception) {
			logger.critical(this, "Failed to update \"" + playerData.getUsername() + "\"'s account", exception);
		}
		
		this.core.removePlayerData(player);
		
		logger.info(this, "Logout sequence successfully executed for \"" + playerData.getUsername() + "\".");
	}
	
}
