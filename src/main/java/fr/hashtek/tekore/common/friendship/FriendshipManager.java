package fr.hashtek.tekore.common.friendship;

import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exceptions.EntryNotFoundException;
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
    private void fetchFriendships()
    {
        final List<Friendship> playerFriendships = new FriendshipProvider(REDIS_ACCESS)
            .getPlayerFriendships(this.playerUuid);

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
        final FriendshipPublisher publisher = new FriendshipPublisher(CORE.getRedisAccess());

        /* Push the friendship */
        publisher.push(friendship);

        /* And add the friendship's UUID to player's friendship list. */
        publisher.involvePlayerIntoFriendship(this.playerUuid, friendship);
        publisher.involvePlayerIntoFriendship(targetUuid, friendship);
    }

    /**
     * @param   friendshipUuid  Friendship's UUID
     * @return  If exists, fetched friendship. Otherwise, null.
     */
    public Friendship getFriendship(String friendshipUuid)
    {
        return this.friendships.stream()
            .filter(friendship -> friendship.getUuid().equals(friendshipUuid))
            .findFirst()
            .orElse(null);
    }

    /**
     * @return  Every friendship
     */
    public List<Friendship> getFriendships()
    {
        return this.friendships;
    }

}
