package fr.hashtek.tekore.common.friendship;

import java.sql.Timestamp;
import java.util.UUID;

public class Friendship
{

    private String uuid;
    private String senderUuid;
    private String receiverUuid;
    private FriendshipRequestState state;
    private Timestamp requestedAt;
    private Timestamp acceptedAt;


    /**
     * Creates an empty friendship.
     *
     * @apiNote Solely used for Redis stuff, not for public use.
     */
    public Friendship() {}

    /**
     * Creates a new Friendship.
     * <p>
     * See this class as a link between two players, with some
     * metadata about the creation of their friendship.
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
     * @param   playerUuid  Player's UUID
     * @return  Player's friend's UUID
     */
    public String getFriendUuid(String playerUuid)
    {
        if (playerUuid.equals(this.senderUuid)) {
            return this.receiverUuid;
        }
        return this.senderUuid;
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

    /**
     * @param   uuid    New UUID
     */
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    /**
     * @param   senderUuid  New sender's UUID
     */
    public void setSenderUuid(String senderUuid)
    {
        this.senderUuid = senderUuid;
    }

    /**
     * @param   receiverUuid    New receiver's UUID
     */
    public void setReceiverUuid(String receiverUuid)
    {
        this.receiverUuid = receiverUuid;
    }

    /**
     * @param   state   New friendship state
     */
    public void setState(FriendshipRequestState state)
    {
        this.state = state;
    }

    /**
     * @param   requestedAt New request timestamp
     */
    public void setRequestedAt(Timestamp requestedAt)
    {
        this.requestedAt = requestedAt;
    }

    /**
     * @param   acceptedAt  New request acceptation timestamp
     */
    public void setAcceptedAt(Timestamp acceptedAt)
    {
        this.acceptedAt = acceptedAt;
    }

}
