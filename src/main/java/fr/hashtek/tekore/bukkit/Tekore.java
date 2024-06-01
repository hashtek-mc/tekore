package fr.hashtek.tekore.bukkit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import fr.hashtek.hashconfig.HashConfig;
import fr.hashtek.hasherror.HashError;
import fr.hashtek.tekore.bukkit.command.logs.CommandLogs;
import fr.hashtek.tekore.bukkit.command.whoami.CommandWhoAmI;
import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerManager;
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
import fr.hashtek.tekore.common.sql.SQLManager;
import fr.hashtek.tekore.common.sql.account.AccountManager;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.simpleyaml.configuration.file.YamlFile;

public class Tekore extends JavaPlugin implements HashLoggable, PluginMessageListener
{
	
	private static Tekore instance;
	private SQLManager sql;
	private HashLogger logger;

	private String serverVersion;
	private String serverIp;
	
	private PluginManager pluginManager;
	private AccountManager accountManager;

	private HashConfig hashConfig;
	
	private final HashMap<Player, PlayerManager> playersManager = new HashMap<Player, PlayerManager>();

	private ArrayList<Rank> ranks;

	private final List<String> messagingChannels = Arrays.asList(
		"BungeeCord"
	);

	
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
		this.loadMessenger();
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

		this.unloadMessenger();

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
			serverIp = "mc.hashtek.fr";
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
				sqlEnv.get("DB_DATABASE"),
				sqlEnv.get("DB_HOST"),
				sqlEnv.get("DB_PORT"),
				sqlEnv.get("DB_USER"),
				sqlEnv.get("DB_PASSWORD")
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
	 * Loads plugin's messenger.
	 */
	private void loadMessenger()
	{
		this.logger.info(this, "Loading messenger...");

		for (String channel : messagingChannels) {
			this.getServer().getMessenger().registerOutgoingPluginChannel(this, channel);
			this.getServer().getMessenger().registerIncomingPluginChannel(this, channel, this);
			this.logger.info(this, "\tChannel \"" + channel + "\" registered!");
		}

		this.logger.info(this, "Messenger loaded!");
	}

	/**
	 * Unloads plugin's messenger.
	 */
	private void unloadMessenger()
	{
		this.logger.info(this, "Unloading messenger...");

		this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
		this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

		this.logger.info(this, "Messenger unloaded.");
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

		this.getCommand("whoami").setExecutor(new CommandWhoAmI(this));
		this.getCommand("logs").setExecutor(new CommandLogs(this));

		this.logger.info(this, "Commands registered!");
	}

	/**
	 * Called when Tekore receives a message through messenger.
	 * TODO: Finish this function.
	 *
	 * @param	channel		Channel that the message was sent through.
	 * @param	player		Source of the message.
	 * @param	message		The raw message that was sent.
	 */
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		// ...
	}
	
	/**
	 * Adds a player's manager to the main HashMap.
	 * 
	 * @param	player			Player
	 * @param	playerManager	Player's manager
	 */
	public void addPlayerManager(Player player, PlayerManager playerManager)
	{
		this.removePlayerManager(player);
		this.playersManager.put(player, playerManager);
	}
	
	/**
	 * Remove a player's manager from the main HashMap.
	 * 
	 * @param	player	Player
	 */
	public void removePlayerManager(Player player)
	{
        this.playersManager.remove(player);
	}

	/**
	 * Saves a player's manager to the database (updates it).
	 * This function must be only used in static methods.
	 * Prefer using {@link Tekore#updatePlayerAccount(Player, HashLoggable)}
	 *
	 * @param	player	Player
	 * @param	author	Author's filename
	 */
	public void updatePlayerAccount(Player player, String author)
	{
		final PlayerManager playerManager = this.getPlayerManager(player);

        try {
            this.getAccountManager().updatePlayerAccount(playerManager);
        } catch (SQLException exception) {
			HashError.PD_UPDATE_FAIL
				.log(this.logger, this, exception, playerManager.getData().getUniqueId(), author)
				.sendToPlayer(player);
        }
    }

	/**
	 * Saves a player's manager to the database (updates it).
	 *
	 * @param	player	Player
	 * @param	author	Author's filename
	 */
	public void updatePlayerAccount(Player player, HashLoggable author)
	{
		this.updatePlayerAccount(player, author.getClass().getSimpleName());
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
	 * Returns the PlayerManager linked to a Player.
	 * 
	 * @param	player	Player
	 * @return	Player's manager
	 */
	public PlayerManager getPlayerManager(Player player)
	{
		return this.playersManager.get(player);
	}

	/* Move the functions below in a dedicated class or something. */

	/**
	 * @return	Every online player of the current server.
	 */
	public Collection<? extends Player> getOnlinePlayers()
	{
		return this.getServer().getOnlinePlayers();
	}

	/**
	 * @return	Number of players connected to the current server.
	 */
	public int getLocalNumberOfPlayers()
	{
		return this.getServer().getOnlinePlayers().size();
	}

	/**
	 * TODO: Finish this function.
	 *       (maybe use plugin messaging?)
	 *
	 * @return	Number of players connected to the entire proxy.
	 * @deprecated
	 */
	public int getTotalNumberOfPlayers()
	{
		return 0;
	}

}
