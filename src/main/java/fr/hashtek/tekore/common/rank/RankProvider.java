package fr.hashtek.tekore.common.rank;

import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.data.io.AbstractProvider;
import fr.hashtek.tekore.common.data.redis.RedisAccess;

public class RankProvider
    extends AbstractProvider<Rank>
{

    public RankProvider(RedisAccess redisAccess)
    {
        super(redisAccess, Constants.RANK_PREFIX_KEY);
    }

}
