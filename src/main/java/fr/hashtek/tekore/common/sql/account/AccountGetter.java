package fr.hashtek.tekore.common.sql.account;

import java.sql.*;

import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.common.player.PlayerSettingsManager;
import fr.hashtek.tekore.common.player.settings.categories.SettingEFFN;
import fr.hashtek.tekore.common.player.settings.categories.SettingEFN;
import fr.hashtek.tekore.common.player.settings.categories.SettingEN;
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


	/**
	 * @param	playerData		Player's data
	 * @param	resultSet		SQL ResultSet
	 * @throws	SQLException	SQL failure
	 */
	private void setPlayerRank(PlayerData playerData, ResultSet resultSet)
		throws SQLException
	{
		final RankGetter rankGetter = new RankGetter(this.sqlConnection);
		final Rank rank = rankGetter.getRankFromResultSet(resultSet, "ranks.");

		playerData.setRank(rank);
	}

	/**
	 * @param	playerManager				Player's manager
	 * @param	resultSet					SQL ResultSet
	 * @throws	SQLException				SQL failure
	 * @throws	IllegalArgumentException	Unknown setting
	 */
	private void setPlayerSettings(PlayerManager playerManager, ResultSet resultSet)
		throws SQLException, IllegalArgumentException
	{
		final PlayerSettingsManager settingsManager = playerManager.getSettingsManager();

		settingsManager.setLobbyPlayersSetting(SettingEN.valueOf(resultSet.getString("settings.showLobbyPlayers")));
		settingsManager.setPrivateMessagesSetting(SettingEFN.valueOf(resultSet.getString("settings.privateMessages")));
		settingsManager.setFriendRequestsSetting(SettingEFFN.valueOf(resultSet.getString("settings.friendRequests")));
		settingsManager.setPartyRequestsSetting(SettingEFFN.valueOf(resultSet.getString("settings.partyRequests")));
		settingsManager.setGuildRequestsSetting(SettingEFFN.valueOf(resultSet.getString("settings.guildRequests")));
	}

	/**
	 * Fills up a PlayerData using a ResultSet results.
	 * 
	 * @param	playerManager				Player's manager
	 * @param	resultSet					ResultSet results
	 * @param	fillAllData					Should fetch UUID and username from the database too ?
	 * @throws	SQLException				SQL failure
	 * @throws	IllegalArgumentException	Unknown setting
	 */
	private void fillPlayerData(PlayerManager playerManager, ResultSet resultSet, boolean fillAllData)
		throws SQLException, IllegalArgumentException
	{
		final PlayerData playerData = playerManager.getData();

		if (fillAllData) {
			playerData.setUniqueId(resultSet.getString("uuid"));
			playerData.setUsername(resultSet.getString("username"));
		}
		
		playerData.setCreatedAt(resultSet.getTimestamp("createdAt"));
		playerData.setLastUpdate(resultSet.getTimestamp("lastUpdate"));

		playerData.setCoins(resultSet.getInt("coins"));
		playerData.setHashCoins(resultSet.getInt("hashCoins"));

		this.setPlayerRank(playerData, resultSet);
		if (playerManager.getSettingsManager() != null)
			this.setPlayerSettings(playerManager, resultSet);
	}
	
	/**
	 * Gets a player's data from the SQL database and
	 * fills up a PlayerData with the fetched data.
	 *
	 * @param	playerManager				Player's manager
	 * @param	fillAllData					Should fetch UUID and username from the database too ?
	 * @throws	SQLException				SQL failure
	 * @throws	NoSuchFieldException		No account found
	 * @throws	IllegalArgumentException	Unknown setting
	 */
	public void getPlayerAccount(PlayerManager playerManager, boolean fillAllData)
		throws SQLException, NoSuchFieldException, IllegalArgumentException
	{
		final PlayerData playerData = playerManager.getData();
		final PreparedStatement statement;
		final ResultSet resultSet;
		final String query = "SELECT * FROM players " +
			"JOIN ranks ON ranks.uuid = players.rankUuid " +
			"JOIN settings ON settings.uuid = players.uuid " +
			"WHERE players." + (fillAllData ? "username" : "uuid") + " = ?;";
		
		statement = sqlConnection.prepareStatement(query);

		statement.setString(1, fillAllData ? playerData.getUsername() : playerData.getUniqueId());
		
		resultSet = statement.executeQuery();
		
		this.sqlConnection.commit();
		
		if (!resultSet.next())
			throw new NoSuchFieldException();

		this.fillPlayerData(playerManager, resultSet, fillAllData);

		resultSet.close();
		statement.close();
	}

}
