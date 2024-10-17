package fr.hashtek.tekore.common.account.io;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.data.io.AbstractProvider;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
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
