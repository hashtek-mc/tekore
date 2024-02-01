package fr.hashtek.tekore.common.player;

import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bukkit.Tekore;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.Exit;
import fr.hashtek.tekore.common.sql.SQLManager;

public class PlayerData {
	
	private static final String FILENAME = "Tekord.java";

	private Object player;

	private String uuid;
	private String formattedUuid;
	private String username;
	
	private PlayerProfile profile;

	private SQLManager sql;
	
	
	public PlayerData(Object player)
	{
		HashLogger.debug(FILENAME, "Creating PlayerData for \"" + player + "\"...");
		
		this.player = player;
		
		if (setBukkitPlayer() == Exit.FAILURE)
			return;
		
		this.setUniqueId(this.uuid);
		
		this.profile = new PlayerProfile();
		
		HashLogger.debug(FILENAME, "Successfully created PlayerData for \"" + this.username + "\".");
	}
	
	
	private Exit setBukkitPlayer()
	{
		try {
			if (player instanceof org.bukkit.entity.Player) {
				HashLogger.debug(FILENAME, player + " is a BukkitPlayer");
				
				this.player = (org.bukkit.entity.Player) player;
				this.uuid = ((org.bukkit.entity.Player) player).getUniqueId().toString();
				this.username = ((org.bukkit.entity.Player) player).getName();
				this.sql = Tekore.getInstance().getSQLManager();
				
				return Exit.SUCCESS;
			} else 
				throw new NoClassDefFoundError();
		} catch (NoClassDefFoundError exception) {
			return setBungeePlayer();
		}
	}
	
	private Exit setBungeePlayer()
	{
		if (player instanceof net.md_5.bungee.api.connection.ProxiedPlayer) {
			HashLogger.debug(FILENAME, player + " is a BungeePlayer");
			
			this.player = (net.md_5.bungee.api.connection.ProxiedPlayer) player;
			this.uuid = ((net.md_5.bungee.api.connection.ProxiedPlayer) player).getUniqueId().toString();
			this.username = ((net.md_5.bungee.api.connection.ProxiedPlayer) player).getName();
			this.sql = Tekord.getInstance().getSQLManager();
			
			return Exit.SUCCESS;
		} else {
			HashLogger.err(FILENAME, "Failed creating PlayerData for \"" + player + "\"\n" +
				"Reason : Player type is not BukkitPlayer or BungeePlayer.");
			return Exit.FAILURE;
		}
	}
	
	
	public String getUniqueId()
	{
		return this.uuid;
	}
	
	public String getFormattedUniqueId()
	{
		return this.formattedUuid;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public PlayerProfile getProfile()
	{
		return this.profile;
	}
	
	public SQLManager getSQLManager()
	{
		return this.sql;
	}
	
	
	public void setUniqueId(String uuid)
	{
		this.uuid = uuid;
		this.formattedUuid = uuid.replaceAll("-", "");
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public void setProfile(PlayerProfile profile)
	{
		this.profile = profile;
	}

}
