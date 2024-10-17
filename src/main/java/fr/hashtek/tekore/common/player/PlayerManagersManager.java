package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exceptions.InvalidPlayerType;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Find a new class name for this, PLEASE.
 * @param   <T> MUST be an instance of either {@link org.bukkit.entity.Player}
 *              or {@link net.md_5.bungee.api.connection.ProxiedPlayer}.
 */
public class PlayerManagersManager
    <T>
{

    private final Map<String, PlayerManager<T>> playerManagers;


    /**
     * Creates a new PlayerManager manager.
     */
    public PlayerManagersManager(RedisAccess redisAccess)
    {
        this.playerManagers = new HashMap<String, PlayerManager<T>>();
    }


    /**
     * @param   player  Player
     * @param   <P>     MUST be an instance of either {@link org.bukkit.entity.Player}
     *                  or {@link net.md_5.bungee.api.connection.ProxiedPlayer}.
     * @return  Player's UUID
     * @throws  InvalidPlayerType   If player type is invalid
     */
    public static <P> String getUuid(P player)
        throws InvalidPlayerType
    {
        String uuid;

        try {
            if (!(player instanceof org.bukkit.entity.Player p)) {
                throw new NoClassDefFoundError();
            }
            uuid = p.getUniqueId().toString();
        }
        catch (NoClassDefFoundError unused) {
            try {
                if (!(player instanceof net.md_5.bungee.api.connection.ProxiedPlayer p)) {
                    throw new NoClassDefFoundError();
                }
                uuid = p.getUniqueId().toString();
            }
            catch (NoClassDefFoundError exception) {
                throw new InvalidPlayerType(player);
            }
        }

        return uuid;
    }

    /**
     * @param   player  Player
     * @param   <P>     MUST be an instance of either {@link org.bukkit.entity.Player}
     *                  or {@link net.md_5.bungee.api.connection.ProxiedPlayer}.
     * @return  Player's username
     * @throws  InvalidPlayerType   If player type is invalid
     */
    public static <P> String getUsername(P player)
        throws InvalidPlayerType
    {
        String name;

        try {
            if (!(player instanceof org.bukkit.entity.Player p)) {
                throw new NoClassDefFoundError();
            }
            name = p.getName();
        }
        catch (NoClassDefFoundError unused) {
            try {
                if (!(player instanceof net.md_5.bungee.api.connection.ProxiedPlayer p)) {
                    throw new NoClassDefFoundError();
                }
                name = p.getName();
            }
            catch (NoClassDefFoundError exception) {
                throw new InvalidPlayerType(player);
            }
        }

        return name;
    }


    /**
     * @param   playerUuid  Player's UUID
     * @return  Associated player manager
     * @apiNote Prefer using {@link PlayerManagersManager#getPlayerManager(T)}
     */
    public PlayerManager<T> getPlayerManager(String playerUuid)
    {
        return this.playerManagers.get(playerUuid);
    }

    /**
     * @param   player  Player
     * @return  Associated player manager
     */
    public PlayerManager<T> getPlayerManager(T player)
    {
        try {
            return this.getPlayerManager(getUuid(player));
        } catch (InvalidPlayerType unused) {
            // Should NEVER fire. But maybe log. You know... just in case ¬‿¬
        }
        return null;
    }

    public PlayerManager<T> createPlayerManager(
        T player,
        RedisAccess redisAccess
    )
    {
        try {
            final String playerUuid = getUuid(player);
            final PlayerManager<T> playerManager = new PlayerManager<T>(player, redisAccess);

            /*
             * If player is already in the map, overwrite the data inside.
             * Should never happen, but we're preventive ;)
             */
            if (!this.playerManagers.containsKey(playerUuid)) {
                this.removePlayerManager(playerUuid);
            }
            this.playerManagers.put(playerUuid, playerManager);

            return playerManager;
        }
        catch (InvalidPlayerType unused) {
            // Should NEVER fire. But maybe log. You know... just in case ¬‿¬
        }
        return null;
    }

    /**
     * @param   playerUuid  Player's UUID
     * @return  Player manager that has just been deleted.
     */
    public PlayerManager<T> removePlayerManager(String playerUuid)
    {
        final PlayerManager<T> playerManager = this.playerManagers.get(playerUuid);

        /* If player has no manager associated, ignore the rest. */
        if (playerManager == null) {
            return null;
        }

        return this.playerManagers.remove(playerUuid);
    }

}
