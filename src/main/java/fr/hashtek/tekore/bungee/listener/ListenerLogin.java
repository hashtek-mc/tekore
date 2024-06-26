package fr.hashtek.tekore.bungee.listener;

import java.sql.SQLException;

import fr.hashtek.hasherror.HashError;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.common.sql.account.AccountManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerLogin implements Listener, HashLoggable
{
	
	private final Tekord cord;
	private final HashLogger logger;
	
	
	/**
	 * Creates a new instance of LoginEvent.
	 * 
	 * @param	cord	Tekord instance
	 */
	public ListenerLogin(Tekord cord)
	{
		this.cord = cord;
		this.logger = this.cord.getHashLogger();
	}
	
	
	/**
	 * Called when a player logs into the proxy (post-bungee, pre-bukkit).
	 */
	@EventHandler
	public void onPostLogin(PostLoginEvent event)
	{
		final AccountManager accountManager = this.cord.getAccountManager();
		
		final ProxiedPlayer player = event.getPlayer();
		final PlayerManager playerManager;
		final PlayerData playerData;
		
		this.logger.info(this, "\"" + player.getName() + "\" logged in, launching login sequence...");
		
		try {
			playerManager = new PlayerManager(player);
			playerData = playerManager.getData();
		} catch (Exception exception) {
			HashError.PD_UNKNOWN_ENTITY
				.log(this.cord.getHashLogger(), this, exception, player.getName())
				.kickPlayer(player);
			return;
		}

		try {
			accountManager.getPlayerAccount(playerManager);
		} catch (NoSuchFieldException unused) {
			try {
				accountManager.createPlayerAccount(playerData);
			} catch (SQLException exception) {
				HashError.PD_ACCOUNT_CREATION_FAIL
					.log(this.cord.getHashLogger(), this, exception, playerData.getUniqueId())
					.kickPlayer(player);
				return;
			}
		} catch (SQLException exception) {
			HashError.PD_ACCOUNT_FETCH_FAIL
				.log(this.cord.getHashLogger(), this, exception, playerData.getUniqueId())
				.kickPlayer(player);
			return;
		}
		
		this.cord.addPlayerManager(player, playerManager);

		playerManager.sendToServer("lobby01");

		logger.info(this, "Login sequence successfully executed for \"" + playerData.getUsername() + "\".");
	}
	
}
