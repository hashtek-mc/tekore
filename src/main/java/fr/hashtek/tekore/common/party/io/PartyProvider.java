package fr.hashtek.tekore.common.party.io;

import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.data.io.AbstractProvider;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.party.Party;

public class PartyProvider
    extends AbstractProvider<Party>
{

    public PartyProvider(RedisAccess redisAccess)
    {
        super(redisAccess, Constants.PARTY_PREFIX_KEY);
    }

}
