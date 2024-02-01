package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerData;

public class AccountGetter {
	
	@SuppressWarnings("unused")
	private static final String FILENAME = "AccountGetter.java";
	
	private static Connection connection;
	
	
	private static void setCoreAttributes(PlayerData playerData, ResultSet resultSet)
		throws SQLException
	{
		
	}
	
	private static void setProfileAttributes(PlayerData playerData, ResultSet resultSet)
		throws SQLException
	{
		String rawRank = resultSet.getString("profiles.rank");
		Rank rank = Rank.getRankByDatabaseName(rawRank);
		
		playerData.getProfile().setRank(rank);
	}
	
	private static void setStatsAttributes(PlayerData playerData, ResultSet resultSet)
		throws SQLException
	{
		
	}
	
	public static PlayerData getPlayerAccount(Connection conn, PlayerData playerData)
		throws SQLException, NoSuchFieldException
	{
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM core " +
			"JOIN profiles ON core.uuid = profiles.uuid " +
			"JOIN stats ON core.uuid = stats.uuid " +
			"WHERE core.uuid = ?";
		
		connection = conn;
		
		statement = connection.prepareStatement(query);
		statement.setString(1, playerData.getFormattedUniqueId());
		resultSet = statement.executeQuery();
		
		connection.commit();
		
		do {
			if (!resultSet.next())
				throw new NoSuchFieldException();
			
			setCoreAttributes(playerData, resultSet);
			setProfileAttributes(playerData, resultSet);
			setStatsAttributes(playerData, resultSet);
		} while (resultSet.next());
		
		if (resultSet != null)
			resultSet.close();
		
		if (statement != null)
			statement.close();
		
		return playerData;
	}

}
