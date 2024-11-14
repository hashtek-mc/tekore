package fr.hashtek.tekore.common.rank;

import java.util.Set;

public class Rank
    extends AbstractRank
{

    private final String fullName;
    private final String shortName;
    private final String usernameColor;


    /**
     * @see AbstractRank#AbstractRank()
     */
    public Rank()
    {
        this(null);
    }

    /**
     * @see AbstractRank#AbstractRank(String)
     */
    public Rank(String uuid)
    {
        super(uuid);
        this.fullName = null;
        this.shortName = null;
        this.usernameColor = null;
    }

    /**
     * Creates a new Rank.
     *
     * @param   uuid            Rank's UUID
     * @param   name            Rank's raw name (used in database)
     * @param   fullName        Rank's full name (in chat)
     * @param   shortName       Rank's short name (for tablist)
     * @param   usernameColor   Username color
     * @param   permissions     Rank's permissions
     */
    public Rank(
        String uuid,
        String name,
        String fullName,
        String shortName,
        String usernameColor,
        Set<String> permissions
    )
    {
        super(uuid, name, permissions);
        this.fullName = fullName;
        this.shortName = shortName;
        this.usernameColor = usernameColor;
    }


    /**
     * @return  Rank's full name (in chat)
     */
    public String getFullName()
    {
        return this.fullName;
    }

    /**
     * @return  Rank's short name (for tablist)
     */
    public String getShortName()
    {
        return this.shortName;
    }

    /**
     * @return  Username color
     */
    public String getUsernameColor()
    {
        return this.usernameColor;
    }

}
