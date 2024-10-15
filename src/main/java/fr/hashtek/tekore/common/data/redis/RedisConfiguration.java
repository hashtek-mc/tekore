package fr.hashtek.tekore.common.data.redis;

public class RedisConfiguration
{

    private static final int MAX_THREADS =
        Runtime.getRuntime().availableProcessors() * 2;

    private final int threads;
    private final int nettyThreads;


    /**
     * Creates a new configuration for {@link RedisAccess},
     * with the maximum amount of threads.
     */
    public RedisConfiguration()
    {
        this(MAX_THREADS, MAX_THREADS);
    }

    /**
     * Creates a new configuration for {@link RedisAccess}.
     *
     * @param   threads         Threads
     * @param   nettyThreads    Netty threads
     */
    public RedisConfiguration(int threads, int nettyThreads)
    {
        this.threads = threads;
        this.nettyThreads = nettyThreads;
    }


    /**
     * @return  Threads
     */
    public int getThreads()
    {
        return threads;
    }

    /**
     * @return  Netty threads
     */
    public int getNettyThreads()
    {
        return nettyThreads;
    }
}
