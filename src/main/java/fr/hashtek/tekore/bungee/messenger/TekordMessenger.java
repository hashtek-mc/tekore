package fr.hashtek.tekore.bungee.messenger;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.regex.Regexes;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TekordMessenger
    implements HashLoggable, Listener
{

    private static final Tekord CORD = Tekord.getInstance();
    public static final String BUNGEECORD_CHANNEL = "BungeeCord";


    /**
     * Loads the messenger.
     * </br>
     * Basically registers the messaging channels.
     */
    public void load()
    {
        final String bungeecordChannel = BUNGEECORD_CHANNEL;

        CORD.getProxy().registerChannel(bungeecordChannel);

        CORD.getHashLogger().info(this, "Channel \"" + bungeecordChannel + "\" registered!");
    }

    /**
     * Unloads the messenger.
     */
    public void unload()
    {
        CORD.getProxy().unregisterChannel(BUNGEECORD_CHANNEL);
    }


    /**
     * Refreshes the account of a player from the Redis database.
     *
     * @param   in  Data input
     * @throws  IllegalArgumentException    If passed string is not a UUID
     */
    private void refreshAccount(ByteArrayDataInput in)
        throws IllegalArgumentException
    {
        final String uuid = in.readUTF();

        if (!Regexes.matches(Regexes.UUID_REGEX, uuid)) {
            throw new IllegalArgumentException("Invalid UUID: " + uuid);
        }

        CORD.getPlayerManagersManager().getPlayerManager(uuid).refreshData(CORD.getRedisAccess());
    }


    /**
     * Called when the proxy receives a message from another plugin.
     */
    @EventHandler
    public void onPluginMessageReceived(PluginMessageEvent event)
    {
        if (!event.getTag().equals(BUNGEECORD_CHANNEL)) {
            return;
        }

        final ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        final String subchannel = in.readUTF();

        switch (subchannel) {
            case "RefreshAccount":
                this.refreshAccount(in);
                break;
            // ...
            default:
                return;
        }
    }

}
