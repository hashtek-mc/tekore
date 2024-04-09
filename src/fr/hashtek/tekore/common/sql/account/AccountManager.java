package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.SQLException;

import fr.hashtek.tekore.common.player.PlayerData;

public class AccountManager
{

    private final AccountCreator accountCreator;
	private final AccountGetter accountGetter;
	private final AccountUpdater accountUpdater;
	
	
	/**
	 * Creates a new instance of AccountManager.
	 * 
	 * @param	sqlConnection	SQL connection
	 */
	public AccountManager(Connection sqlConnection)
	{
        this.accountCreator = new AccountCreator(sqlConnection);
		this.accountGetter = new AccountGetter(sqlConnection);
		this.accountUpdater = new AccountUpdater(sqlConnection);
	}
	
	
	/**
	 * Creates an account for a player and fills up its PlayerData.
	 * 
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	public void createPlayerAccount(PlayerData playerData)
		throws SQLException
	{
		this.accountCreator.createPlayerAccount(playerData);
	}
	
	/**
	 * Updates an account for a player according to its PlayerData.
	 * 
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	public void updatePlayerAccount(PlayerData playerData)
		throws SQLException
	{
		this.accountUpdater.updatePlayerAccount(playerData);
	}
	
	/**
	 * Fills up a PlayerData according to SQL fetched data.
	 * 
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	public void getPlayerAccount(PlayerData playerData)
		throws SQLException, NoSuchFieldException
	{
		this.accountGetter.getPlayerAccount(playerData, false);
	}
	
	/**
	 * Entirely fills up a PlayerData according to SQL fetched data.
	 * 
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	public void getFullPlayerAccount(PlayerData playerData)
		throws SQLException, NoSuchFieldException
	{
		this.accountGetter.getPlayerAccount(playerData, true);
	}

}
