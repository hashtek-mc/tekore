package fr.hashtek.tekore.bungee.command.hub;

import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.player.PlayerManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandHub extends Command
{

    private final Tekord cord;


    /**
     * Creates a new instance of CommandHub.
     * Used to register the command.
     */
    public CommandHub(Tekord cord)
    {
        super("hub", null, "lobby", "l");

        this.cord = cord;
    }


    /**
     * Called when command is executed.
     *
     * @param	sender	Player who executed the command
     * @param	args	Arguments passed
     */
    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Vous devez être un joueur pour exécuter cette commande."));
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;
        final PlayerManager playerManager = this.cord.getPlayerManager(player);
        final ServerInfo playerServer = player.getServer().getInfo();

        if (playerServer.getName().startsWith("lobby")) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Vous êtes déjà dans un lobby !"));
            return;
        }

        playerManager.sendToServer("lobby01");
    }
}
