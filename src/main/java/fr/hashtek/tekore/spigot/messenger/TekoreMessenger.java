package fr.hashtek.tekore.spigot.messenger;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class TekoreMessenger
    implements HashLoggable, PluginMessageListener
{

    private static final Tekore CORE = Tekore.getInstance();
    public static final String BUNGEECORD_CHANNEL = "BungeeCord";


    public void load()
    {
        final String bungeecordChannel = BUNGEECORD_CHANNEL;
        final Messenger messenger = CORE.getServer().getMessenger();

        messenger.registerOutgoingPluginChannel(CORE, bungeecordChannel);
        messenger.registerIncomingPluginChannel(CORE, bungeecordChannel, this);
        CORE.getHashLogger().info(this, "Channel \"" + bungeecordChannel + "\" registered!");
    }

    public void unload()
    {
        final Messenger messenger = CORE.getServer().getMessenger();

        messenger.unregisterOutgoingPluginChannel(CORE);
        messenger.unregisterIncomingPluginChannel(CORE);
    }

    public void sendPluginMessage(
        String subchannel,
        String... message
    )
    {
        this.sendPluginMessage(
            Iterables.getFirst(CORE.getServer().getOnlinePlayers(), null),
            subchannel,
            message
        );
    }

    public void sendPluginMessage(
        Player player,
        String subchannel,
        String... message
    )
    {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(subchannel);
        for (String str : message) {
            out.writeUTF(str);
        }
        player.sendPluginMessage(CORE, BUNGEECORD_CHANNEL, out.toByteArray());
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
        if (!channel.equals(BUNGEECORD_CHANNEL)) {
            return;
        }

        // For now, nothing is done here.
    }

}
