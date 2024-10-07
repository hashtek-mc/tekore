package fr.hashtek.tekore.common.player.friend;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class PlayerFriendLink
{

    private final String uuid;
    private final String senderUuid;
    private final String receiverUuid;
    private final PlayerFriendRequestState state;
    private final Timestamp requestedAt;
    private final Timestamp acceptedAt;


    /**
     * Creates a new friendship link.
     *
     * @param   uuid            Link's UUID
     * @param   senderUuid      Sender's UUID
     * @param   receiverUuid    Receiver's UUID
     * @param   state           Link state
     * @param   requestedAt     Request timestamp
     * @param   acceptedAt      Acceptation timestamp
     */
    public PlayerFriendLink(
        String uuid,
        String senderUuid,
        String receiverUuid,
        PlayerFriendRequestState state,
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

    /**
     * Creates a new friendship link.
     *
     * @param   senderUuid      Sender's UUID
     * @param   receiverUuid    Receiver's UUID
     */
    public PlayerFriendLink(
        String senderUuid,
        String receiverUuid
    )
    {
        this(
            UUID.randomUUID().toString(),
            senderUuid,
            receiverUuid,
            PlayerFriendRequestState.PENDING,
            new Timestamp(new Date().getTime()),
            null
        );
    }


    /**
     * @return  Link's UUID
     */
    public String getUuid()
    {
        return this.uuid;
    }

    /**
     * @return  Sender's UUID
     */
    public String getSenderUuid()
    {
        return this.senderUuid;
    }

    /**
     * @return  Receiver's UUID
     */
    public String getReceiverUuid()
    {
        return this.receiverUuid;
    }

    /**
     * @return  Link state
     */
    public PlayerFriendRequestState getState()
    {
        return this.state;
    }

    /**
     * @return  Request timestamp
     */
    public Timestamp getRequestedAt()
    {
        return this.requestedAt;
    }

    /**
     * @return  Acceptation timestamp
     */
    public Timestamp getAcceptedAt()
    {
        return this.acceptedAt;
    }

}
