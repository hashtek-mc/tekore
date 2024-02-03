package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hashtek.tekore.common.player.PlayerData;

public class AccountUpdater {

	private static Connection connection;
	
	
	/**
	 * Updates a player's account in the SQL database.
	 * 
	 * @param conn			SQL connection
	 * @param playerData	Player's data
	 */
	public static void updatePlayerAccount(Connection conn, PlayerData playerData)
		throws SQLException
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
		
		connection = conn;

		statement = connection.prepareStatement(query);
		statement.setString(1, playerData.getUsername());
		statement.setString(2, playerData.getProfile().getRank().getDatabaseName());
		statement.setLong(3, 0);
		statement.setString(4, playerData.getFormattedUniqueId());
		statement.executeUpdate();
		connection.commit();
	}
	
}
