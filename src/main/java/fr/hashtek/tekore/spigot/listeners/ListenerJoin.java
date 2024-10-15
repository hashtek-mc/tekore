package fr.hashtek.tekore.spigot.listeners;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

public class ListenerJoin
    implements Listener, HashLoggable
{

    private static final Tekore CORE = Tekore.getInstance();


    /**
     * Called when a player joins a server.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();
        final HashLogger logger = CORE.getHashLogger();

        logger.info(this, "\"" + player.getName() + "\" logged in, launching login sequence...");

        /* Create a player manager for the player (so, pull the data from Redis) and put it in the Map. */
        CORE.getPlayerManagersManager().createPlayerManager(player, CORE.getRedisAccess());

        logger.info(this, "Login sequence successfully executed for \"" + player.getName() + "\".");
    }

}
