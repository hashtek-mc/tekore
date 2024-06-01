package fr.hashtek.tekore.common.player;

import java.sql.Timestamp;

import fr.hashtek.tekore.common.Rank;

/**
 * PlayerData aims to store some data of a player.
 * Some will go to the database, some others won't because it's useless.
 * This class is the main reason we decided to create a core.
 */
public class PlayerData
{
	
	private String uuid;
	private String username;
	
	private Timestamp createdAt;
	private Timestamp lastUpdate;
	
	private Rank rank;

	private int coins;
	private int hashCoins;


	/**
	 * Creates an instance of PlayerData for an unknown-type player.
	 * Setups SQLManager based on player type (Bungee or Bukkit).
	 * 
	 * @param	player	Unknown-type player
	 */
	public PlayerData(Object player) throws NoClassDefFoundError
	{
		try {
			this.setAsBukkitPlayer(player);
		} catch (NoClassDefFoundError unused) {
			try {
				this.setAsBungeePlayer(player);
			} catch (NoClassDefFoundError unused1) {
				throw new NoClassDefFoundError("Neither Bukkit nor Bungee.");
			}
		}
		
		this.setUniqueId(this.uuid);
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
	}
	
	
	/**
	 * Fills up PlayerData using Bukkit's API (used only if player is Bukkit).
	 */
	private void setAsBukkitPlayer(Object player) throws NoClassDefFoundError
	{
		if (!(player instanceof org.bukkit.entity.Player))
			throw new NoClassDefFoundError("Not Bukkit.");

		this.uuid = ((org.bukkit.entity.Player) player).getUniqueId().toString();
		this.username = ((org.bukkit.entity.Player) player).getName();
	}

	/**
	 * Fills up PlayerData using BungeeCord's API (used only if player is Bungee).
	 */
	private void setAsBungeePlayer(Object player) throws NoClassDefFoundError
	{
		if (!(player instanceof net.md_5.bungee.api.connection.ProxiedPlayer))
			throw new NoClassDefFoundError("Not Bungee.");

		this.uuid = ((net.md_5.bungee.api.connection.ProxiedPlayer) player).getUniqueId().toString();
		this.username = ((net.md_5.bungee.api.connection.ProxiedPlayer) player).getName();
	}
	
	
	/**
	 * @return	Player's UUID
	 */
	public String getUniqueId()
	{
		return this.uuid;
	}
	
	/**
	 * @return	Player's username
	 */
	public String getUsername()
	{
		return this.username;
	}
	
	/**
	 * @return	Player's first login timestamp
	 */
	public Timestamp getCreatedAt()
	{
		return this.createdAt;
	}
	
	/**
	 * @return	Player's last login timestamp
	 */
	public Timestamp getLastUpdate()
	{
		return this.lastUpdate;
	}
	
	/**
	 * @return	Player's rank.
	 */
	public Rank getRank()
	{
		return this.rank;
	}

	/**
	 * @return	Player's coins.
	 */
	public int getCoins()
	{
		return this.coins;
	}

	/**
	 * @return	Player's HashCoins.
	 */
	public int getHashCoins()
	{
		return this.hashCoins;
	}


	/**
	 * @param	uuid	Player's new UUID
	 */
	public void setUniqueId(String uuid)
	{
		this.uuid = uuid;
	}
	
	/**
	 * @param	username	Player's new username
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	/**
	 * @param	createdAt	Player's new account creation timestamp
	 */
	public void setCreatedAt(Timestamp createdAt)
	{
		this.createdAt = createdAt;
	}
	
	/**
	 * @param	lastUpdate	Player's new account last update timestamp
	 */
	public void setLastUpdate(Timestamp lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}
	
	/**
	 * @param	rank	Player's rank
	 */
	public void setRank(Rank rank)
	{
		this.rank = rank;
	}

	/**
	 * @param	coins	Player's new amount of coins
	 */
	public void setCoins(int coins)
	{
		this.coins = coins;
	}

	/**
	 * @param	hashCoins	Player's new amount of HashCoins
	 */
	public void setHashCoins(int hashCoins)
	{
		this.hashCoins = hashCoins;
	}

}
