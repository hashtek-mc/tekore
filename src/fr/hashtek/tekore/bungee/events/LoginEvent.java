package fr.hashtek.tekore.bungee.events;

import java.sql.SQLException;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.account.AccountManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginEvent implements Listener, HashLoggable {
	
	private final Tekord cord;
	private final HashLogger logger;
	
	
	/**
	 * Creates a new instance of LoginEvent.
	 * 
	 * @param	cord	Tekord instance
	 */
	public LoginEvent(Tekord cord)
	{
		this.cord = cord;
		this.logger = this.cord.getHashLogger();
	}
	
	
	/**
	 * Called when a player logs into the proxy (post-bungee, pre-bukkit).
	 * 
	 * TODO: Change disconnect messages.
	 */
	@EventHandler
	public void onPostLogin(PostLoginEvent event)
	{
		AccountManager accountManager = this.cord.getAccountManager();
		
		ProxiedPlayer player = event.getPlayer();
		PlayerData playerData;
		
		this.logger.info(this, "\"" + player.getName() + "\" logged in, launching login sequence...");
		
		try {
			playerData = new PlayerData(player);
		} catch (Exception exception) {
			this.logger.critical(this, "Failed to create a PlayerData for \"" + player.getName() + "\".", exception);
			player.disconnect(new TextComponent("I am a poor dev that can't do his work properly."));
			return;
		}

		try {
			accountManager.getPlayerAccount(playerData);
		} catch (NoSuchFieldException unused) {
			try {
				accountManager.createPlayerAccount(playerData);				
			} catch (SQLException exception) {
				this.logger.critical(this,"Failed to create an account for \"" + playerData.getUniqueId() + "\".", exception);
				player.disconnect(new TextComponent("I am a poor dev that can't do his work properly."));
				return;
			}
		} catch (SQLException exception) {
			this.logger.critical(this, "Failed to get \"" + playerData.getUsername() + "\"'s account.", exception);
			player.disconnect(new TextComponent("I am a poor dev that can't do his work properly."));
			return;
		}
		
		this.cord.addPlayerData(player, playerData);
		
		logger.info(this, "Login sequence successfully executed for \"" + playerData.getUsername() + "\".");
	}
	
}
