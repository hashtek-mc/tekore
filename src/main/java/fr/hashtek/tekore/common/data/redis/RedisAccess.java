package fr.hashtek.tekore.common.data.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

public class RedisAccess
{

    private static final int DATABASE_INDEX = 0;

    private final RedissonClient redissonClient;


    /**
     * Creates a new Redis access.
     *
     * @param   credentials     Redis access credentials
     * @param   configuration   Redis access configuration
     */
    public RedisAccess(
        RedisCredentials credentials,
        RedisConfiguration configuration
    )
    {
        this.redissonClient = this.init(credentials, configuration);
    }


    /**
     * Initializes the connection to the Redis database.
     *
     * @param   credentials     Redis access credentials
     * @param   configuration   Redis access configuration
     * @return  Created Redisson client
     */
    public RedissonClient init(
        RedisCredentials credentials,
        RedisConfiguration configuration
    )
    {
        final Config redissonConfig = new Config();

        redissonConfig
            .setCodec(new JsonJacksonCodec())
            .setThreads(configuration.getThreads())
            .setNettyThreads(configuration.getNettyThreads())
            .useSingleServer()

            .setAddress(credentials.toRedisUrl())
            .setPassword(credentials.getPassword())
            .setDatabase(DATABASE_INDEX)
            .setClientName(credentials.getClientName());

        return Redisson.create(redissonConfig);
    }

    /**
     * Closes the connection to the Redis database.
     */
    public void close()
    {
        this.redissonClient.shutdown();
    }


    /**
     * @return  Redisson client
     */
    public RedissonClient getRedissonClient()
    {
        return redissonClient;
    }

}
