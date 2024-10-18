package fr.hashtek.tekore.common.friendship;

import java.sql.Timestamp;
import java.util.UUID;

public class Friendship
{

    private final String uuid;
    private final String senderUuid;
    private final String receiverUuid;
    private final FriendshipRequestState state;
    private final Timestamp requestedAt;
    private final Timestamp acceptedAt;


    /**
     * Creates a new Friendship.
     * <p>
     * See this class as a link between two players, with some
     * metadata about the creation of their friendship.
     * </p>
     *
     * @param   uuid            Friendship's UUID
     * @param   senderUuid      Sender's UUID
     * @param   receiverUuid    Receiver's UUID
     * @param   state           Friendship's state
     * @param   requestedAt     Request timestamp
     * @param   acceptedAt      Acceptation timestamp
     */
    public Friendship(
        String uuid,
        String senderUuid,
        String receiverUuid,
        FriendshipRequestState state,
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
     * Creates a new Friendship between two players.
     *
     * @param   senderUuid      Sender's UUID
     * @param   receiverUuid    Receiver's UUID
     * @return  Created friendship
     */
    public static Friendship create(
        String senderUuid,
        String receiverUuid
    )
    {
        return new Friendship(
            UUID.randomUUID().toString(),
            senderUuid,
            receiverUuid,
            FriendshipRequestState.PENDING,
            new Timestamp(System.currentTimeMillis()),
            null
        );
    }

    /**
     * @return  Friendship's UUID
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
     * @return  Friendship's state
     */
    public FriendshipRequestState getState()
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
