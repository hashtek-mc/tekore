package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerData;

public class AccountGetter {
	
	private static Connection connection;
	
	
	private static void setCoreAttributes(PlayerData playerData, ResultSet resultSet, boolean isPlayerDataIncomplete)
		throws SQLException
	{
		if (isPlayerDataIncomplete) {
			playerData.setUniqueId(resultSet.getString("core.uuid"));
			playerData.setUsername(resultSet.getString("core.username"));
		}
	}
	
	private static void setProfileAttributes(PlayerData playerData, ResultSet resultSet, boolean isPlayerDataIncomplete)
		throws SQLException
	{
		String rawRank = resultSet.getString("profiles.rank");
		Rank rank = Rank.getRankByDatabaseName(rawRank);
		
		playerData.getProfile().setRank(rank);
	}
	
	private static void setStatsAttributes(PlayerData playerData, ResultSet resultSet, boolean isPlayerDataIncomplete)
		throws SQLException
	{
		
	}
	
	/**
	 * 
	 * @param conn
	 * @param playerData
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 */
	public static PlayerData getPlayerAccount(Connection conn, PlayerData playerData)
		throws SQLException, NoSuchFieldException
	{
		boolean isPlayerDataIncomplete = playerData.getUniqueId() == null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM core " +
			"JOIN profiles ON core.uuid = profiles.uuid " +
			"JOIN stats ON core.uuid = stats.uuid " +
			"WHERE core." + (isPlayerDataIncomplete ? "username" : "uuid") + " = ?";
		
		connection = conn;
		
		statement = connection.prepareStatement(query);
		if (!isPlayerDataIncomplete)
			statement.setString(1, playerData.getFormattedUniqueId());
		else
			statement.setString(1, playerData.getUsername());
		resultSet = statement.executeQuery();
		
		connection.commit();
		
		do {
			if (!resultSet.next())
				throw new NoSuchFieldException();
			
			setCoreAttributes(playerData, resultSet, isPlayerDataIncomplete);
			setProfileAttributes(playerData, resultSet, isPlayerDataIncomplete);
			setStatsAttributes(playerData, resultSet, isPlayerDataIncomplete);
		} while (resultSet.next());
		
		if (resultSet != null)
			resultSet.close();
		
		if (statement != null)
			statement.close();
		
		return playerData;
	}

}
