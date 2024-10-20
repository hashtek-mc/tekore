package fr.hashtek.tekore.bungee.messenger.routes;

import com.google.common.io.ByteArrayDataInput;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.bungee.messenger.TekordMessenger;
import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.router.Router;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TekordMessengerRouter
    extends Router
{

    private static final Tekord CORD = Tekord.getInstance();

    private final TekordMessenger messenger;


    /**
     * Create Tekord messenger router.
     *
     * @param   messenger   Tekord's messenger
     */
    public TekordMessengerRouter(TekordMessenger messenger)
    {
        this.messenger = messenger;
    }


    protected void updateFriends(ByteArrayDataInput in)
    {
        final String playerName = in.readUTF();
        final ProxiedPlayer player = CORD.getProxy().getPlayer(playerName);

        if (player == null) {
            return;
        }

        messenger.sendPluginMessage(player.getServer(), Constants.UPDATE_FRIENDS_SUBCHANNEL, playerName);
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
