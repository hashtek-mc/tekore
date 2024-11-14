package fr.hashtek.tekore.common.account.io;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.data.io.AbstractPublisher;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import org.redisson.api.RBucket;

public class AccountPublisher
    extends AbstractPublisher<Account>
{

    private static final String PREFIX_KEY = Constants.ACCOUNT_PREFIX_KEY;


    public AccountPublisher(RedisAccess redisAccess)
    {
        super(redisAccess, PREFIX_KEY);
    }


    /**
     * @apiNote Prefer using {@link AccountPublisher#push(String, String, Account)}. Please.
     */
    @Override
    public void push(String uuid, Account account)
    {
        super.push(uuid, account);
    }

    /**
     * Pushes a given account to the Redis database.
     *
     * @param   username    Account's username
     * @param   uuid        Account's UUID
     * @param   account     Account to push
     */
    public void push(String username, String uuid, Account account)
    {
        super.push(uuid, account);
        this.pushUsername(account.getUsername(), account.getUuid());
    }

    /**
     * Stores the username in a Map in the Redis database.
     * <p>
     * Used for faster UUID querying from username.
     *
     * @param   username    Username
     * @param   uuid        UUID
     */
    private void pushUsername(String username, String uuid)
    {
        final RBucket<String> rBucket = super.getRedissonClient()
            .getBucket(PREFIX_KEY + username.toLowerCase());

        rBucket.set(uuid);
    }

}
