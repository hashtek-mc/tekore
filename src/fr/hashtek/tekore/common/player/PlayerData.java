package fr.hashtek.tekore.common.player;

import java.sql.SQLException;

import fr.hashtek.tekore.bukkit.Tekore;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.sql.SQLManager;
import fr.hashtek.tekore.common.sql.account.AccountGetter;

/**
 * PlayerData aims to store some data of a player.
 * Some will go to the database, some others won't because it's useless.
 * This class is the main reason we created a core.
 */
public class PlayerData {
	
	private Object player;

	private String uuid;
	private String formattedUuid;
	private String username;
	
	private PlayerProfile profile;

	private SQLManager sql;
	private boolean doesExists;
	
	
	/**
	 * Creates an instance of PlayerData for an unknown-type player.
	 * Setups SQLManager based on player type (Bungee or Bukkit).
	 * 
	 * @param	player	Unknown-type player
	 */
	public PlayerData(Object player) throws Exception
	{
		this.player = player;
		
		try {
			this.setBukkitPlayer();
		} catch (NoClassDefFoundError _unused) {
			try {
				this.setBungeePlayer();
			} catch (NoClassDefFoundError ex) {
				throw new Exception("Player type is not BukkitPlayer or BungeePlayer.");
			}
		}
		
		this.setUniqueId(this.uuid);
		
		this.profile = new PlayerProfile();
	}
	
	/**
	 * Creates an empty instance of PlayerData based on a username.
	 * Mainly used for "/check" bungee command.
	 * 
	 * @param	username	Player's username
	 */
	public PlayerData(String username)
	{
		this.username = username;
		
		this.profile = new PlayerProfile();
	}
	
	
	/**
	 * Fills up PlayerData using Bukkit's API (used only if player is Bukkit).
	 */
	private void setBukkitPlayer() throws NoClassDefFoundError
	{
		if (player instanceof org.bukkit.entity.Player) {
			this.player = (org.bukkit.entity.Player) player;
			this.uuid = ((org.bukkit.entity.Player) player).getUniqueId().toString();
			this.username = ((org.bukkit.entity.Player) player).getName();
			this.sql = Tekore.getInstance().getSQLManager();
		} else 
			throw new NoClassDefFoundError();
	}
	
	/**
	 * Fills up PlayerData using Bungee's API (used only if player is Bungee).
	 */
	private void setBungeePlayer() throws NoClassDefFoundError
	{
		if (player instanceof net.md_5.bungee.api.connection.ProxiedPlayer) {
			this.player = (net.md_5.bungee.api.connection.ProxiedPlayer) player;
			this.uuid = ((net.md_5.bungee.api.connection.ProxiedPlayer) player).getUniqueId().toString();
			this.username = ((net.md_5.bungee.api.connection.ProxiedPlayer) player).getName();
			this.sql = Tekord.getInstance().getSQLManager();
		} else
			throw new NoClassDefFoundError();
	}
	
	/**
	 * Sets a PlayerData's data from the SQL database.
	 */
	public void fetchDataFromSql(SQLManager sqlManager) throws NoSuchFieldException, SQLException
	{
		AccountGetter.getPlayerAccount(sqlManager.getConnection(), this);
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
	
	public boolean doesExists()
	{
		return this.doesExists;
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
	
	public void setExistence(boolean exists)
	{
		this.doesExists = exists;
	}

}
