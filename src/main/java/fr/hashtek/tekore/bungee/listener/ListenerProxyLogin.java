package fr.hashtek.tekore.bungee.listener;

import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.friendship.Friendship;
import fr.hashtek.tekore.common.friendship.FriendshipRequestState;
import fr.hashtek.tekore.common.friendship.io.FriendshipProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.UUID;

public class ListenerProxyLogin
    implements Listener
{

    private static final Tekord CORD = Tekord.getInstance();


    /**
     * Broacasts the login to player's friends.
     *
     * @param   player  Player
     */
    private void broadcastToFriends(ProxiedPlayer player)
    {
        final List<Friendship> friendships = new FriendshipProvider(CORD.getRedisAccess())
            .getPlayerFriendships(player.getUniqueId().toString());

        friendships.forEach((Friendship friendship) -> {
            if (friendship.getState() != FriendshipRequestState.ACCEPTED) {
                return;
            }

            final ProxiedPlayer target = CORD.getProxy()
                .getPlayer(UUID.fromString(friendship.getFriendUuid(player.getUniqueId().toString())));

            if (target == null) {
                return;
            }

            target.sendMessage(new TextComponent(ChatColor.AQUA + player.getName() + ChatColor.RESET + " a rejoint le serveur! ! !1"));
        });
    }

    /**
     * Called when a player joins the proxy.
     */
    @EventHandler
    public void onLogin(PostLoginEvent event)
    {
        final ProxiedPlayer player = event.getPlayer();

        this.broadcastToFriends(player);
    }

}
