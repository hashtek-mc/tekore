package fr.hashtek.tekore.common.guild.io;

import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.data.io.AbstractPublisher;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.guild.Guild;

public class GuildPublisher
    extends AbstractPublisher<Guild>
{

    public GuildPublisher(RedisAccess redisAccess)
    {
        super(redisAccess, Constants.GUILD_PREFIX_KEY);
    }


    /**
     * @param   guild   Guild to push
     */
    public void push(Guild guild)
    {
        super.push(guild.getUuid(), guild);
    }

}
