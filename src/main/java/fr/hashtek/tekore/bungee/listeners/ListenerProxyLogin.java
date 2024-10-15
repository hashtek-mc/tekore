package fr.hashtek.tekore.bungee.listeners;

import fr.hashtek.tekore.bungee.Tekord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerProxyLogin
    implements Listener
{

    private static final Tekord CORD = Tekord.getInstance();


    /**
     * Called when a player joins the proxy.
     */
    @EventHandler
    public void onLogin(PostLoginEvent event)
    {
        final ProxiedPlayer player = event.getPlayer();

        // TODO: Pull player's account from the API and store it in Redis.
        CORD.getPlayerManagersManager().createPlayerManager(player, CORD.getRedisAccess());
    }

}
