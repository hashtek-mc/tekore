package fr.hashtek.tekore.common.sql.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.player.PlayerSettings;

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
	 * @param	playerData		Player's data
	 * @throws	SQLException	SQL failure
	 */
	public void updatePlayerAccount(PlayerData playerData)
		throws SQLException
	{
		Timestamp now = new Timestamp(System.currentTimeMillis());

		PlayerSettings playerSettings = playerData.getPlayerSettings();

		PreparedStatement statement;
		String query = "UPDATE players " +
			"JOIN settings ON settings.uuid = players.uuid " +
			"SET " +

			"players.username = ?, " +
			"players.last_update = ?, " +
			"players.rank_uuid = ?, " +
			"players.coins = ?, " +
			"players.hash_coins = ?, " +

			"settings.show_lobby_players = ?, " +
			"settings.friend_requests = ?, " +
			"settings.private_messages = ? " +

			"WHERE players.uuid = ?;";

		statement = sqlConnection.prepareStatement(query);
		
		statement.setString(1, playerData.getUsername());
		statement.setTimestamp(2, now);
		statement.setString(3, playerData.getRank().getUuid());
		statement.setInt(4, playerData.getCoins());
		statement.setInt(5, playerData.getHashCoins());

		statement.setBoolean(6, playerSettings.getLobbyPlayersSetting());
		statement.setString(7, playerSettings.getFriendRequestsSetting().name());
		statement.setString(8, playerSettings.getPrivateMessagesSetting().name());

		statement.setString(9, playerData.getUniqueId());
		
		statement.executeUpdate();
		
		this.sqlConnection.commit();
	}
	
}
