package fr.hashtek.tekore.common.friendship.io;

import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.data.io.AbstractPublisher;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.friendship.Friendship;

public class FriendshipPublisher
    extends AbstractPublisher<Friendship>
{

    public FriendshipPublisher(RedisAccess redisAccess)
    {
        super(redisAccess, Constants.FRIENDSHIP_PREFIX_KEY);
    }


    /**
     * @param   friendship  Friendship to push
     */
    public void push(Friendship friendship)
    {
        super.push(friendship.getUuid(), friendship);
    }

    /**
     * Used for easier and faster Redis querying, "involve" is
     * a prefix key used in the Redis database that basically returns
     * a {@link org.redisson.api.RSet<String>} of Friendship UUIDs, in
     * which the player is "involved" (is either the sender or the
     * receiver).
     *
     * @param   playerUuid  Player's UUID
     * @param   friendship  Friendship to add
     */
    public void involvePlayerIntoFriendship(
        String playerUuid,
        Friendship friendship
    )
    {
        super.getRedissonClient()
            .getSet(Constants.FRIENDSHIP_PREFIX_KEY + Constants.FRIENDSHIP_INVOLVES_PREFIX_KEY + playerUuid)
            .add(friendship.getUuid());
    }

    /**
     * Disengages a player from a friendship.
     * <p>
     * Does NOT delete the friendship, this method simply removes the friendship
     * from the "involve" Set stored for faster querying.
     * <p>
     * See {@link FriendshipPublisher#involvePlayerIntoFriendship(String, Friendship)}.
     *
     * @param   playerUuid      Player's UUID
     * @param   friendshipUuid  UUID of friendship to remove
     */
    public void disengagePlayerFromFriendship(
        String playerUuid,
        String friendshipUuid
    )
    {
        super.getRedissonClient()
            .getSet(Constants.FRIENDSHIP_PREFIX_KEY + Constants.FRIENDSHIP_INVOLVES_PREFIX_KEY + playerUuid)
            .remove(friendshipUuid);
    }

}
