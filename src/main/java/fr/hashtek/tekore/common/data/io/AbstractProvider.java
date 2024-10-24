package fr.hashtek.tekore.common.data.io;

import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exceptions.EntryNotFoundException;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/**
 * Basically a class that retrieves some data
 * (of a certain type T) from the Redis database.
 *
 * @param   <T> Type of data to fetch
 */
public abstract class AbstractProvider
    <T>
{

    private final RedisAccess redisAccess;
    private final String keyPrefix;


    /**
     * Creates a new Provider.
     *
     * @param   redisAccess     Redis access
     * @param   keyPrefix       Key prefix
     *                          </br>
     *                          For example, in <code>accounts:{uuid}</code>,
     *                          <code>accounts:</code> is the key prefix.
     */
    public AbstractProvider(
        RedisAccess redisAccess,
        String keyPrefix
    )
    {
        this.redisAccess = redisAccess;
        this.keyPrefix = keyPrefix;
    }


    /**
     * @param   key     Key
     * @return  Fetched data
     * @throws  EntryNotFoundException  If no data could be fetched
     */
    public T get(String key)
        throws EntryNotFoundException
    {
        final String redisKey = this.keyPrefix + key;
        final RBucket<T> accountRBucket = this.getRedissonClient().getBucket(redisKey);
        final T data = accountRBucket.get();

        /* If the data was not found in the Redis database, throw error. */
        if (data == null) {
            throw new EntryNotFoundException(redisKey);
        }

        return data;
    }


    /**
     * @return  Provider's Redis access
     */
    protected RedisAccess getRedisAccess()
    {
        return this.redisAccess;
    }

    /**
     * @return  Provider's Redisson client
     */
    protected RedissonClient getRedissonClient()
    {
        return this.redisAccess.getRedissonClient();
    }

}
