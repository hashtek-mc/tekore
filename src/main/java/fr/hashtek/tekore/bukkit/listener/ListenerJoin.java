package fr.hashtek.tekore.bukkit.listener;

import java.sql.SQLException;

import fr.hashtek.hasherror.HashError;
import fr.hashtek.tekore.common.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bukkit.Tekore;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.account.AccountManager;

public class ListenerJoin implements Listener, HashLoggable
{

	private final Tekore core;
	private final HashLogger logger;
	
	
	/**
	 * Creates a new instance of JoinEvent.
	 * 
	 * @param	core	Tekore instance
	 */
	public ListenerJoin(Tekore core)
	{
		this.core = core;
		this.logger = core.getHashLogger();
	}
	
	
	/**
	 * Called when a player logs into the server.
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		final AccountManager accountManager = this.core.getAccountManager();
		
		final Player player = event.getPlayer();
		final PlayerManager playerManager;
		final PlayerData playerData;
		
		this.logger.info(this, "\"" + player.getName() + "\" logged in, launching login sequence...");
		
		try {
			playerManager = new PlayerManager(player);
			playerData = playerManager.getData();
		} catch (Exception exception) {
			HashError.PD_UNKNOWN_ENTITY
				.log(this.logger, this, exception, player.getName())
				.kickPlayer(player, player.getName());
			return;
		}
		
		try {
			accountManager.getPlayerAccount(playerManager);
		} catch (NoSuchFieldException unused) {
			HashError.PD_CANNOT_CREATE_ACCOUNT
				.log(this.logger, this, playerData.getUniqueId())
				.kickPlayer(player);
			return;
		} catch (SQLException exception) {
			HashError.PD_ACCOUNT_FETCH_FAIL
				.log(this.logger, this, playerData.getUniqueId())
				.kickPlayer(player);
			return;
		}
		
		this.core.addPlayerManager(player, playerManager);
		
		logger.info(this, "Login sequence successfully executed for \"" + playerData.getUsername() + "\".");
	}
	
}
