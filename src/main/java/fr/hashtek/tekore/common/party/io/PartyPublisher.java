package fr.hashtek.tekore.common.party.io;

import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.data.io.AbstractPublisher;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.party.Party;

public class PartyPublisher
    extends AbstractPublisher<Party>
{

    public PartyPublisher(RedisAccess redisAccess)
    {
        super(redisAccess, Constants.PARTY_PREFIX_KEY);
    }


    /**
     * @param   party   Party to push
     */
    public void push(Party party)
    {
        super.push(party.getUuid(), party);
    }

}
