package fr.hashtek.tekore.common.account;

import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class AccountPublisher
{

    public AccountPublisher sendAccountToRedis(
        Account account,
        RedisAccess redisAccess
    )
    {
        return this.sendAccountToRedis(account, account.getUuid(), redisAccess);
    }

    /**
     * Pushes the account data to the Redis database.
     *
     * @param   account         Account to push
     * @param   redisAccess     Redis access
     */
    public AccountPublisher sendAccountToRedis(
        Account account,
        String uuid,
        RedisAccess redisAccess
    )
    {
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        final String key = Constants.REDIS_ACCOUNT_KEY + uuid;
        final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

        accountRBucket.set(account);
        return this;
    }

    /**
     * Removes the account entry from the Redis database.
     *
     * @param   uuid            Account to remove
     * @param   redisAccess     Redis access
     */
    public AccountPublisher removeAccountFromRedis(
        String uuid,
        RedisAccess redisAccess
    )
    {
        return this.sendAccountToRedis(null, uuid, redisAccess);
    }

    /**
     * Pushes the account data to the API.
     *
     * @param   account     Account to push
     */
    public AccountPublisher sendAccountToApi(
        Account account
    )
    {
        // TODO: Finish this when API is done.
        return this;
    }

}
