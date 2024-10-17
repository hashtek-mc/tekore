package fr.hashtek.tekore.bungee.listeners;

import fr.hashtek.tekore.bungee.Tekord;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerProxyDisconnect
    implements Listener
{

    private static final Tekord CORD = Tekord.getInstance();

    /**
     * Called when a player disconnects from the proxy.
     */
    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event)
    {
        // ...
    }

}
