package fr.hashtek.tekore.common.data.io;

import fr.hashtek.tekore.common.data.redis.RedisAccess;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.time.Duration;

public abstract class AbstractPublisher
    <T>
{

    private final RedisAccess redisAccess;
    private final String keyPrefix;


    /**
     * Creates a new Publisher.
     * <p>
     * Basically is a class that pushes some data
     * (of a certain type T) to the Redis database.
     * </p>
     *
     * @param   redisAccess     Redis access
     * @param   keyPrefix       Key prefix
     *                          </br>
     *                          For example, in <code>accounts:{uuid}</code>,
     *                          <code>accounts:</code> is the key prefix.
     */
    public AbstractPublisher(
        RedisAccess redisAccess,
        String keyPrefix
    )
    {
        this.redisAccess = redisAccess;
        this.keyPrefix = keyPrefix;
    }


    /**
     * @param   key     Key
     * @param   data    Data to push
     */
    public void push(
        String key,
        T data
    )
    {
        this.push(key, data, null);
    }

    /**
     * @param   key         Key
     * @param   data        Data to push
     * @param   timeout     Time after which the data will be deleted from the Redis database
     */
    public void push(
        String key,
        T data,
        Duration timeout
    )
    {
        final String redisKey = this.keyPrefix + key;
        final RBucket<T> bucket = redisAccess.getRedissonClient().getBucket(redisKey);

        if (timeout == null) {
            bucket.set(data);
            return;
        }
        bucket.set(data, timeout);
    }

    /**
     * Deletes the key from the Redis database.
     *
     * @param   key     Key
     */
    public void remove(String key)
    {
        this.push(key, null);
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
