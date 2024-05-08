package fr.hashtek.tekore.bungee.command.hub;

import fr.hashtek.tekore.bungee.Tekord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandHub extends Command
{

    private final Tekord core;

    /**
     * Creates a new instance of CommandHub.
     * Used to register the command.
     */
    public CommandHub(Tekord core)
    {
        super("hub", null, "lobby", "l");

        this.core = core;
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
        final ServerInfo playerServer = player.getServer().getInfo();

        if (playerServer.getName().startsWith("lobby")) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Vous êtes déjà dans un lobby !"));
            return;
        }

        final ServerInfo target = this.core.getProxy().getServerInfo("lobby01");
        player.connect(target);
    }
}
