package fr.hashtek.tekore.common.guild;

import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.guild.io.GuildProvider;
import fr.hashtek.tekore.spigot.Tekore;

public class GuildManager
{

    private Guild currentGuild;


    /**
     * Creates a new Guild manager.
     *
     * @param   guildUuid   Associated (?) Guild UUID
     */
    public GuildManager(String guildUuid)
    {
        this.currentGuild = new Guild(guildUuid);
    }


    /**
     * Updates the Guild stored here.
     * <p>
     * Used for synchronization between players.
     *
     * @param   playerUuid  Player's UUID
     * @return  Itself
     */
    public GuildManager updateGuild(String playerUuid)
    {
        if (this.currentGuild != null) {
            this.updateGuild(this.currentGuild.getUuid(), playerUuid);
        }
        return this;
    }

    /**
     * Updates the Guild stored here.
     * <p>
     * Basically fetches the Guild from the Redis database and puts it in here.
     * <p>
     * Also verifies if the player is still in the Guild. If not, currentGuild will be set to null,
     * making the player guild-less.
     *
     * @param   guildUuid   UUID of the Guild to fetch
     * @param   playerUuid  Player's UUID
     * @return  Itself
     */
    public GuildManager updateGuild(String guildUuid, String playerUuid)
    {
        try {
            final Guild fetchedGuild = new GuildProvider(Tekore.getInstance().getRedisAccess())
                .get(guildUuid);

            if (fetchedGuild.getMemberProfile(playerUuid) == null) {
                return this;
            }

            this.currentGuild = fetchedGuild;
            /* Update player's rank to bring it out of its primitive state. */
            this.currentGuild.getMemberProfile(playerUuid)
                .setRank(this.currentGuild);
        }
        catch (EntryNotFoundException exception) {
            this.currentGuild = null;
        }
        return this;
    }

    /**
     * @return  Current Guild
     */
    public Guild getCurrentGuild()
    {
        return this.currentGuild;
    }

    /**
     * @param   currentGuild    New current Guild
     */
    public void setCurrentGuild(Guild currentGuild)
    {
        this.currentGuild = currentGuild;
    }

}
