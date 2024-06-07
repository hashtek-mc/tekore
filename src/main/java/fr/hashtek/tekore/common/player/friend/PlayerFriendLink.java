package fr.hashtek.tekore.common.player.friend;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class PlayerFriendLink
{

    private final String uuid;
    private final String senderUuid;
    private final String receiverUuid;
    private final PlayerFriendState state;
    private final Timestamp requestedAt;
    private final Timestamp acceptedAt;


    public PlayerFriendLink(
        String uuid,
        String senderUuid,
        String receiverUuid,
        PlayerFriendState state,
        Timestamp requestedAt,
        Timestamp acceptedAt
    )
    {
        this.uuid = uuid;
        this.senderUuid = senderUuid;
        this.receiverUuid = receiverUuid;
        this.state = state;
        this.requestedAt = requestedAt;
        this.acceptedAt = acceptedAt;
    }


    public static PlayerFriendLink newFriendship(String senderUuid, String receiverUuid)
    {
        return new PlayerFriendLink(
            UUID.randomUUID().toString(),
            senderUuid,
            receiverUuid,
            PlayerFriendState.PENDING,
            new Timestamp(new Date().getTime()),
            null
        );
    }


    public String getUuid()
    {
        return this.uuid;
    }

    public String getSenderUuid()
    {
        return this.senderUuid;
    }

    public String getReceiverUuid()
    {
        return this.receiverUuid;
    }

    public PlayerFriendState getState()
    {
        return this.state;
    }

    public Timestamp getRequestedAt()
    {
        return this.requestedAt;
    }

    public Timestamp getAcceptedAt()
    {
        return this.acceptedAt;
    }

}
