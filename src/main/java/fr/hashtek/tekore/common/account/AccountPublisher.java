package fr.hashtek.tekore.common.account;

import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.data.io.AbstractPublisher;
import fr.hashtek.tekore.common.data.redis.RedisAccess;

public class AccountPublisher
    extends AbstractPublisher<Account>
{

    public AccountPublisher(RedisAccess redisAccess)
    {
        super(redisAccess, Constants.ACCOUNT_PREFIX_KEY);
    }

}
