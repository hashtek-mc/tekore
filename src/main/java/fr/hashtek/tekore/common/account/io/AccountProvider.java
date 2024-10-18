package fr.hashtek.tekore.common.account.io;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.data.io.AbstractProvider;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exceptions.EntryNotFoundException;
import org.redisson.api.RMap;
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
     * Gets the UUID from the Redis database from a player's
     * username.
     *
     * @param   username    Player's username
     * @return  Fetched UUID
     * @throws  EntryNotFoundException  If account does not exist
     */
    public String getUuidFromUsername(String username)
        throws EntryNotFoundException
    {
        final String fetchedUuid;

        final RMap<String, String> uuidMap = super.getRedissonClient()
            .getMap(PREFIX_KEY + username);

        fetchedUuid = uuidMap.get(username);

        if (fetchedUuid == null) {
            throw new EntryNotFoundException(username);
        }
        return fetchedUuid;
    }

    /**
     * @param   uuid    UUID to test
     * @return  True if UUID is found in the database (if there is an account linked to that UUID)
     */
    public boolean hasAccount(String uuid)
    {
        return super.getRedissonClient().getKeys()
            .getKeys(KeysScanOptions.defaults().pattern(PREFIX_KEY + uuid))
            .iterator().hasNext();
    }

}
