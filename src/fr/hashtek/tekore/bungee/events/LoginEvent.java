package fr.hashtek.tekore.bungee.events;

import java.sql.SQLException;

import fr.hashtek.hashlogger.HashLoggable;
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

public class LoginEvent implements Listener, HashLoggable {
	
	/**
	 * Called when a player logs into the proxy (post-bungee, pre-bukkit).
	 */
	@EventHandler
	public void onPostLogin(PostLoginEvent event)
	{
		Tekord tekord = Tekord.getInstance();
		HashLogger logger = tekord.getHashLogger();
		
		ProxiedPlayer player = event.getPlayer();
		PlayerData playerData = null;
		SQLManager sql = null;
		
		try {
			playerData = new PlayerData(player);
		} catch (Exception exception) {
			logger.critical(this,
				"Failed to create a PlayerData for \"" + player.getUniqueId() + "\".",
				exception);
			return;
		}

		sql = playerData.getSQLManager();
		
		try {
			AccountGetter.getPlayerAccount(sql.getConnection(), playerData);
		} catch (NoSuchFieldException e) {
			try {
				AccountCreator.createPlayerAccount(sql.getConnection(), playerData);				
			} catch (SQLException exception) {
				logger.critical(this,
					"Failed to create an account for \"" + playerData.getUniqueId() + "\".",
					exception);
				return;
			}
		} catch (SQLException exception) {
			logger.critical(this, "Failed to get \"" + playerData.getUniqueId() + "\"'s account.", exception);
			player.disconnect(new TextComponent("I am a poor dev that can't do his work properly.")); // TODO: Change this !!
			return;
		}
		
		Tekord.getInstance().addPlayerData(player, playerData);
	}
	
}
