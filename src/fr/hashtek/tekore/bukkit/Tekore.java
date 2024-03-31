package fr.hashtek.tekore.bukkit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import fr.hashtek.hashconfig.HashConfig;
import fr.hashtek.hasherror.HashError;
import fr.hashtek.tekore.bukkit.command.CommandWhoAmI;
import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.sql.rank.RankGetter;
import io.github.cdimascio.dotenv.Dotenv;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.hashlogger.LogLevel;
import fr.hashtek.tekore.bukkit.listener.ListenerJoin;
import fr.hashtek.tekore.bukkit.listener.ListenerQuit;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.SQLManager;
import fr.hashtek.tekore.common.sql.account.AccountManager;
import org.simpleyaml.configuration.file.YamlFile;

public class Tekore extends JavaPlugin implements HashLoggable
{
	
	private static Tekore instance;
	private SQLManager sql;
	private HashLogger logger;

	private String serverVersion;
	private String serverIp;
	
	private PluginManager pluginManager;
	private AccountManager accountManager;

	private HashConfig hashConfig;
	
	private final HashMap<Player, PlayerData> playersData = new HashMap<Player, PlayerData>();

	private ArrayList<Rank> ranks;

	
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

		this.loadConfigContent();
		this.setupManagers();
		this.fetchRanks();
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
				this.getClass(),
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
			this.getServer().shutdown();
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

		String serverVersion = config.getString("serverInfo.version");
		String serverIp = config.getString("serverInfo.ip");

		if (serverVersion == null) {
			HashError.CFG_KEY_NOT_FOUND
				.log(this.getHashLogger(), this, "serverInfo.version");
			serverVersion = "0.1-ALPHA";
		}

		if (serverIp == null) {
			HashError.CFG_KEY_NOT_FOUND
				.log(this.getHashLogger(), this, "serverInfo.ip");
			serverVersion = "mc.hashtek.fr";
		}

		this.serverVersion = serverVersion;
		this.serverIp = serverIp;
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
				sqlEnv.get("PORT"),
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
	 * Fetches all the ranks from the database and stores them in {@link Tekore#ranks}.
	 */
	private void fetchRanks()
	{
		this.logger.info(this, "Fetching ranks from the database...");

		RankGetter rankGetter = new RankGetter(this.sql.getConnection());

		try {
			this.ranks = rankGetter.getRanks();
			ranks.sort((Rank rank1, Rank rank2) -> rank2.getPower() - rank1.getPower());
		} catch (SQLException exception) {
			HashError.DB_NO_RANK_FOUND.log(this.logger, this, exception);
			this.getServer().shutdown();
			return;
		}

		this.logger.info(this, this.ranks.size() + " ranks fetched from the database.");
	}
	
	/**
	 * Registers all event listeners.
	 */
	private void registerListeners()
	{
		this.logger.info(this, "Registering listeners...");
		
		this.pluginManager.registerEvents(new ListenerJoin(this), this);
		this.pluginManager.registerEvents(new ListenerQuit(this), this);
		
		this.logger.info(this, "Listeners loaded!");
	}
	
	/**
	 * Registers all command listeners.
	 */
	private void registerCommands()
	{
		this.logger.info(this, "Registering commands...");

		getCommand("whoami").setExecutor(new CommandWhoAmI(this));

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
	 * Saves a player's data to the database (updates it).
	 * This function must be only used in static methods.
	 * Prefer using {@link Tekore#updatePlayerData(Player, HashLoggable)}
	 *
	 * @param	player	Player
	 * @param	author	Author's filename
	 */
	public void updatePlayerData(Player player, String author)
	{
		PlayerData playerData = this.getPlayerData(player);

        try {
            this.getAccountManager().updatePlayerAccount(playerData);
        } catch (SQLException exception) {
			HashError.PD_UPDATE_FAIL
				.log(this.logger, this, exception, playerData.getUniqueId(), author)
				.sendToPlayer(player);
        }
    }

	/**
	 * Saves a player's data to the database (updates it).
	 *
	 * @param	player	Player
	 * @param	author	Author's filename
	 */
	public void updatePlayerData(Player player, HashLoggable author)
	{
		this.updatePlayerData(player, author.getClass().getSimpleName());
	}

	/**
	 * @return	All existing ranks
	 */
	public ArrayList<Rank> getRanks()
	{
		return this.ranks;
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
	 * @return	Server version
	 */
	public String getServerVersion()
	{
		return this.serverVersion;
	}

	/**
	 * @return	Server IP
	 */
	public String getServerIp()
	{
		return this.serverIp;
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
