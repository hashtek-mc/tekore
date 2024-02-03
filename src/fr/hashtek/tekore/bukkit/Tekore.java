package fr.hashtek.tekore.bukkit;

import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
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

public class Tekore extends JavaPlugin implements HashLoggable {
	
	private static Tekore instance;
	private SQLManager sql;
	private HashLogger logger;
	
	private PluginManager pluginManager;
	
	private HashMap<Player, PlayerData> playerDatas = new HashMap<Player, PlayerData>();

	/**
	 * Called on server start.
	 */
	@Override
	public void onEnable()
	{
		instance = this;
		this.setupHashLogger();
		
		logger.info(this, "Starting Tekore...");
		
		this.pluginManager = this.getServer().getPluginManager();
		
		try {			
			sql = new SQLManager(this.logger, "hashdb", "127.0.0.1", "root", "");
			sql.connect();
		} catch (SQLException exception) {
			Bukkit.shutdown();
			return;
		}
		
		this.setupListeners();
		this.setupCommands();
	
		logger.info(this, "Tekore loaded.");
	}
	
	/**
	 * Called on server stop.
	 */
	@Override
	public void onDisable()
	{
		logger.info(this, "Disabling Tekore...");
		
		// ...
		
		logger.info(this, "Tekore disabled.");
	}
	
	/**
	 * Creates an instance of HashLogger.
	 * This HashLogger instance will be used server-wide, in every plugin that uses Tekore.
	 */
	private void setupHashLogger()
	{
		this.logger = new HashLogger(this, LogLevel.DEBUG);
		this.logger.setShowTimestamp(true);
	}
	
	/**
	 * Setups all event listeners.
	 */
	private void setupListeners()
	{
		this.pluginManager.registerEvents(new JoinEvent(), this);
		this.pluginManager.registerEvents(new QuitEvent(), this);
	}
	
	/**
	 * Setups all command listeners.
	 */
	private void setupCommands()
	{
		
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
	
	
	public static Tekore getInstance()
	{
		return instance;
	}
	
	public SQLManager getSQLManager()
	{
		return sql;
	}
	
	public HashLogger getHashLogger()
	{
		return this.logger;
	}
	
	public PlayerData getPlayerData(Player player)
	{
		return playerDatas.get(player);
	}
	
}
