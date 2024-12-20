package fr.hashtek.tekore.spigot.listener;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerQuit
    implements Listener, HashLoggable
{

    private static final Tekore CORE = Tekore.getInstance();


    /**
     * Called when a player quits a server.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        final Player player = event.getPlayer();
        final HashLogger logger = CORE.getHashLogger();

        logger.info(this, "\"" + player.getName() + "\" logged out, launching logout sequence...");

        /* Push RAM-stored data to the Redis database, and then free the RAM. */
        CORE.getPlayerManagersManager()
            .removePlayerManager(player.getUniqueId().toString())
            .pushData(CORE.getRedisAccess());

        logger.info(this, "Logout sequence successfully executed for \"" + player.getName() + "\".");
    }

}
