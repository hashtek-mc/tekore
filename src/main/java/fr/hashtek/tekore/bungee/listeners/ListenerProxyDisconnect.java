package fr.hashtek.tekore.bungee.listeners;

import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.account.AccountPublisher;
import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.player.PlayerManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
        // TODO: Push player's account to the API.
        CORD.getProxy().getScheduler().schedule(CORD, () -> {
            final ProxiedPlayer player = event.getPlayer();

            final PlayerManager<ProxiedPlayer> playerManager =
                CORD.getPlayerManagersManager().getPlayerManager(player);

            new AccountPublisher()
                .sendAccountToApi(playerManager.getAccount())
                .removeAccountFromRedis(
                    player.getUniqueId().toString(),
                    CORD.getRedisAccess()
                );
        }, Constants.FINAL_PUSH_TIME, Constants.FINAL_PUSH_TIME_UNIT);
    }

}
