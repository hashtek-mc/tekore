package fr.hashtek.tekore.common.account.io;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.data.io.AbstractProvider;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import org.redisson.api.RBucket;
import org.redisson.api.options.KeysScanOptions;

public class AccountProvider
    extends AbstractProvider<Account>
{

    private static final String PREFIX_KEY = Constants.ACCOUNT_PREFIX_KEY;


    public AccountProvider(RedisAccess redisAccess)
    {
        super(redisAccess, PREFIX_KEY);
    }


    /**
     * Gets the username of a player from the Redis database
     * from its UUID.
     *
     * @param   uuid    Player's UUID
     * @return  Fetched username
     * @throws  EntryNotFoundException  If account does not exist
     */
    public String getUsernameFromUuid(String uuid)
        throws EntryNotFoundException
    {
        if (uuid == null) {
            throw new EntryNotFoundException(null);
        }

        return super.get(uuid).getUsername();
    }

    /**
     * Gets the UUID of a player from the Redis database from
     * its username.
     *
     * @param   username    Player's username
     * @return  Fetched UUID
     * @throws  EntryNotFoundException  If account does not exist
     */
    public String getUuidFromUsername(String username)
        throws EntryNotFoundException
    {
        if (username == null) {
            throw new EntryNotFoundException(null);
        }

        final String fetchedUuid;

        final RBucket<String> rBucket = super.getRedissonClient()
            .getBucket(PREFIX_KEY + username.toLowerCase());

        fetchedUuid = rBucket.get();

        if (fetchedUuid == null) {
            throw new EntryNotFoundException(username);
        }
        return fetchedUuid;
    }

    /**
     * @param   tag     Can be a UUID or a username
     * @return  True if tag is found in the database (if there is an account linked to that tag)
     */
    public boolean hasAccount(String tag)
    {
        if (tag == null) {
            return false;
        }

        return super.getRedissonClient().getKeys()
            .getKeys(KeysScanOptions.defaults().pattern(PREFIX_KEY + tag))
            .iterator().hasNext();
    }

}
