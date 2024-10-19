package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.data.redis.RedisAccess;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Find a new class name for this, PLEASE.
 */
public class PlayerManagersManager
{

    private final Map<String, PlayerManager> playerManagers;


    /**
     * Creates a new PlayerManager manager.
     */
    public PlayerManagersManager()
    {
        this.playerManagers = new HashMap<String, PlayerManager>();
    }


    /**
     * @param   playerUuid  Player's UUID
     * @return  Associated player manager
     * @apiNote Prefer using {@link PlayerManagersManager#getPlayerManager(Player)}
     */
    public PlayerManager getPlayerManager(String playerUuid)
    {
        return this.playerManagers.get(playerUuid);
    }

    /**
     * @param   player  Player
     * @return  Associated player manager
     */
    public PlayerManager getPlayerManager(Player player)
    {
        return this.getPlayerManager(player.getUniqueId().toString());
    }

    /**
     * Creates a new Player manager for a player.
     *
     * @param   player          Player to create the manager for
     * @param   redisAccess     Redis access
     * @return  Created Player manager
     */
    public PlayerManager createPlayerManager(
        Player player,
        RedisAccess redisAccess
    )
    {
        final String playerUuid = player.getUniqueId().toString();
        final PlayerManager playerManager = new PlayerManager(player, redisAccess);

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

    /**
     * @param   playerUuid  Player's UUID
     * @return  Player manager that has just been deleted.
     */
    public PlayerManager removePlayerManager(String playerUuid)
    {
        final PlayerManager playerManager = this.playerManagers.get(playerUuid);

        /* If player has no manager associated, ignore the rest. */
        if (playerManager == null) {
            return null;
        }

        return this.playerManagers.remove(playerUuid);
    }

}
