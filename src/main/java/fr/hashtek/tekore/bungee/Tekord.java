package fr.hashtek.tekore.bungee;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import fr.hashtek.hashconfig.HashConfig;
import fr.hashtek.hasherror.HashError;
import fr.hashtek.tekore.bungee.command.hub.CommandHub;
import fr.hashtek.tekore.bungee.command.ping.CommandPing;
import org.simpleyaml.configuration.file.YamlFile;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.hashlogger.LogLevel;
import fr.hashtek.tekore.bungee.command.neofetch.CommandNeofetch;
import fr.hashtek.tekore.bungee.listener.ListenerDisconnect;
import fr.hashtek.tekore.bungee.listener.ListenerLogin;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.SQLManager;
import fr.hashtek.tekore.common.sql.account.AccountManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Tekord extends Plugin implements HashLoggable
{
	
	private static Tekord instance;
	private SQLManager sql;
	private HashLogger logger;

	private String serverVersion;
	
	private PluginManager pluginManager;
	private AccountManager accountManager;
	
	private HashConfig hashConfig;
	
	private final HashMap<ProxiedPlayer, PlayerData> playersData = new HashMap<ProxiedPlayer, PlayerData>();
	
	
	/**
	 * Called on proxy start.
	 */
	@Override
	public void onEnable()
	{
		instance = this;
		
		this.setupConfig();
		this.setupHashLogger();
		
		logger.info(this, "Starting Tekord...");

		this.loadConfigContent();
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
	 * Creates a new instance of HashConfig, to read configuration files.
	 * This function doesn't use HashLogger because it is called before the
	 * initialization of HashLogger. System.err.println is used instead.
	 */
	private void setupConfig()
	{
		String configFilename = "config.yml";
		
		try {
			this.hashConfig = new HashConfig(
				this.getClass(),
				configFilename,
				this.getDataFolder().getPath() + "/" + configFilename,
				true
			);
		} catch (IOException exception) {
			System.err.println("Failed to read config file.");
			System.err.println(exception.getMessage());
			this.getProxy().stop();
		}
	}
	
	/**
	 * Creates an instance of HashLogger.
	 * This HashLogger instance will be used server-wide, in every plugin that uses Tekord.
	 * This function doesn't use HashLogger because it is called before the
	 * initialization of HashLogger. System.err.println is used instead.
	 */
	private void setupHashLogger()
	{
		YamlFile config = this.hashConfig.getYaml();
		String loggerLevel = config.getString("loggerLevel");
		LogLevel logLevel;

		try {
			logLevel = LogLevel.valueOf(loggerLevel);
		} catch (IllegalArgumentException | NullPointerException exception) {
			System.err.println(exception instanceof NullPointerException ?
				"Field \"loggerLevel\" not found." :
				"\"" + loggerLevel + "\" is not a valid log level."
			);
			System.err.println("Can't initialize HashLogger. Stopping.");
			this.getProxy().stop();
			return;
		}

		this.logger = new HashLogger(this, logLevel);
	}

	/*
	 * Loads config content.
	 */
	private void loadConfigContent()
	{
		YamlFile config = this.hashConfig.getYaml();
		String serverVersion = config.getString("serverVersion");

		if (serverVersion == null) {
			HashError.CFG_KEY_NOT_FOUND
				.log(this.getHashLogger(), this, "serverVersion");
			serverVersion = "0.1-ALPHA";
		}
		this.serverVersion = serverVersion;
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
				sqlEnv.get("DB_DATABASE"),
				sqlEnv.get("DB_HOST"),
				sqlEnv.get("DB_PORT"),
				sqlEnv.get("DB_USER"),
				sqlEnv.get("DB_PASSWORD")
			);
			
			this.sql.connect();
		} catch (SQLException exception) {
			this.logger.fatal(this, "Failed to connect to the SQL database. Shutting down proxy.");
			this.getProxy().stop();
			return;
		}
		
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
		
		this.pluginManager.registerListener(this, new ListenerLogin(this));
		this.pluginManager.registerListener(this, new ListenerDisconnect(this));
	
		this.logger.info(this, "Listeners loaded!");
	}
	
	/**
	 * Setups all command listeners.
	 */
	private void setupCommands()
	{
		this.logger.info(this, "Registering commands...");

		this.pluginManager.registerCommand(this, new CommandNeofetch(this));
		this.pluginManager.registerCommand(this, new CommandHub(this));
		this.pluginManager.registerCommand(this, new CommandPing());

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
		this.playersData.put(player, playerData);
	}
	
	/**
	 * Remove a player's data from the main HashMap.
	 * 
	 * @param	player	Player
	 */
	public void removePlayerData(ProxiedPlayer player)
	{
		this.playersData.remove(player);
	}
	
	
	/**
	 * @return	Tekore instance
	 */
	public static Tekord getInstance()
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
	 * @return	Server version
	 */
	public String getServerVersion()
	{
		return this.serverVersion;
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
	public PlayerData getPlayerData(ProxiedPlayer player)
	{
		return this.playersData.get(player);
	}
	
}
