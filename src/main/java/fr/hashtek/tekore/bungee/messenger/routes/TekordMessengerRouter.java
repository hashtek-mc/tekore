package fr.hashtek.tekore.bungee.messenger.routes;

import com.google.common.io.ByteArrayDataInput;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.bungee.messenger.TekordMessenger;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.common.router.Router;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

public class TekordMessengerRouter
    extends Router
    implements HashLoggable
{

    private static final Tekord CORD = Tekord.getInstance();

    private static final String[] DEFAULT_CHANNELS = new String[] {
        "Connect",
        "ConnectOther",
        "IP",
        "IPOther",
        "PlayerCount",
        "PlayerList",
        "GetPlayerServer",
        "GetServers",
        "Message",
        "MessageRaw",
        "GetServer",
        "Forward",
        "ForwardToPlayer",
        "UUID",
        "UUIDOther",
        "ServerIP",
        "KickPlayer",
        "KickPlayerRaw"
    };

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


    @Override
    protected void updateFriends(ByteArrayDataInput in)
    {
        final String playerName = in.readUTF();
        final ProxiedPlayer player = CORD.getProxy().getPlayer(playerName);

        if (player == null) {
            return;
        }

        messenger.sendPluginMessage(player.getServer(), Constants.UPDATE_FRIENDS_SUBCHANNEL, playerName);
    }

    @Override
    protected void updateParty(ByteArrayDataInput in)
    {
        final String playerTag = in.readUTF();
        final ProxiedPlayer player = Regexes.matches(Regexes.UUID_REGEX, playerTag)
            ? CORD.getProxy().getPlayer(UUID.fromString(playerTag))
            : CORD.getProxy().getPlayer(playerTag);

        if (player == null) {
            return;
        }

        messenger.sendPluginMessage(player.getServer(), Constants.UPDATE_PARTY_SUBCHANNEL, playerTag);
    }

    @Override
    protected void updateAccount(ByteArrayDataInput in)
    {
        final String playerName = in.readUTF();
        final ProxiedPlayer player = CORD.getProxy().getPlayer(playerName);

        if (player == null) {
            return;
        }

        messenger.sendPluginMessage(player.getServer(), Constants.UPDATE_ACCOUNT_SUBCHANNEL, playerName);
    }


    @Override
    public void dispatch(
        String subchannel,
        ByteArrayDataInput in
    )
    {
        switch (subchannel) {
            case Constants.UPDATE_FRIENDS_SUBCHANNEL:
                this.updateFriends(in);
                break;
            case Constants.UPDATE_PARTY_SUBCHANNEL:
                this.updateParty(in);
                break;
            case Constants.UPDATE_ACCOUNT_SUBCHANNEL:
                this.updateAccount(in);
                break;
            default:
                if (!List.of(DEFAULT_CHANNELS).contains(subchannel)) {
                    CORD.getHashLogger().error(
                        this,
                        "Unknown route \"" + subchannel + "\".\n" +
                        "Data passed: " + in
                    );
                }
                break;
        }
    }

}
