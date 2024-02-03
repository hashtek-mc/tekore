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
import fr.hashtek.tekore.common.sql.SQLManager;
import fr.hashtek.tekore.common.sql.account.AccountCreator;
import fr.hashtek.tekore.common.sql.account.AccountGetter;

public class JoinEvent implements Listener, HashLoggable {

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		Tekore tekore = Tekore.getInstance();
		HashLogger logger = tekore.getHashLogger();
		
		Player player = event.getPlayer();
		PlayerData playerData = null;
		
		logger.info(this, player.getName() + " logged in, creating PlayerData...");
		
		try {
			playerData = new PlayerData(player);
		} catch (Exception exception) {
			logger.critical(this,
				"Failed to create a PlayerData for \"" + player.getUniqueId() + "\".",
				exception);
			return;
		}
		
		logger.info(this, "PlayerData successfully created. Now fetching data...");

		SQLManager sql = playerData.getSQLManager();
		
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
			player.kickPlayer("I am a poor dev that can't do his work properly."); // TODO: Change this !!
			return;
		}
		
		logger.info(this, "Data successfully fetched. Now adding to the main HashMap...");
		
		tekore.addPlayerData(player, playerData);
		
		logger.info(this, "Done.");
	}
	
}
