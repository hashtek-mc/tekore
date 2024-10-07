package fr.hashtek.tekore.common.sql.friends;

import fr.hashtek.tekore.common.player.friend.PlayerFriendLink;
import fr.hashtek.tekore.common.player.friend.PlayerFriendRequestState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendsGetter
{

    private final Connection sqlConnection;


    public FriendsGetter(Connection sqlConnection)
    {
        this.sqlConnection = sqlConnection;
    }


    public List<PlayerFriendLink> getFriendsLinks()
        throws SQLException, IllegalArgumentException
    {
        final List<PlayerFriendLink> friendsLinks = new ArrayList<PlayerFriendLink>();

        final String query = "SELECT * FROM friends;";
        final PreparedStatement statement = this.sqlConnection.prepareStatement(query);
        final ResultSet resultSet = statement.executeQuery();

        this.sqlConnection.commit();

        while (resultSet.next()) {
            friendsLinks.add(new PlayerFriendLink(
                resultSet.getString("uuid"),
                resultSet.getString("senderUuid"),
                resultSet.getString("receiverUuid"),
                PlayerFriendRequestState.valueOf(resultSet.getString("state")),
                resultSet.getTimestamp("requestedAt"),
                resultSet.getTimestamp("acceptedAt")
            ));
        }

        resultSet.close();
        statement.close();

        return friendsLinks;
    }

}
