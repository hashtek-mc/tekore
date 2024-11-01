package fr.hashtek.tekore.common.friendship;

import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.friendship.io.FriendshipProvider;
import fr.hashtek.tekore.common.friendship.io.FriendshipPublisher;
import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FriendshipManager
{

    private static final Tekore CORE = Tekore.getInstance();
    private static final RedisAccess REDIS_ACCESS = CORE.getRedisAccess();


    private final String playerUuid;
    private final List<Friendship> friendships;


    /**
     * Creates a new Friendship manager.
     */
    public FriendshipManager(Player player)
    {
        this.playerUuid = player.getUniqueId().toString();
        this.friendships = new ArrayList<>();

        this.fetchFriendships();
    }


    /**
     * Fetches every friendship player has from the Redis database
     * and stores it here.
     */
    public void fetchFriendships()
    {
        final List<Friendship> playerFriendships = new FriendshipProvider(REDIS_ACCESS)
            .getPlayerFriendships(this.playerUuid);

        this.friendships.clear();
        this.friendships.addAll(playerFriendships);
    }


    /**
     * Creates a new friendship between the player and a target
     * (targeted via its UUID).
     *
     * @param   targetUuid  Target's UUID
     */
    public void createFriendship(String targetUuid)
        throws EntryNotFoundException
    {
        /*
         * Verify if target has an account on the server.
         * If not, EntryNotFoundException will be thrown.
         */
        if (!new AccountProvider(REDIS_ACCESS).hasAccount(targetUuid)) {
            throw new EntryNotFoundException(targetUuid);
        }

        /* Create the friendship */
        final Friendship friendship = Friendship.create(this.playerUuid, targetUuid);
        final FriendshipPublisher publisher = new FriendshipPublisher(REDIS_ACCESS);

        /* Push the friendship */
        this.friendships.add(friendship);
        publisher.push(friendship);

        /* And add the friendship's UUID to player's friendship list. */
        publisher.involvePlayerIntoFriendship(this.playerUuid, friendship);
        publisher.involvePlayerIntoFriendship(targetUuid, friendship);
    }

    /**
     * Destroys a friendship.
     *
     * @param   friendshipUuid  Friendship's UUID
     * @throws  EntryNotFoundException  If friendship does not exist
     */
    public void destroyFriendship(String friendshipUuid)
        throws EntryNotFoundException
    {
        final Friendship friendshipToRemove = this.getFriendship(friendshipUuid);

        if (friendshipToRemove == null) {
            throw new EntryNotFoundException(friendshipUuid);
        }

        final FriendshipPublisher publisher = new FriendshipPublisher(REDIS_ACCESS);

        /* Disengage both players from the friendship */
        publisher.disengagePlayerFromFriendship(friendshipToRemove.getSenderUuid(), friendshipUuid);
        publisher.disengagePlayerFromFriendship(friendshipToRemove.getReceiverUuid(), friendshipUuid);

        /* Destroy the friendship */
        this.friendships.remove(friendshipToRemove);
        publisher.remove(friendshipUuid);
    }

    /**
     * @param   friendshipUuid  Friendship's UUID
     * @return  If exists, fetched friendship. Otherwise, null.
     */
    public Friendship getFriendship(String friendshipUuid)
    {
        return this.friendships.stream()
            .filter((Friendship friendship) -> friendship.getUuid().equals(friendshipUuid))
            .findFirst()
            .orElse(null);
    }

    /**
     * @param   senderUuid  Sender's UUID
     * @return  Fetched friendship where sender UUID equals to the given one
     */
    public Friendship getFriendshipBySender(String senderUuid)
    {
        return this.friendships.stream()
            .filter((Friendship friendship) -> friendship.getSenderUuid().equals(senderUuid))
            .findFirst()
            .orElse(null);
    }

    /**
     * @param   receiverUuid    Receiver's UUID
     * @return  Fetched friendship where sender UUID equals to the given one
     */
    public Friendship getFriendshipByReceiver(String receiverUuid)
    {
        return this.friendships.stream()
            .filter((Friendship friendship) -> friendship.getReceiverUuid().equals(receiverUuid))
            .findFirst()
            .orElse(null);
    }

    public Friendship getFriendshipByUuid(String targetUuid)
    {
        return this.friendships.stream()
            .filter((Friendship friendship) ->
                friendship.getSenderUuid().equals(targetUuid) ||
                friendship.getReceiverUuid().equals(targetUuid)
            )
            .findFirst()
            .orElse(null);
    }

    /**
     * @param   targetUuid  Target's UUID
     * @return  True if player is involved in a friendship with the target.
     */
    public boolean hasFriendshipWith(String targetUuid)
    {
        return this.getFriendshipByUuid(targetUuid) != null;
    }

    /**
     * @return  Every friendship
     */
    public List<Friendship> getFriendships()
    {
        return this.friendships;
    }

}
