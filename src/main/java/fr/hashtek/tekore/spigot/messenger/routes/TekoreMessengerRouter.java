package fr.hashtek.tekore.spigot.messenger.routes;

import com.google.common.io.ByteArrayDataInput;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.common.router.Router;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.messenger.TekoreMessenger;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TekoreMessengerRouter
    extends Router
    implements HashLoggable
{

    private static final Tekore CORE = Tekore.getInstance();

    private final TekoreMessenger messenger;


    /**
     * Create Tekore messenger router.
     *
     * @param   messenger   Tekore's messenger
     */
    public TekoreMessengerRouter(TekoreMessenger messenger)
    {
        this.messenger = messenger;
    }


    @Override
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

    @Override
    protected void updateParty(ByteArrayDataInput in)
    {
        final String playerTag = in.readUTF();
        final Player player = Regexes.matches(Regexes.UUID_REGEX, playerTag)
            ? CORE.getServer().getPlayer(UUID.fromString(playerTag))
            : CORE.getServer().getPlayer(playerTag);

        if (player == null) {
            return;
        }

        CORE.getPlayerManagersManager()
            .getPlayerManager(player)
            .getAccount()
            .getPartyManager()
            .updateParty(player.getUniqueId().toString());
    }

    @Override
    protected void updateAccount(ByteArrayDataInput in)
    {
        final String playerName = in.readUTF();
        final Player player = CORE.getServer().getPlayer(playerName);

        if (player == null) {
            return;
        }

        CORE.getPlayerManagersManager()
            .getPlayerManager(player)
            .getAccount()
            .pushData(CORE.getRedisAccess());
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
                CORE.getHashLogger().error(
                    this,
                    "Unknown route \"" + subchannel + "\".\n" +
                    "Data passed: " + in);
                break;
        }
    }

}
