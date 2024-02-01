package fr.hashtek.tekore.bungee;

import java.util.HashMap;

import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bungee.events.OnLogin;
import fr.hashtek.tekore.bungee.events.OnQuit;
import fr.hashtek.tekore.common.Exit;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.SQLManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Tekord extends Plugin {
	
	@SuppressWarnings("unused")
	private static final String FILENAME = "Tekord.java";

	private static Tekord instance;
	private SQLManager sql;
	
	PluginManager pluginManager;
	
	private HashMap<ProxiedPlayer, PlayerData> playerDatas = new HashMap<ProxiedPlayer, PlayerData>();
	
	@Override
	public void onEnable()
	{
		HashLogger.info("Starting Tekord...");
		
		instance = this;
		pluginManager = this.getProxy().getPluginManager();
				
		sql = new SQLManager("hashdb", "127.0.0.1", "root", "");
		if (sql.connect() == Exit.FAILURE ||
			setupListeners() == Exit.FAILURE ||
			setupCommands() == Exit.FAILURE
		)
			BungeeCord.getInstance().stop();			
		
		HashLogger.info("Tekord loaded.");
	}
	
	@Override
	public void onDisable()
	{
		HashLogger.info("Disabling Tekord...");
		
		sql.disconnect(); // TODO: Check return value.
		
		/* ... */
		
		HashLogger.info("Tekord disabled...");
	}
	
	private Exit setupListeners()
	{
		pluginManager.registerListener(this, new OnLogin());
		pluginManager.registerListener(this, new OnQuit());
		
		return Exit.SUCCESS;
	}
	
	private Exit setupCommands()
	{
		return Exit.SUCCESS;
	}
	
	public static Tekord getInstance()
	{
		return instance;
	}
	
	public SQLManager getSQLManager()
	{
		return sql;
	}
	
	public PlayerData getPlayerData(ProxiedPlayer player)
	{
		return playerDatas.get(player);
	}
	
	public void addPlayerData(ProxiedPlayer player, PlayerData playerData)
	{
		this.removePlayerData(player);
		this.playerDatas.put(player, playerData);
	}
	
	public void removePlayerData(ProxiedPlayer player)
	{
		if (this.playerDatas.containsKey(player))
			this.playerDatas.remove(player);
	}
	
}
