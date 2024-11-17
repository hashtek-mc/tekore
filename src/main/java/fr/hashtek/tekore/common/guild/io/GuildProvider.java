package fr.hashtek.tekore.common.guild.io;

import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.data.io.AbstractProvider;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.guild.Guild;

public class GuildProvider
    extends AbstractProvider<Guild>
{

    public GuildProvider(RedisAccess redisAccess)
    {
        super(redisAccess, Constants.GUILD_PREFIX_KEY);
    }

}
