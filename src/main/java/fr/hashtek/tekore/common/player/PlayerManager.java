package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.bukkit.Tekore;
import fr.hashtek.tekore.bungee.Tekord;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;

public class PlayerManager
{

    private final Object player;

    private final PlayerData data;
    private final PlayerSettingsManager settingsManager;


    public PlayerManager(Object player) throws NoClassDefFoundError
    {
        this.player = player;

        this.data = new PlayerData(this.player);
        this.settingsManager = new PlayerSettingsManager();
    }

    public PlayerManager(String playerName) throws NoClassDefFoundError
    {
        this.player = null;
        this.data = new PlayerData(playerName);
        this.settingsManager = null;
    }


    /**
     * Sends the player to a server given its name.
     *
     * @param   serverName  Server's name
     */
    public void sendToServer(String serverName)
        throws InvalidClassException
    {
        assert player != null;

        if (this.isBukkit()) {
            final Tekore core = Tekore.getInstance();

            try {
                final ByteArrayOutputStream b = new ByteArrayOutputStream();
                final DataOutputStream out = new DataOutputStream(b);

                out.writeUTF("Connect");
                out.writeUTF(serverName);

                ((org.bukkit.entity.Player) player)
                    .sendPluginMessage(core, "BungeeCord", b.toByteArray());

                b.close();
                out.close();
            } catch (IOException exception) {
                // Log HashError
            }
            return;
        }

        if (this.isBungee()) {
            final Tekord cord = Tekord.getInstance();
            final net.md_5.bungee.api.config.ServerInfo server =
                cord.getProxy().getServerInfo(serverName);

            this.getBungeePlayer().connect(server);
            return;
        }

        throw new InvalidClassException("Player type is unknown.");
    }


    /**
     * @return  Linked player as Bukkit.
     */
    public org.bukkit.entity.Player getBukkitPlayer()
        throws InvalidClassException
    {
        if (!this.isBukkit())
            throw new InvalidClassException("Linked player is not Bukkit.");
        return (org.bukkit.entity.Player) this.player;
    }

    /**
     * @return  Linked player as Bungee.
     */
    public net.md_5.bungee.api.connection.ProxiedPlayer getBungeePlayer()
        throws InvalidClassException
    {
        if (this.isBungee())
            throw new InvalidClassException("Linked player is not Bungee.");
        return (net.md_5.bungee.api.connection.ProxiedPlayer) this.player;
    }

    /**
     * @return  True if linked player is Bukkit.
     */
    public boolean isBukkit()
    {
        return this.player != null &&
            this.player instanceof org.bukkit.entity.Player;
    }

    /**
     * @return  True if linked player is Bungee.
     */
    public boolean isBungee()
    {
        return this.player != null &&
            this.player instanceof net.md_5.bungee.api.connection.ProxiedPlayer;
    }


    public PlayerData getData()
    {
        return this.data;
    }

    public PlayerSettingsManager getSettingsManager()
    {
        return this.settingsManager;
    }

}
