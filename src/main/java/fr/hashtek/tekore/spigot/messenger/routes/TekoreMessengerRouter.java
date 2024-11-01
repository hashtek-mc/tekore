package fr.hashtek.tekore.spigot.messenger.routes;

import com.google.common.io.ByteArrayDataInput;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.common.router.Router;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.messenger.TekoreMessenger;
import org.bukkit.entity.Player;

public class TekoreMessengerRouter
    extends Router
{

    private static final Tekore CORE = Tekore.getInstance();

    private final TekoreMessenger messenger;


    /**
     * Create Tekore messenger router.
     *
     * @param   messenger   Tekore's router
     */
    public TekoreMessengerRouter(TekoreMessenger messenger)
    {
        this.messenger = messenger;
    }


    protected void updateFriends(ByteArrayDataInput in)
    {
        final String playerName = in.readUTF();
        final Player player = CORE.getServer().getPlayer(playerName);

        if (player == null) {
            return;
        }

        final PlayerManager playerManager =
            CORE.getPlayerManagersManager().getPlayerManager(player);

        playerManager.getFriendshipManager().fetchFriendships();
    }

    public void dispatch(
        String subchannel,
        ByteArrayDataInput in
    )
    {
        switch (subchannel) {
            case Constants.UPDATE_FRIENDS_SUBCHANNEL:
                this.updateFriends(in);
                break;
            default:
                // TODO: Send an error message.
                return;
        }
    }

}
