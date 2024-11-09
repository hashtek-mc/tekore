package fr.hashtek.tekore.spigot.messenger;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.messenger.routes.TekoreMessengerRouter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class TekoreMessenger
    implements HashLoggable, PluginMessageListener
{

    private static final Tekore CORE = Tekore.getInstance();

    private final TekoreMessengerRouter router;


    /**
     * Creates a new messenger for Tekore.
     */
    public TekoreMessenger()
    {
        this.router = new TekoreMessengerRouter(this);
    }


    /**
     * Loads the messenger.
     * </br>
     * Basically registers the messaging channels.
     */
    public void load()
    {
        final Messenger messenger = CORE.getServer().getMessenger();

        messenger.registerOutgoingPluginChannel(CORE, Constants.BUNGEECORD_CHANNEL);
        messenger.registerIncomingPluginChannel(CORE, Constants.BUNGEECORD_CHANNEL, this);
        CORE.getHashLogger().info(this, "Channel \"" + Constants.BUNGEECORD_CHANNEL + "\" registered!");
    }

    /**
     * Unloads the messenger.
     */
    public void unload()
    {
        final Messenger messenger = CORE.getServer().getMessenger();

        CORE.getHashLogger().info(this, "Unloading messenger...");

        messenger.unregisterOutgoingPluginChannel(CORE);
        messenger.unregisterIncomingPluginChannel(CORE);

        CORE.getHashLogger().info(this, "Messenger unloaded.");
    }

    /**
     * Sends a plugin message to Bungeecord, with
     * no regards on the player responsible for the
     * communication.
     *
     * @param   subchannel  Sub-channel
     * @param   data        Data to send
     * @apiNote For easier debugging (and even for better code cohesion),
     *          prefer using {@link TekoreMessenger#sendPluginMessage(Player, String, String...)}.
     */
    public void sendPluginMessage(
        String subchannel,
        String... data
    )
    {
        this.sendPluginMessage(
            Iterables.getFirst(CORE.getServer().getOnlinePlayers(), null),
            subchannel,
            data
        );
    }

    /**
     * Sends a plugin message to Bungeecord.
     *
     * @param   player      Player responsible for the communication
     * @param   subchannel  Sub-channel
     * @param   data        Data to send
     * @apiNote Keep in mind that if an error occurs when sending out the data,
     *          the player responsible for the communication will be kicked
     *          out of the proxy (at least for default BungeeCord subchannels).
     */
    public void sendPluginMessage(
        Player player,
        String subchannel,
        String... data
    )
    {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(subchannel);
        for (String str : data) {
            out.writeUTF(str);
        }
        player.sendPluginMessage(CORE, Constants.BUNGEECORD_CHANNEL, out.toByteArray());
    }

    /**
     * Called when a PluginMessageSource sends a plugin
     * message on a registered channel.
     *
     * @param   channel     Channel that the message was sent through
     * @param   player      Source of the message
     * @param   message     The raw message that was sent
     */
    @Override
    public void onPluginMessageReceived(
        @NotNull String channel,
        @NotNull Player player,
        @NotNull byte[] message
    )
    {
        if (!channel.equals(Constants.BUNGEECORD_CHANNEL)) {
            return;
        }

        final ByteArrayDataInput in = ByteStreams.newDataInput(message);
        final String subchannel = in.readUTF();

        this.router.dispatch(subchannel, in);
    }

}
