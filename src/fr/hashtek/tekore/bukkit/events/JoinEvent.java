package fr.hashtek.tekore.bukkit.events;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bukkit.Tekore;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.account.AccountManager;

public class JoinEvent implements Listener, HashLoggable {

	private final Tekore core;
	private final HashLogger logger;
	
	
	/**
	 * Creates a new instance of JoinEvent.
	 * 
	 * @param	core	Tekore instance
	 */
	public JoinEvent(Tekore core)
	{
		this.core = core;
		this.logger = core.getHashLogger();
	}
	
	
	/**
	 * Called when a player logs into the server.
	 * 
	 * TODO: Change Player#kickPlayer messages.
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		AccountManager accountManager = this.core.getAccountManager();
		
		Player player = event.getPlayer();
		PlayerData playerData;
		
		this.logger.info(this, "\"" + player.getName() + "\" logged in, launching login sequence...");
		
		try {
			playerData = new PlayerData(player);
		} catch (Exception exception) {
			this.logger.critical(this, "Failed to create a PlayerData for \"" + player.getName() + "\".", exception);
			player.kickPlayer("I am a poor dev that can't do his work properly.");
			return;
		}
		
		try {
			accountManager.getPlayerAccount(playerData);
		} catch (NoSuchFieldException unused) {
			this.logger.critical(this, "Account does not exist. Can't create one, that's Tekord job.");
			player.kickPlayer("I am a poor dev that can't do his work properly.");
			return;
		} catch (SQLException exception) {
			this.logger.critical(this, "Failed to get \"" + playerData.getUsername() + "\"'s account.", exception);
			player.kickPlayer("I am a poor dev that can't do his work properly.");
			return;
		}
		
		this.core.addPlayerData(player, playerData);
		
		logger.info(this, "Login sequence successfully executed for \"" + playerData.getUsername() + "\".");
	}
	
}
