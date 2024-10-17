package fr.hashtek.tekore.common.account;

import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.data.a.AbstractProvider;
import fr.hashtek.tekore.common.data.redis.RedisAccess;

public class AccountProvider
    extends AbstractProvider<Account>
{

    public AccountProvider(RedisAccess redisAccess)
    {
        super(redisAccess, Constants.ACCOUNT_PREFIX_KEY);
    }

}
