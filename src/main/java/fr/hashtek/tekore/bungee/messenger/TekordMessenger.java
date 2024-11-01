package fr.hashtek.tekore.bungee.messenger;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.bungee.messenger.routes.TekordMessengerRouter;
import fr.hashtek.tekore.common.constant.Constants;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TekordMessenger
    implements HashLoggable, Listener
{

    private static final Tekord CORD = Tekord.getInstance();

    private final TekordMessengerRouter router;


    /**
     * Creates a new messenger for Tekord.
     */
    public TekordMessenger()
    {
        this.router = new TekordMessengerRouter(this);
    }


    /**
     * Loads the messenger.
     * </br>
     * Basically registers the messaging channels.
     */
    public void load()
    {
        CORD.getProxy().registerChannel(Constants.BUNGEECORD_CHANNEL);
        CORD.getProxy().getPluginManager().registerListener(CORD, this);

        CORD.getHashLogger().info(this, "Channel \"" + Constants.BUNGEECORD_CHANNEL + "\" registered!");
    }

    /**
     * Unloads the messenger.
     */
    public void unload()
    {
        CORD.getProxy().unregisterChannel(Constants.BUNGEECORD_CHANNEL);
    }


    /**
     * Sends a plugin message to a given server.
     *
     * @param   server      Server to send the message on
     * @param   subchannel  Sub-channel
     * @param   data        Data to send
     */
    public void sendPluginMessage(
        Server server,
        String subchannel,
        String... data
    )
    {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(subchannel);
        for (String str : data) {
            out.writeUTF(str);
        }
        server.sendData(Constants.BUNGEECORD_CHANNEL, out.toByteArray());
    }

    /**
     * Called when the proxy receives a message from another plugin.
     */
    @EventHandler
    public void onPluginMessageReceived(PluginMessageEvent event)
    {
        if (!event.getTag().equals(Constants.BUNGEECORD_CHANNEL)) {
            return;
        }

        final ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        final String subchannel = in.readUTF();

        this.router.dispatch(subchannel, in);
    }

}
