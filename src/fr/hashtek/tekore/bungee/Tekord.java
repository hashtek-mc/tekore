package fr.hashtek.tekore.bungee;

import java.sql.SQLException;
import java.util.HashMap;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.hashlogger.LogLevel;
import fr.hashtek.tekore.bungee.commands.NeofetchCommand;
import fr.hashtek.tekore.bungee.events.DisconnectEvent;
import fr.hashtek.tekore.bungee.events.LoginEvent;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.SQLManager;
import fr.hashtek.tekore.common.sql.account.AccountManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Tekord extends Plugin implements HashLoggable {
	
	private static Tekord instance;
	private SQLManager sql;
	private HashLogger logger;
	
	private PluginManager pluginManager;
	private AccountManager accountManager;
	
	private HashMap<ProxiedPlayer, PlayerData> playerDatas = new HashMap<ProxiedPlayer, PlayerData>();
	
	
	/**
	 * Called on proxy start.
	 */
	@Override
	public void onEnable()
	{
		instance = this;
		this.setupHashLogger();
		
		logger.info(this, "Starting Tekord...");
		
		try {
			this.sql = new SQLManager(this.logger, "hashtekdb", "127.0.0.1", "root", "");
			this.sql.connect();
		} catch (SQLException exception) {
			this.logger.fatal(this, "Failed to connect to the SQL database. Shutting down proxy.");
			this.getProxy().stop();
			return;
		}
		
		this.setupManagers();
		this.setupListeners();
		this.setupCommands();
		
		logger.info(this, "Tekord loaded.");
	}
	
	/**
	 * Called on proxy stop.
	 */
	@Override
	public void onDisable()
	{
		this.logger.info(this, "Disabling Tekord...");
		
		try {
			this.sql.disconnect();
		} catch (SQLException exception) {
			this.logger.critical(this, "Failed to disconnect from the database.", exception);
		}
		
		this.logger.info(this, "Tekord disabled.");
	}
	
	/**
	 * Creates an instance of HashLogger.
	 * This HashLogger instance will be used server-wide, in every plugin that uses Tekord.
	 * 
	 * TODO: Read log level from the configuration file.
	 */
	private void setupHashLogger()
	{
		this.logger = new HashLogger(this, LogLevel.DEBUG);
	}
	
	/**
	 * Setups all managers.
	 */
	private void setupManagers()
	{
		this.logger.info(this, "Setting up managers...");
		
		this.pluginManager = this.getProxy().getPluginManager();
		this.accountManager = new AccountManager(this.sql.getConnection());
		
		this.logger.info(this, "Managers set up!");
	}
	
	/**
	 * Setups all event listeners.
	 */
	private void setupListeners()
	{
		this.logger.info(this, "Registering listeners...");
		
		this.pluginManager.registerListener(this, new LoginEvent(this));
		this.pluginManager.registerListener(this, new DisconnectEvent(this));
	
		this.logger.info(this, "Listeners loaded!");
	}
	
	/**
	 * Setups all command listeners.
	 */
	private void setupCommands()
	{
		this.logger.info(this, "Registering commands...");
		
		this.pluginManager.registerCommand(this, new NeofetchCommand());
	
		this.logger.info(this, "Commands registered!");
	}
	
	/**
	 * Adds a player's data to the main HashMap.
	 * 
	 * @param	player		Player
	 * @param	playerData	Player's data
	 */
	public void addPlayerData(ProxiedPlayer player, PlayerData playerData)
	{
		this.removePlayerData(player);
		this.playerDatas.put(player, playerData);
	}
	
	/**
	 * Remove a player's data from the main HashMap.
	 * 
	 * @param	player	Player
	 */
	public void removePlayerData(ProxiedPlayer player)
	{
		if (this.playerDatas.containsKey(player))
			this.playerDatas.remove(player);
	}
	
	
	/**
	 * Returns the instance of Tekore.
	 * 
	 * @return	Tekore instance
	 */
	public static Tekord getInstance()
	{
		return instance;
	}
	
	/**
	 * Returns the SQL manager.
	 * 
	 * @return	SQL manager
	 */
	public SQLManager getSQLManager()
	{
		return this.sql;
	}
	
	/**
	 * Returns the logger
	 * 
	 * @return	Logger
	 */
	public HashLogger getHashLogger()
	{
		return this.logger;
	}
	
	/**
	 * Returns the account manager
	 * 
	 * @return	Account manager
	 */
	public AccountManager getAccountManager()
	{
		return this.accountManager;
	}
	
	/**
	 * Returns the PlayerData linked to a Player.
	 * 
	 * @param	player	Player
	 * @return	Player's data
	 */
	public PlayerData getPlayerData(ProxiedPlayer player)
	{
		return this.playerDatas.get(player);
	}
	
}
