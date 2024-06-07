package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.common.player.settings.PlayerSettingsManager;

public class AccountUpdater
{

	private final Connection sqlConnection;
	
	
	/**
	 * Creates a new instance of AccountUpdater.
	 * 
	 * @param	sqlConnection	SQL connection
	 */
	public AccountUpdater(Connection sqlConnection)
	{
		this.sqlConnection = sqlConnection;
	}
	
	
	/**
	 * Updates a player's account in the SQL database.
	 * 
	 * @param	playerManager	Player's manager
	 * @throws	SQLException	SQL failure
	 */
	public void updatePlayerAccount(PlayerManager playerManager)
		throws SQLException
	{
		final PlayerData playerData = playerManager.getData();
		final Timestamp now = new Timestamp(System.currentTimeMillis());

		final PlayerSettingsManager playerSettingsManager = playerManager.getSettingsManager();

		final PreparedStatement statement;
		final String query = "UPDATE players " +
			"JOIN settings ON settings.uuid = players.uuid " +
			"SET " +

			"players.username = ?, " +
			"players.lastUpdate = ?, " +
			"players.rankUuid = ?, " +
			"players.coins = ?, " +
			"players.hashCoins = ?, " +

			"settings.showLobbyPlayers = ?, " +
			"settings.privateMessages = ?, " +
			"settings.friendRequests = ?, " +
			"settings.partyRequests = ?, " +
			"settings.guildRequests = ? " +

			"WHERE players.uuid = ?;";

		statement = sqlConnection.prepareStatement(query);
		
		statement.setString(1, playerData.getUsername());
		statement.setTimestamp(2, now);
		statement.setString(3, playerData.getRank().getUuid());
		statement.setInt(4, playerData.getCoins());
		statement.setInt(5, playerData.getHashCoins());

		statement.setString(6, playerSettingsManager.getLobbyPlayersSetting().name());
		statement.setString(7, playerSettingsManager.getPrivateMessagesSetting().name());
		statement.setString(8, playerSettingsManager.getFriendRequestsSetting().name());
		statement.setString(9, playerSettingsManager.getPartyRequestsSetting().name());
		statement.setString(10, playerSettingsManager.getGuildRequestsSetting().name());

		statement.setString(11, playerData.getUniqueId());
		
		statement.executeUpdate();
		
		this.sqlConnection.commit();
	}
	
}
