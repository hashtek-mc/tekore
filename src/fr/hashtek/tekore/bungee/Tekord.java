package fr.hashtek.tekore.bungee;

import java.sql.SQLException;
import java.util.HashMap;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.hashlogger.LogLevel;
import fr.hashtek.tekore.bungee.commands.CheckCommand;
import fr.hashtek.tekore.bungee.events.LoginEvent;
import fr.hashtek.tekore.bungee.events.QuitEvent;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.SQLManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Tekord extends Plugin implements HashLoggable {
	
	private static Tekord instance;
	private SQLManager sql;
	private HashLogger logger;
	
	private PluginManager pluginManager;
	
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

		pluginManager = this.getProxy().getPluginManager();
		
		try {
			sql = new SQLManager(this.logger, "hashdb", "127.0.0.1", "root", "");
			sql.connect();
		} catch (SQLException exception) {
			BungeeCord.getInstance().stop();
			return;
		}
		
		setupListeners();
		setupCommands();
		
		logger.info(this, "Tekord loaded.");
	}
	
	/**
	 * Called on proxy stop.
	 */
	@Override
	public void onDisable()
	{
		logger.info(this, "Disabling Tekord...");
		
		try {
			sql.disconnect();
		} catch (SQLException exception) {
			// TODO: Do some things.
		}
		
		/* ... */
		
		logger.info(this, "Tekord disabled...");
	}
	
	/**
	 * Creates an instance of HashLogger.
	 */
	private void setupHashLogger()
	{
		this.logger = new HashLogger(this, LogLevel.DEBUG);
		this.logger.setShowTimestamp(true);
	}
	
	/**
	 * Setup all event listeners.
	 */
	private void setupListeners()
	{
		this.pluginManager.registerListener(this, new LoginEvent());
		this.pluginManager.registerListener(this, new QuitEvent());
	}
	
	/**
	 * Setup all command listeners.
	 */
	private void setupCommands()
	{
		this.pluginManager.registerCommand(this, new CheckCommand());
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
	
	
	public static Tekord getInstance()
	{
		return instance;
	}
	
	public SQLManager getSQLManager()
	{
		return this.sql;
	}
	
	public HashLogger getHashLogger()
	{
		return this.logger;
	}
	
	public PlayerData getPlayerData(ProxiedPlayer player)
	{
		return playerDatas.get(player);
	}
	
}
