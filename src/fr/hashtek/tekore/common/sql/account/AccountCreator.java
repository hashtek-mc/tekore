package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hashtek.tekore.common.player.PlayerData;

public class AccountCreator {
	
	private final Connection sqlConnection;
	
	
	/**
	 * Creates a new instance of AccountCreator.
	 * 
	 * @param	sqlConnection	SQL connection
	 */
	public AccountCreator(Connection sqlConnection)
	{
		this.sqlConnection = sqlConnection;
	}
	

	/**
	 * Inserts into `players` table.
	 * 
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	private void queryPlayers(PlayerData playerData) throws SQLException
	{
		PreparedStatement statement;
		String query = "INSERT INTO players (uuid, username) VALUES (?, ?);";

		statement = this.sqlConnection.prepareStatement(query);
		statement.setString(1, playerData.getUniqueId());
		statement.setString(2, playerData.getUsername());
		statement.executeUpdate();
		statement.close();
	}

	/**
	 * Inserts into `players` table.
	 *
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	private void querySettings(PlayerData playerData) throws SQLException
	{
		PreparedStatement statement;
		String query = "INSERT INTO settings (uuid) VALUES (?);";

		statement = this.sqlConnection.prepareStatement(query);
		statement.setString(1, playerData.getUniqueId());
		statement.executeUpdate();
		statement.close();
	}
	
	/**
	 * Adds a new account to the database.
	 * 
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	public void createPlayerAccount(PlayerData playerData) throws SQLException
	{
		this.queryPlayers(playerData);
		this.querySettings(playerData);
		
		this.sqlConnection.commit();
	}
	
}
