package fr.hashtek.tekore.common.constants;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class Constants
{

    public static final Timestamp DEFAULT_TIMESTAMP = new Timestamp(0);

    /* Account stuff */
    public static final String ACCOUNT_KEY = "accounts";
    public static final String REDIS_ACCOUNT_KEY = ACCOUNT_KEY + ":";
    public static final String API_ACCOUNT_KEY = ACCOUNT_KEY + "/";

    /* Final push is the API push on Proxy disconnect (and ultimately the Redis key removal). */
    public static final int FINAL_PUSH_TIME = 5;
    public static final TimeUnit FINAL_PUSH_TIME_UNIT = TimeUnit.SECONDS;

}
