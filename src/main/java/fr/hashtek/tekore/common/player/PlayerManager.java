package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.player.friend.PlayerFriendManager;
import fr.hashtek.tekore.common.player.settings.PlayerSettingsManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerManager
{

    private final Object player;

    private final PlayerData data;
    private final PlayerSettingsManager settingsManager;
    private final PlayerFriendManager friendManager;


    public PlayerManager(Object player) throws NoClassDefFoundError
    {
        this.player = player;

        this.data = new PlayerData(this.player);
        this.settingsManager = new PlayerSettingsManager();
        this.friendManager = new PlayerFriendManager();
    }

    public PlayerManager(String playerName) throws NoClassDefFoundError
    {
        this.player = null;

        this.data = new PlayerData(playerName);
        this.settingsManager = null;
        this.friendManager = null;
    }


    public void sendToServer(String serverName) throws NoClassDefFoundError
    {
        try {
            this.sendToServerBungee(serverName);
        } catch (NoClassDefFoundError unused) {
            try {
                this.sendToServerBukkit(serverName);
            } catch (NoClassDefFoundError exception) {
                throw new NoClassDefFoundError("Player type is unknown, can't send to server.");
            }
        }
    }

    private void sendToServerBukkit(String serverName)
        throws NoClassDefFoundError
    {
        if (!(this.player instanceof org.bukkit.entity.Player))
            throw new NoClassDefFoundError("Not Bukkit.");

        try {
            final ByteArrayOutputStream b = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(b);

            out.writeUTF("Connect");
            out.writeUTF(serverName);

            final Class<?> bukkitPlayerClass = this.player.getClass();
            final Method sendPluginMessageMethod = bukkitPlayerClass.getMethod("sendPluginMessage",
                Class.forName("org.bukkit.plugin.Plugin"), String.class, byte[].class);

            final Class<?> tekoreClass = Class.forName("fr.hashtek.tekore.bukkit.Tekore");
            final Method getInstanceMethod = tekoreClass.getMethod("getInstance");
            final Object tekoreInstance = getInstanceMethod.invoke(null);

            sendPluginMessageMethod.invoke(this.player, tekoreInstance, "BungeeCord", b.toByteArray());

            b.close();
            out.close();
        } catch (IOException exception) {
            // TODO: Log HashError
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            // TODO: Log HashError !!
        }
    }

    private void sendToServerBungee(String serverName)
        throws NoClassDefFoundError
    {
        if (!(this.player instanceof net.md_5.bungee.api.connection.ProxiedPlayer))
            throw new NoClassDefFoundError("Not Bungee.");

        ((net.md_5.bungee.api.connection.ProxiedPlayer) this.player).connect(
            (fr.hashtek.tekore.bungee.Tekord.getInstance()).getProxy().getServerInfo(serverName));
    }


    /**
     * @return  Player's data
     */
    public PlayerData getData()
    {
        return this.data;
    }

    /**
     * @return  Settings manager
     */
    public PlayerSettingsManager getSettingsManager()
    {
        return this.settingsManager;
    }

    /**
     * @return	Friend manager
     */
    public PlayerFriendManager getFriendManager()
    {
        return this.friendManager;
    }

}
