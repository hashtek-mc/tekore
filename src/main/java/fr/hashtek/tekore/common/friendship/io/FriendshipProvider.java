package fr.hashtek.tekore.common.friendship.io;

import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.data.io.AbstractProvider;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exceptions.EntryNotFoundException;
import fr.hashtek.tekore.common.friendship.Friendship;
import org.redisson.api.RSet;

import java.util.ArrayList;
import java.util.List;

public class FriendshipProvider
    extends AbstractProvider<Friendship>
{

    public FriendshipProvider(RedisAccess redisAccess)
    {
        super(redisAccess, Constants.FRIENDSHIP_PREFIX_KEY);
    }


    /**
     * @param   playerUuid  Player's UUID
     * @return  Friendship the player got
     */
    public List<Friendship> getPlayerFriendships(String playerUuid)
    {
        final List<Friendship> friendships = new ArrayList<Friendship>();

        final RSet<String> involvedFriendships = super.getRedissonClient().getSet(
            Constants.FRIENDSHIP_PREFIX_KEY + Constants.FRIENDSHIP_INVOLVES_PREFIX_KEY + playerUuid
        );

        involvedFriendships.readAll()
            .forEach(friendshipUuid -> {
                try {
                    friendships.add(super.get(friendshipUuid));
                }
                catch (EntryNotFoundException exception) {
                    // TODO: Log.
                }
            });

        return friendships;
    }

}
