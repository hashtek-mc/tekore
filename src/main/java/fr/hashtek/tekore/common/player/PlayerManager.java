package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.bukkit.Tekore;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Only player class that depends on Bukkit.
 */
public class PlayerManager
{

    private final Tekore core;

    private final Player player;


    /**
     * Creates a new instance of PlayerManager.
     *
     * @param   core    Tekore's instance
     * @param   player  Player
     */
    public PlayerManager(Tekore core, Player player)
    {
        this.core = core;
        this.player = player;
    }


    /**
     * Sends the player to a server given its name.
     *
     * @param   serverName  Server's name
     */
    public void sendToServer(String serverName)
    {
        try {
            final ByteArrayOutputStream b = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(b);

            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.sendPluginMessage(this.core, "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        } catch (IOException exception) {
            // Log HashError
        }
    }


    /**
     * @return  Linked player
     */
    public Player getPlayer()
    {
        return this.player;
    }

}
