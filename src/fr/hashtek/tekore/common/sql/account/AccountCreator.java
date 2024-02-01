package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.common.Exit;
import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerData;

public class AccountCreator {
	
	private static final String FILENAME = "AccountCreation.java";
	
	private static Connection connection;
	

	private static Exit queryCore(PlayerData playerData)
	{
		PreparedStatement statement = null;
		String query = "INSERT INTO core (uuid, username) VALUES (?, ?);";
		
		String uuid = playerData.getUniqueId();
		String username = playerData.getUsername();
		
		HashLogger.debug(FILENAME, "Creating core query for player \"" + uuid + "\"...");
		
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, playerData.getFormattedUniqueId());
			statement.setString(2, username);
			statement.executeUpdate();
		} catch (SQLException exception) {
			HashLogger.err(FILENAME, "Failed to create core query for player \"" + uuid + "\".", exception);
			return Exit.FAILURE;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException exception) {
					HashLogger.err(FILENAME, "Failed to close a database component.", exception);
					return Exit.FAILURE;
				}
			}
		}
		
		HashLogger.debug(FILENAME, "Core query for player \"" + uuid + "\" succeeded.");
		
		return Exit.SUCCESS;
	}
	
	private static Exit queryProfile(PlayerData playerData)
	{
		PreparedStatement statement = null;
		String query = "INSERT INTO profiles (uuid, rank) VALUES (?, ?);";
		
		String uuid = playerData.getUniqueId();
		Rank rank = playerData.getProfile().getRank();
		
		HashLogger.debug(FILENAME, "Creating profile query for player \"" + uuid + "\"...");
		
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, playerData.getFormattedUniqueId());
			statement.setString(2, rank.getDatabaseName());
			statement.executeUpdate();
		} catch (SQLException exception) {
			HashLogger.err(FILENAME, "Failed to create profile query for player \"" + uuid + "\".", exception);
			return Exit.FAILURE;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException exception) {
					HashLogger.err(FILENAME, "Failed to close a database component.", exception);
					return Exit.FAILURE;
				}
			}
		}
		
		HashLogger.debug(FILENAME, "Profile query for player \"" + uuid + "\" succeeded.");
		
		return Exit.SUCCESS;
	}
	
	private static Exit queryStats(PlayerData playerData)
	{
		PreparedStatement statement = null;
		String query = "INSERT INTO stats (uuid) VALUES (?);";
		
		String uuid = playerData.getUniqueId();
		
		HashLogger.debug(FILENAME, "Creating stats query for player \"" + uuid + "\"...");
		
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, playerData.getFormattedUniqueId());
			statement.executeUpdate();
		} catch (SQLException exception) {
			HashLogger.err(FILENAME, "Failed to create stats query for player \"" + uuid + "\".", exception);
			return Exit.FAILURE;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException exception) {
					HashLogger.err(FILENAME, "Failed to close a database component.", exception);
					return Exit.FAILURE;
				}
			}
		}
		
		HashLogger.debug(FILENAME, "Stats query for player \"" + uuid + "\" succeeded.");
		
		return Exit.SUCCESS;
	}
	
	public static Exit createPlayerAccount(Connection conn, PlayerData playerData)
	{
		String uuid = playerData.getUniqueId().toString();
		
		connection = conn;
		
		if (queryCore(playerData) == Exit.FAILURE)
			return Exit.FAILURE;
		if (queryProfile(playerData) == Exit.FAILURE)
			return Exit.FAILURE;
		if (queryStats(playerData) == Exit.FAILURE)
			return Exit.FAILURE;
		
		try {
			connection.commit();
		} catch (SQLException exception) {
			HashLogger.err(FILENAME, "Failed to create player \"" + uuid + "\" account.", exception);
			return Exit.FAILURE;
		}
		
		HashLogger.debug(FILENAME, "Account for player \"" + uuid + "\" created.");
		
		return Exit.SUCCESS;
	}
	
}
