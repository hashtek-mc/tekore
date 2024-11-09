package fr.hashtek.tekore.common.rank;

import fr.hashtek.tekore.common.account.Account;

import java.util.List;

public class Rank
{

    private final String uuid;
    private final String name;
    private final String fullName;
    private final String shortName;
    private final String usernameColor;
    private final List<String> permissions;


    /**
     * Creates an empty rank.
     *
     * @apiNote Solely used for Redis stuff, not for public use.
     */
    public Rank()
    {
        this("", "", "", "", "", null);
    }

    /**
     * Creates an empty Rank, just with the UUID.
     * </br>
     * Basically only used at the very first state of
     * an {@link Account}, during its creation.
     *
     * @param   uuid    Rank's UUID
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Rank(String uuid)
    {
        this(uuid, "", "", "", "", null);
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
     */
    public Rank(
        String uuid,
        String name,
        String fullName,
        String shortName,
        String usernameColor,
        List<String> permissions
    )
    {
        this.uuid = uuid;
        this.name = name;
        this.fullName = fullName;
        this.shortName = shortName;
        this.usernameColor = usernameColor;
        this.permissions = permissions;
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
     * @param   permission  Permission to check
     * @return  True if rank has the given permission
     */
    public boolean hasPermission(String permission)
    {
        /* If rank directly has the permission or have every permission, return true. */
        if (this.permissions.contains("*") || this.permissions.contains(permission)) {
            return true;
        }

        /* Globbing system (wildcard *) ------------------------------------ */
        int dotIndex = -1;

        do {
            dotIndex = permission.indexOf('.', dotIndex + 1);
            final String prefix = permission.substring(0, dotIndex) + ".*";

            if (this.permissions.contains(prefix)) {
                return true;
            }
        } while (dotIndex != -1);
        /* ----------------------------------------------------------------- */

        return false;
    }

}
