package fr.hashtek.tekore.bungee.events;

import java.sql.SQLException;

import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.SQLManager;
import fr.hashtek.tekore.common.sql.account.AccountCreator;
import fr.hashtek.tekore.common.sql.account.AccountGetter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnLogin implements Listener {
	
	private static final String FILENAME = "Tekord.java";
	

	@EventHandler
	public void onPostLogin(PostLoginEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		PlayerData playerData = new PlayerData(player);
		SQLManager sql = playerData.getSQLManager();
		
		try {
			AccountGetter.getPlayerAccount(sql.getConnection(), playerData);
		} catch (NoSuchFieldException exception) {
			AccountCreator.createPlayerAccount(sql.getConnection(), playerData);
		} catch (SQLException exception) {
			HashLogger.err(FILENAME, "Error", exception);
			player.disconnect(new TextComponent("Error"));
			return;
		}
		
		Tekord.getInstance().addPlayerData(player, playerData);
	}
	
}
