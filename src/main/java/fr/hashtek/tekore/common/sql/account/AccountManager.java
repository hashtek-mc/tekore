package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.SQLException;

import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.player.PlayerManager;

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
	 * Updates an account for a player according to its PlayerManager.
	 * 
	 * @param	playerManager	Player's manager
	 * @throws	SQLException	SQL failure
	 */
	public void updatePlayerAccount(PlayerManager playerManager)
		throws SQLException
	{
		this.accountUpdater.updatePlayerAccount(playerManager);
	}
	
	/**
	 * Fills up a PlayerManager according to SQL fetched data.
	 * 
	 * @param	playerManager	Player's manager
	 * @throws	SQLException	SQL failure
	 */
	public void getPlayerAccount(PlayerManager playerManager)
		throws SQLException, NoSuchFieldException
	{
		this.accountGetter.getPlayerAccount(playerManager, false);
	}
	
	/**
	 * Entirely fills up a PlayerData according to SQL fetched data.
	 * 
	 * @param	playerManager	Player's manager
	 * @throws	SQLException	SQL failure
	 */
	public void getFullPlayerAccount(PlayerManager playerManager)
		throws SQLException, NoSuchFieldException
	{
		this.accountGetter.getPlayerAccount(playerManager, true);
	}


	/**
	 * @return	Account creator
	 */
	public AccountCreator getAccountCreator()
	{
		return this.accountCreator;
	}

	/**
	 * @return	Account getter
	 */
	public AccountGetter getAccountGetter()
	{
		return this.accountGetter;
	}

	/**
	 * @return	Account updater
	 */
	public AccountUpdater getAccountUpdater()
	{
		return this.accountUpdater;
	}

}
