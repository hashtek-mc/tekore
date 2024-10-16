package fr.hashtek.tekore.common.account;

import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exceptions.AccountNotFoundException;
import org.redisson.api.RBucket;

public class AccountProvider
{

    /**
     * Gets the account of a player (targeted via its UUID),
     * first from the Redis database (from cache), and from the
     * API if the account is not found in the Redis database and
     * ultimately pushes the account in it.
     *
     * @param   uuid            Player's UUID
     * @param   redisAccess     Redis access
     * @return  Found account
     * @throws  AccountNotFoundException    If the account was not found in both the Redis database and the API
     */
    public Account getAccount(
        String uuid,
        RedisAccess redisAccess
    )
        throws AccountNotFoundException
    {
        Account account = null;

        try {
            /* First step: Try to fetch the account from the Redis database. */
            account = this.getAccountFromRedis(uuid, redisAccess);
        }
        catch (AccountNotFoundException unused) {
            /* Second step: Try to fetch the account from the API. */
            account = this.getAccountFromApi(uuid);
        }

        return account;
    }


    /**
     * Gets the account from the Redis database.
     *
     * @param   uuid            Player's UUID
     * @param   redisAccess     Redis access
     * @return  Found account
     * @throws  AccountNotFoundException    If the account was not found in the Redis database
     */
    private Account getAccountFromRedis(
        String uuid,
        RedisAccess redisAccess
    )
        throws AccountNotFoundException
    {
        final String key = Constants.REDIS_ACCOUNT_KEY + uuid;
        final RBucket<Account> accountRBucket = redisAccess.getRedissonClient().getBucket(key);
        final Account account = accountRBucket.get();

        /* If the account was not found in the Redis database, throw error. */
        if (account == null) {
            throw new AccountNotFoundException(uuid);
        }

        return account;
    }

    /**
     * Gets the account from the API.
     *
     * @param   uuid    Player's UUID
     * @return  Found account
     * @throws  AccountNotFoundException    If the account was not found in the API.
     */
    private Account getAccountFromApi(
        String uuid
    )
        throws AccountNotFoundException
    {
        // TODO: Fetch account from the API, when it is finished.
        throw new AccountNotFoundException(uuid);
    }

}
