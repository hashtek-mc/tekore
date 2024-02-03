package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerData;

public class AccountCreator {
	
	private static Connection connection;
	

	/**
	 * Inserts into `core` table.
	 * 
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	private static void queryCore(PlayerData playerData) throws SQLException
	{
		PreparedStatement statement = null;
		String query = "INSERT INTO core (uuid, username) VALUES (?, ?);";

		statement = connection.prepareStatement(query);
		statement.setString(1, playerData.getFormattedUniqueId());
		statement.setString(2, playerData.getUsername());
		statement.executeUpdate();
		statement.close();
	}
	
	/**
	 * Inserts into `profiles` table.
	 * 
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	private static void queryProfile(PlayerData playerData) throws SQLException
	{
		PreparedStatement statement = null;
		String query = "INSERT INTO profiles (uuid, rank) VALUES (?, ?);";
		
		Rank rank = playerData.getProfile().getRank();
		
		statement = connection.prepareStatement(query);
		statement.setString(1, playerData.getFormattedUniqueId());
		statement.setString(2, rank.getDatabaseName());
		statement.executeUpdate();
		statement.close();
	}
	
	/**
	 * Inserts into `stats` table.
	 * 
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	private static void queryStats(PlayerData playerData) throws SQLException
	{
		PreparedStatement statement = null;
		String query = "INSERT INTO stats (uuid) VALUES (?);";
		
		statement = connection.prepareStatement(query);
		statement.setString(1, playerData.getFormattedUniqueId());
		statement.executeUpdate();
		statement.close();
	}
	
	/**
	 * Adds a new account to the database.
	 * 
	 * @param	conn			SQL connection
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	public static void createPlayerAccount(Connection conn, PlayerData playerData)
		throws SQLException
	{
		connection = conn;
		
		queryCore(playerData);
		queryProfile(playerData);
		queryStats(playerData);
		
		connection.commit();
	}
	
}
