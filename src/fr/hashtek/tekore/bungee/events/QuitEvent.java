package fr.hashtek.tekore.bungee.events;

import java.sql.SQLException;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.account.AccountUpdater;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitEvent implements Listener, HashLoggable {
	
	/**
	 * Called when a player logs out of the proxy.
	 */
	@EventHandler
	public void onQuit(PlayerDisconnectEvent event)
	{
		Tekord tekord = Tekord.getInstance();
		HashLogger logger = tekord.getHashLogger();
		
		ProxiedPlayer player = event.getPlayer();
		PlayerData playerData = tekord.getPlayerData(player);
		
		try {
			AccountUpdater.updatePlayerAccount(tekord.getSQLManager().getConnection(), playerData);
		} catch (SQLException exception) {
			logger.critical(this, "Failed to update \"" + playerData.getUsername() + "\"'s account", exception);
		}
		tekord.removePlayerData(player);
	}
	
}
