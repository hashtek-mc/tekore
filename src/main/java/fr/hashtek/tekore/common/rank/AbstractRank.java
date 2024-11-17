package fr.hashtek.tekore.common.rank;

import java.util.Set;

public abstract class AbstractRank
{

    private final String uuid;
    private final String name;
    private final Set<String> permissions;


    /**
     * Creates a new empty rank.
     * @apiNote Solely used for Redis stuff. Not for public use!
     */
    public AbstractRank()
    {
        this("", "", null);
    }

    /**
     * Creates an empty Rank, just with the UUID.
     * <br>
     * Basically only used at the very first state of
     * an {@link fr.hashtek.tekore.common.account.Account}, during its creation.
     * @param   uuid    Rank's UUID
     * @apiNote Solely used for Redis stuff. Not for public use!
     */
    public AbstractRank(String uuid)
    {
        this(uuid, "", null);
    }

    /**
     * Creates a new Abstract Rank.
     *
     * @param   uuid        Rank's UUID
     * @param   name        Rank's name
     * @param   permissions Rank's permissions
     */
    public AbstractRank(
        String uuid,
        String name,
        Set<String> permissions
    )
    {
        this.uuid = uuid;
        this.name = name;
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
     * @return  Rank's name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return  Rank's permissions
     */
    public Set<String> getPermissions()
    {
        if (this.permissions.isEmpty()) {
            return null;
        }
        return this.permissions;
    }

    /**
     * @param   permission  Permission to check
     * @return  True if rank has the given permission
     */
    public boolean hasPermission(String permission)
    {
        if (this.permissions.isEmpty()) {
            return false;
        }

        if (permission == null ||
            permission.isEmpty() ||
            this.permissions.contains("*") ||
            this.permissions.contains(permission)
        ) {
            return true;
        }

        /* Globbing system (wildcard *) ------------------------------------ */
        int dotIndex = -1;

        do {
            dotIndex = permission.indexOf('.', dotIndex + 1);
            if (dotIndex == -1) {
                break;
            }

            final String prefix = permission.substring(0, dotIndex) + ".*";

            if (this.permissions.contains(prefix)) {
                return true;
            }
        } while (dotIndex != -1);
        /* ----------------------------------------------------------------- */

        return false;
    }

}
