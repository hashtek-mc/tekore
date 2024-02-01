package fr.hashtek.tekore.bukkit;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.common.Exit;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.SQLManager;

public class Tekore extends JavaPlugin {
	
	@SuppressWarnings("unused")
	private static final String FILENAME = "Tekore.java";
	
	private static Tekore instance;
	private SQLManager sql;
	
	private HashMap<Player, PlayerData> playerDatas = new HashMap<Player, PlayerData>();

	@Override
	public void onEnable()
	{
		HashLogger.info("Starting Tekore...");
		
		instance = this;
		
		sql = new SQLManager("hashdb", "127.0.0.1", "root", "");
		if (sql.connect() == Exit.FAILURE)
			Bukkit.shutdown();
	
		HashLogger.info("Tekore loaded.");
	}
	
	@Override
	public void onDisable()
	{
		HashLogger.info("Disabling Tekore...");
		
		// ...
		
		HashLogger.info("Tekore disabled.");
	}
	
	public void addPlayerData(Player player, PlayerData playerData)
	{
		this.removePlayerData(player);
		this.playerDatas.put(player, playerData);
	}
	
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
	
	public PlayerData getPlayerData(Player player)
	{
		return playerDatas.get(player);
	}
	
}
