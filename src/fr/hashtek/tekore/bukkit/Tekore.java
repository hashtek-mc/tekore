package fr.hashtek.tekore.bukkit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import fr.hashtek.common.hashconfig.manager.HashConfig;
import io.github.cdimascio.dotenv.Dotenv;
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
import org.simpleyaml.configuration.file.YamlFile;

public class Tekore extends JavaPlugin implements HashLoggable {
	
	private static Tekore instance;
	private SQLManager sql;
	private HashLogger logger;
	
	private PluginManager pluginManager;
	private AccountManager accountManager;

	private HashConfig hashConfig;
	
	private final HashMap<Player, PlayerData> playersData = new HashMap<Player, PlayerData>();

	
	/**
	 * Called on server start.
	 */
	@Override
	public void onEnable()
	{
		instance = this;

		this.setupConfig();
		this.setupHashLogger();
		
		this.logger.info(this, "Starting Tekore...");

		this.setupManagers();
		this.registerListeners();
		this.registerCommands();
	
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
	 * Creates a new instance of HashConfig, to read configuration files.
	 * This function doesn't use HashLogger because it is called before the
	 * initialization of HashLogger. System.err.println is used instead.
	 */
	private void setupConfig()
	{
		String configFilename = "config.yml";

		try {
			this.hashConfig = new HashConfig(
				configFilename,
				this.getDataFolder().getPath() + "/" + configFilename,
				true
			);
		} catch (IOException exception) {
			System.err.println("Failed to read config file.");
			System.err.println(exception.getMessage());
			this.getServer().shutdown();
		}
	}

	/**
	 * Creates an instance of HashLogger.
	 * This HashLogger instance will be used server-wide, in every plugin that uses Tekore.
	 */
	private void setupHashLogger()
	{
		YamlFile config = this.hashConfig.getYaml();
		LogLevel logLevel = LogLevel.valueOf(config.getString("logger-level"));

		this.logger = new HashLogger(this, logLevel);
	}
	
	/**
	 * Setups all managers.
	 */
	private void setupManagers()
	{
		this.logger.info(this, "Setting up managers...");

		Dotenv sqlEnv = this.hashConfig.getEnv();

		try {
			this.sql = new SQLManager(
				this.logger,
				sqlEnv.get("DATABASE"),
				sqlEnv.get("HOST"),
				sqlEnv.get("USER"),
				sqlEnv.get("PASSWORD")
			);

			this.sql.connect();
		} catch (SQLException exception) {
			this.logger.fatal(this, "Failed to connect to the SQL database. Shutting down server.");
			this.getServer().shutdown();
			return;
		}

		this.pluginManager = this.getServer().getPluginManager();
		this.accountManager = new AccountManager(this.sql.getConnection());
		
		this.logger.info(this, "Managers set up!");
	}
	
	/**
	 * Registers all event listeners.
	 */
	private void registerListeners()
	{
		this.logger.info(this, "Registering listeners...");
		
		this.pluginManager.registerEvents(new JoinEvent(this), this);
		this.pluginManager.registerEvents(new QuitEvent(this), this);
		
		this.logger.info(this, "Listeners loaded!");
	}
	
	/**
	 * Registers all command listeners.
	 */
	private void registerCommands()
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
		this.playersData.put(player, playerData);
	}
	
	/**
	 * Remove a player's data from the main HashMap.
	 * 
	 * @param	player	Player
	 */
	public void removePlayerData(Player player)
	{
        this.playersData.remove(player);
	}
	
	
	/**
	 * @return	Tekore instance
	 */
	public static Tekore getInstance()
	{
		return instance;
	}
	
	/**
	 * @return	SQL manager
	 */
	public SQLManager getSQLManager()
	{
		return this.sql;
	}
	
	/**
	 * @return	Logger
	 */
	public HashLogger getHashLogger()
	{
		return this.logger;
	}
	
	/**
	 * @return	Account manager
	 */
	public AccountManager getAccountManager()
	{
		return this.accountManager;
	}

	/**
	 * @return	Configuration manager
	 */
	public HashConfig getHashConfig()
	{
		return this.hashConfig;
	}
	
	/**
	 * Returns the PlayerData linked to a Player.
	 * 
	 * @param	player	Player
	 * @return	Player's data
	 */
	public PlayerData getPlayerData(Player player)
	{
		return this.playersData.get(player);
	}
	
}
