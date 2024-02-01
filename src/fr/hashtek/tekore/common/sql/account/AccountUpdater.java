package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.common.Exit;
import fr.hashtek.tekore.common.player.PlayerData;

public class AccountUpdater {

	private static final String FILENAME = "AccountCreation.java";
	
	private static Connection connection;
	
	
	public static Exit updatePlayerAccount(Connection conn, PlayerData playerData)
	{
		PreparedStatement statement = null;
		String query = "UPDATE core " +
			"JOIN profiles on profiles.uuid = core.uuid " +
			"JOIN stats on stats.uuid = core.uuid " +
			"SET " +
			
			"core.username = ?, " +
			// "core.last_login = ?" + 
			
			"profiles.rank = ?, " +
			
			"stats.total_uptime = ? " +
			
			"WHERE core.uuid = ?;";
		
		String uuid = playerData.getUniqueId().toString();
		
		connection = conn;

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, playerData.getUsername());
			statement.setString(2, playerData.getProfile().getRank().getDatabaseName());
			statement.setLong(3, 0);
			statement.setString(4, playerData.getFormattedUniqueId());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException exception) {
			HashLogger.err(FILENAME, "Failed to update player \"" + uuid + "\" account.", exception);
			return Exit.FAILURE;
		}
		
		HashLogger.debug(FILENAME, "Account for player \"" + uuid + "\" updated.");
		
		return Exit.SUCCESS;
	}
	
}
