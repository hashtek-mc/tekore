package fr.hashtek.tekore.common.account;

import java.util.List;

public class Rank
{

    private final String uuid;
    private final String name;
    private final String fullName;
    private final String shortName;
    private final String usernameColor;
    private final List<String> permissions;
    private final boolean isStaff;


    /**
     * Creates an empty Rank, just with the UUID.
     * </br>
     * Basically only used at the very first state of
     * an {@link Account}, during its creation.
     *
     * @param   uuid    Rank's UUID
     */
    public Rank(String uuid)
    {
        this(uuid, "", "", "", "", null, false);
    }

    /**
     * Creates a new Rank.
     *
     * @param   uuid            Rank's UUID
     * @param   name            Rank's raw name (used for database for example)
     * @param   fullName        Rank's full name (in chat)
     * @param   shortName       Rank's short name (for tablist)
     * @param   usernameColor   Username color
     * @param   permissions     Rank's permissions
     * @param   isStaff         Is rank a staff?
     */
    public Rank(
        String uuid,
        String name,
        String fullName,
        String shortName,
        String usernameColor,
        List<String> permissions,
        boolean isStaff
    )
    {
        this.uuid = uuid;
        this.name = name;
        this.fullName = fullName;
        this.shortName = shortName;
        this.usernameColor = usernameColor;
        this.permissions = permissions;
        this.isStaff = isStaff;
    }

    /**
     * @return  Rank's UUID
     */
    public String getUuid()
    {
        return this.uuid;
    }

    /**
     * @return  Rank's raw name (used for database for example)
     */
    public String getName()
    {
        return this.name;
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

    /**
     * @return  Rank's permissions
     */
    public List<String> getPermissions()
    {
        return this.permissions;
    }

    /**
     * @return  Is rank a staff?
     */
    public boolean isStaff()
    {
        return this.isStaff;
    }

}
