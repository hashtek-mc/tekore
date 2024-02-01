package fr.hashtek.tekore.bungee.events;

import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.account.AccountUpdater;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnQuit implements Listener {
	
	@SuppressWarnings("unused")
	private static final String FILENAME = "Tekord.java";
	
	
	@EventHandler
	public void onQuit(PlayerDisconnectEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		Tekord tekord = Tekord.getInstance();
		PlayerData playerData = tekord.getPlayerData(player);
		
		AccountUpdater.updatePlayerAccount(tekord.getSQLManager().getConnection(), playerData);
		tekord.removePlayerData(player);
	}
	
}
