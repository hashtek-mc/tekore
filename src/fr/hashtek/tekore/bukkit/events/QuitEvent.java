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
import fr.hashtek.tekore.common.sql.account.AccountUpdater;

public class QuitEvent implements Listener, HashLoggable {

	/**
	 * Called when a player logs out of the server.
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		Tekore tekore = Tekore.getInstance();
		HashLogger logger = tekore.getHashLogger();
		
		Player player = event.getPlayer();
		PlayerData playerData = tekore.getPlayerData(player);
		
		try {
			AccountUpdater.updatePlayerAccount(tekore.getSQLManager().getConnection(), playerData);
		} catch (SQLException exception) {
			logger.critical(this, "Failed to update \"" + playerData.getUsername() + "\"'s account", exception);
		}
		tekore.removePlayerData(player);
	}
	
}
