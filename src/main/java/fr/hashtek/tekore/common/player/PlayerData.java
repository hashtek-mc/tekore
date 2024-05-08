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
public class PlayerData
{
	
	private Object player;

	private String uuid;
	private String username;
	
	private Timestamp createdAt;
	private Timestamp lastUpdate;
	
	private Rank rank;

	private int coins;
	private int hashCoins;

	private final PlayerSettings playerSettings;
	private PlayerManager playerManager;

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
			} catch (NoClassDefFoundError unused1) {
				throw new NoClassDefFoundError("Neither Bukkit nor Bungee.");
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
			final Tekore core = Tekore.getInstance();

            this.uuid = ((org.bukkit.entity.Player) player).getUniqueId().toString();
			this.username = ((org.bukkit.entity.Player) player).getName();
			this.sql = core.getSQLManager();
			this.playerManager = new PlayerManager(core, (org.bukkit.entity.Player) this.player);
		} else
			throw new NoClassDefFoundError("Not Bukkit.");
	}
	
	/**
	 * Fills up PlayerData using BungeeCord's API (used only if player is Bungee).
	 */
	private void setBungeePlayer() throws NoClassDefFoundError
	{
		if (player instanceof net.md_5.bungee.api.connection.ProxiedPlayer) {
            this.uuid = ((net.md_5.bungee.api.connection.ProxiedPlayer) player).getUniqueId().toString();
			this.username = ((net.md_5.bungee.api.connection.ProxiedPlayer) player).getName();
			this.sql = Tekord.getInstance().getSQLManager();
			this.playerManager = null;
		} else
			throw new NoClassDefFoundError("Not Bungee.");
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
	 * @return	Player's settings
	 */
	public PlayerSettings getPlayerSettings()
	{
		return this.playerSettings;
	}

	/**
	 * @return	Player's manager
	 */
	public PlayerManager getPlayerManager()
	{
		return this.playerManager;
	}
	
	/**
	 * @return	Associated SQL manager
	 */
	public SQLManager getSQLManager()
	{
		return this.sql;
	}
	
	
	/**
	 * @param	uuid	UUID
	 */
	public void setUniqueId(String uuid)
	{
		this.uuid = uuid;
	}
	
	/**
	 * @param	username	Username
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	/**
	 * @param	createdAt	Timestamp
	 */
	public void setCreatedAt(Timestamp createdAt)
	{
		this.createdAt = createdAt;
	}
	
	/**
	 * @param	lastUpdate	Last update
	 */
	public void setLastUpdate(Timestamp lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}
	
	/**
	 * @param	rank	Rank
	 */
	public void setRank(Rank rank)
	{
		this.rank = rank;
	}

	/**
	 * @param	coins	Coins
	 */
	public void setCoins(int coins)
	{
		this.coins = coins;
	}

	/**
	 * @param	hashCoins	HashCoins
	 */
	public void setHashCoins(int hashCoins)
	{
		this.hashCoins = hashCoins;
	}

}