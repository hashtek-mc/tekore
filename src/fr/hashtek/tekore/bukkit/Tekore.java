package fr.hashtek.tekore.bukkit;

import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.hashlogger.LogLevel;
import fr.hashtek.tekore.bukkit.events.JoinEvent;
import fr.hashtek.tekore.bukkit.events.QuitEvent;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.SQLManager;
import fr.hashtek.tekore.common.sql.account.AccountManager;

public class Tekore extends JavaPlugin implements HashLoggable {
	
	private static Tekore instance;
	private SQLManager sql;
	private HashLogger logger;
	
	private PluginManager pluginManager;
	private AccountManager accountManager;
	
	private HashMap<Player, PlayerData> playerDatas = new HashMap<Player, PlayerData>();

	
	/**
	 * Called on server start.
	 */
	@Override
	public void onEnable()
	{
		instance = this;
		this.setupHashLogger();
		
		this.logger.info(this, "Starting Tekore...");
		
		try {
			this.sql = new SQLManager(this.logger, "hashtekdb", "127.0.0.1", "root", "");
			this.sql.connect();
		} catch (SQLException exception) {
			this.logger.fatal(this, "Failed to connect to the SQL database. Shutting down server.");
			this.getServer().shutdown();
			return;
		}
		
		this.setupManagers();
		this.setupListeners();
		this.setupCommands();
	
		this.logger.info(this, "Tekore loaded.");
	}
	
	/**
	 * Called on server stop.
	 */
	@Override
	public void onDisable()
	{
		this.logger.info(this, "Disabling Tekore...");
		
		for (Player player: this.getServer().getOnlinePlayers())
			player.kickPlayer("Nous revenons dans quelques minutes...");
		
		try {
			this.sql.disconnect();
		} catch (SQLException exception) {
			this.logger.critical(this, "Failed to disconnect from the database.", exception);
		}
		
		this.logger.info(this, "Tekore disabled.");
	}
	
	/**
	 * Creates an instance of HashLogger.
	 * This HashLogger instance will be used server-wide, in every plugin that uses Tekore.
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
		
		this.pluginManager = this.getServer().getPluginManager();
		this.accountManager = new AccountManager(this.sql.getConnection());
		
		this.logger.info(this, "Managers set up!");
	}
	
	/**
	 * Registers all event listeners.
	 */
	private void setupListeners()
	{
		this.logger.info(this, "Registering listeners...");
		
		this.pluginManager.registerEvents(new JoinEvent(this), this);
		this.pluginManager.registerEvents(new QuitEvent(this), this);
		
		this.logger.info(this, "Listeners loaded!");
	}
	
	/**
	 * Registers all command listeners.
	 */
	private void setupCommands()
	{
		this.logger.info(this, "Registering commands...");
		
		/* ... */

		this.logger.info(this, "Commands registered!");
	}
	
	/**
	 * Adds a player's data to the main HashMap.
	 * 
	 * @param	player		Player
	 * @param	playerData	Player's data
	 */
	public void addPlayerData(Player player, PlayerData playerData)
	{
		this.removePlayerData(player);
		this.playerDatas.put(player, playerData);
	}
	
	/**
	 * Remove a player's data from the main HashMap.
	 * 
	 * @param	player	Player
	 */
	public void removePlayerData(Player player)
	{
		if (this.playerDatas.containsKey(player))
			this.playerDatas.remove(player);
	}
	
	
	/**
	 * Returns the instance of Tekore.
	 * 
	 * @return	Tekore instance
	 */
	public static Tekore getInstance()
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
	public PlayerData getPlayerData(Player player)
	{
		return this.playerDatas.get(player);
	}
	
}
