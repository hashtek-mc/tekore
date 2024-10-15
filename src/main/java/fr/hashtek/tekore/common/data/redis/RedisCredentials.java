package fr.hashtek.tekore.common.data.redis;

public class RedisCredentials
{

    private static final int DEFAULT_PORT = 6379;
    private static final String DEFAULT_CLIENT_NAME = "TEKORE_REDIS_ACCESS";

    private final String host;
    private final String password;
    private final int port;
    private final String clientName;


    /**
     * Creates a new credentials class for {@link RedisAccess},
     * with default port ({@link RedisCredentials#DEFAULT_PORT})
     * and default client name ({@link RedisCredentials#DEFAULT_CLIENT_NAME}).
     *
     * @param   host        Host (IP)
     * @param   password    Password
     */
    public RedisCredentials(
        String host,
        String password
    )
    {
        this(host, password, DEFAULT_PORT, DEFAULT_CLIENT_NAME);
    }

    /**
     * Creates a new credentials class for {@link RedisAccess}.
     *
     * @param   host        Host (IP)
     * @param   password    Password
     * @param   port        Port
     * @param   clientName  Client name
     */
    public RedisCredentials(
        String host,
        String password,
        int port,
        String clientName
    )
    {
        this.host = host;
        this.password = password;
        this.port = port;
        this.clientName = clientName;
    }


    /**
     * @return  Redis URL
     */
    public String toRedisUrl()
    {
        return "redis://" + host + ":" + port; // + "/" + clientName;
    }


    /**
     * @return  Host (IP)
     */
    public String getHost()
    {
        return host;
    }

    /**
     * @return  Password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @return  Port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @return  Client name
     */
    public String getClientName()
    {
        return clientName;
    }

}
