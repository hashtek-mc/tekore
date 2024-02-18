package fr.hashtek.tekore.common.player;

import java.sql.Timestamp;

import fr.hashtek.tekore.bukkit.Tekore;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.sql.SQLManager;

/**
 * PlayerData aims to store some data of a player.
 * Some will go to the database, some others won't because it's useless.
 * This class is the main reason we decided to create a core.
 */
public class PlayerData {
	
	private Object player;

	private String uuid;
	private String username;
	
	private Timestamp createdAt;
	private Timestamp lastUpdate;
	
	private Rank rank;

	private final PlayerSettings playerSettings;

	private SQLManager sql;
	
	
	/**
	 * Creates an instance of PlayerData for an unknown-type player.
	 * Setups SQLManager based on player type (Bungee or Bukkit).
	 * 
	 * @param	player	Unknown-type player
	 */
	public PlayerData(Object player) throws NoClassDefFoundError
	{
		this.player = player;
		
		try {
			this.setBukkitPlayer();
		} catch (NoClassDefFoundError unused) {
			try {
				this.setBungeePlayer();
			} catch (NoClassDefFoundError ex) {
				throw new NoClassDefFoundError("Player type is not BukkitPlayer or BungeePlayer.");
			}
		}
		
		this.setUniqueId(this.uuid);

		this.playerSettings = new PlayerSettings();
	}
	
	/**
	 * Creates an empty instance of PlayerData based on a username.
	 * Mainly used for Neofetch command.
	 * 
	 * @param	username	Player's username
	 */
	public PlayerData(String username)
	{
		this.username = username;
		this.playerSettings = new PlayerSettings();
	}
	
	
	/**
	 * Fills up PlayerData using Bukkit's API (used only if player is Bukkit).
	 */
	private void setBukkitPlayer() throws NoClassDefFoundError
	{
		if (player instanceof org.bukkit.entity.Player) {
            this.uuid = ((org.bukkit.entity.Player) player).getUniqueId().toString();
			this.username = ((org.bukkit.entity.Player) player).getName();
			this.sql = Tekore.getInstance().getSQLManager();
		} else 
			throw new NoClassDefFoundError();
	}
	
	/**
	 * Fills up PlayerData using Bungeecord's API (used only if player is Bungee).
	 */
	private void setBungeePlayer() throws NoClassDefFoundError
	{
		if (player instanceof net.md_5.bungee.api.connection.ProxiedPlayer) {
            this.uuid = ((net.md_5.bungee.api.connection.ProxiedPlayer) player).getUniqueId().toString();
			this.username = ((net.md_5.bungee.api.connection.ProxiedPlayer) player).getName();
			this.sql = Tekord.getInstance().getSQLManager();
		} else
			throw new NoClassDefFoundError();
	}
	
	
	/**
	 * Returns player's UUID.
	 * 
	 * @return	Player's UUID
	 */
	public String getUniqueId()
	{
		return this.uuid;
	}
	
	/**
	 * Returns player's username.
	 * 
	 * @return	Player's username
	 */
	public String getUsername()
	{
		return this.username;
	}
	
	/**
	 * Returns player's first login.
	 * 
	 * @return	Player's first login
	 */
	public Timestamp getCreatedAt()
	{
		return this.createdAt;
	}
	
	/**
	 * Returns player's last login.
	 * 
	 * @return	Player's last login
	 */
	public Timestamp getLastUpdate()
	{
		return this.lastUpdate;
	}
	
	/**
	 * Returns player's rank.
	 * 
	 * @return	Player's rank.
	 */
	public Rank getRank()
	{
		return this.rank;
	}

	/**
	 * Returns player's settings.
	 *
	 * @return	Player's settings
	 */
	public PlayerSettings getPlayerSettings()
	{
		return this.playerSettings;
	}
	
	/**
	 * Returns associated SQL manager.
	 * 
	 * @return	Associated SQL manager
	 */
	public SQLManager getSQLManager()
	{
		return this.sql;
	}
	
	
	/**
	 * Sets player's UUID.
	 * 
	 * @param	uuid	UUID
	 */
	public void setUniqueId(String uuid)
	{
		this.uuid = uuid;
	}
	
	/**
	 * Sets player's username.
	 * 
	 * @param	username	Username
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	/**
	 * Sets player's first login.
	 * 
	 * @param	createdAt	Timestamp
	 */
	public void setCreatedAt(Timestamp createdAt)
	{
		this.createdAt = createdAt;
	}
	
	/**
	 * Sets player's last update.
	 * 
	 * @param	lastUpdate	Last update
	 */
	public void setLastUpdate(Timestamp lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}
	
	/**
	 * Sets player's rank.
	 * 
	 * @param	rank	Rank
	 */
	public void setRank(Rank rank)
	{
		this.rank = rank;
	}

}
