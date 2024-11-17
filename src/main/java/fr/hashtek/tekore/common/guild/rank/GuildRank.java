package fr.hashtek.tekore.common.guild.rank;

import fr.hashtek.tekore.common.rank.AbstractRank;

import java.util.Set;

public class GuildRank
    extends AbstractRank
{

    private final String displayName;


    /**
     * @see AbstractRank#AbstractRank()
     */
    public GuildRank()
    {
        this(null);
    }

    /**
     * @see AbstractRank#AbstractRank(String)
     */
    public GuildRank(String uuid)
    {
        super(uuid);
        this.displayName = null;
    }

    /**
     * Creates a new Guild rank.
     *
     * @param   uuid        Rank's UUID
     * @param   name        Rank's raw name (used in database)
     * @param   displayName Rank's display name
     * @param   permissions Rank's permissions
     */
    public GuildRank(
        String uuid,
        String name,
        String displayName,
        Set<String> permissions
    )
    {
        super(uuid, name, permissions);
        this.displayName = displayName;
    }


    /**
     * @return  Rank's display name
     */
    public String getDisplayName()
    {
        return this.displayName;
    }

}
