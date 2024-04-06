package fr.hashtek.tekore.common.sql.account;

import java.sql.*;

import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.player.PlayerSettings;
import fr.hashtek.tekore.common.player.settings.SettingsFriendRequests;
import fr.hashtek.tekore.common.player.settings.SettingsPrivateMessages;
import fr.hashtek.tekore.common.sql.rank.RankGetter;

public class AccountGetter
{
	
	private final Connection sqlConnection;
	
	
	/**
	 * Creates a new instance of AccountGetter.
	 * 
	 * @param	sqlConnection	SQL connection
	 */
	public AccountGetter(Connection sqlConnection)
	{
		this.sqlConnection = sqlConnection;
	}
	
	
	private void setPlayerRank(PlayerData playerData, ResultSet resultSet)
		throws SQLException
	{
		RankGetter rankGetter = new RankGetter(this.sqlConnection);
		Rank rank = rankGetter.getRankFromResultSet(resultSet, "ranks.");

		playerData.setRank(rank);
	}

	private void setPlayerSettings(PlayerData playerData, ResultSet resultSet)
		throws SQLException, IllegalArgumentException
	{
		PlayerSettings playerSettings = playerData.getPlayerSettings();

		playerSettings.setLobbyPlayersSetting(resultSet.getBoolean("settings.showLobbyPlayers"));
		playerSettings.setFriendRequestsSetting(SettingsFriendRequests.valueOf(resultSet.getString("settings.friendRequests")));
		playerSettings.setPrivateMessagesSetting(SettingsPrivateMessages.valueOf(resultSet.getString("settings.privateMessages")));
	}

	/**
	 * Fills up a PlayerData using a ResultSet results.
	 * 
	 * @param	playerData				Player's data
	 * @param	resultSet				ResultSet results
	 * @param	fillAllData				Should fetch UUID and username from the database too ?
	 * @throws	SQLException			SQL failure
	 */
	private void fillPlayerData(PlayerData playerData, ResultSet resultSet, boolean fillAllData)
		throws SQLException, IllegalArgumentException
	{
		if (fillAllData) {
			playerData.setUniqueId(resultSet.getString("uuid"));
			playerData.setUsername(resultSet.getString("username"));
		}
		
		playerData.setCreatedAt(resultSet.getTimestamp("createdAt"));
		playerData.setLastUpdate(resultSet.getTimestamp("lastUpdate"));

		playerData.setCoins(resultSet.getInt("coins"));
		playerData.setHashCoins(resultSet.getInt("hashCoins"));

		this.setPlayerRank(playerData, resultSet);
		this.setPlayerSettings(playerData, resultSet);
	}
	
	/**
	 * Gets a player's data from the SQL database and
	 * fills up a PlayerData with the fetched data.
	 *
	 * @param	playerData				Player's data
	 * @param	fillAllData				Should fetch UUID and username from the database too ?
	 * @throws	SQLException			SQL failure
	 * @throws	NoSuchFieldException	No account found
	 */
	public void getPlayerAccount(PlayerData playerData, boolean fillAllData)
		throws SQLException, NoSuchFieldException, IllegalArgumentException
	{
		PreparedStatement statement;
		ResultSet resultSet;
		String query = "SELECT * FROM players " +
			"JOIN ranks ON ranks.uuid = players.rankUuid " +
			"JOIN settings ON settings.uuid = players.uuid " +
			"WHERE players." + (fillAllData ? "username" : "uuid") + " = ?;";
		
		statement = sqlConnection.prepareStatement(query);

		statement.setString(1, fillAllData ? playerData.getUsername() : playerData.getUniqueId());
		
		resultSet = statement.executeQuery();
		
		this.sqlConnection.commit();
		
		if (!resultSet.next())
			throw new NoSuchFieldException();

		ResultSetMetaData md = resultSet.getMetaData();

		for (int k = 1; k < md.getColumnCount(); k++) {
			System.out.println(md.getTableName(k) + "." + md.getColumnName(k) + " : " + resultSet.getObject(k));
		}

		this.fillPlayerData(playerData, resultSet, fillAllData);

		resultSet.close();
		statement.close();
	}

}
