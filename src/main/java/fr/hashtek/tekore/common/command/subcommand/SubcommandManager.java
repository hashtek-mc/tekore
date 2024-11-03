package fr.hashtek.tekore.common.command.subcommand;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SubcommandManager
{

    private final Map<String, AbstractSubcommand> subcommands;


    /**
     * Creates a new subcommand manager.
     */
    public SubcommandManager()
    {
        this.subcommands = new HashMap<String, AbstractSubcommand>();
    }


    /**
     * Registers a new subcommand.
     *
     * @param   subcommand  Subcommand to add
     * @return  Itself
     */
    public SubcommandManager registerSubcommand(AbstractSubcommand subcommand)
    {
        this.subcommands.put(subcommand.getName(), subcommand);
        return this;
    }

    /**
     * Executes a subcommand by its name.
     *
     * @param   name    Name of the subcommand to execute
     * @param   player  Player who executed the command
     * @param   args    Arguments passed
     * @return  {@code true} if subcommand exists. Otherwise, {@code false}.
     */
    public boolean executeSubcommand(String name, Player player, String[] args)
    {
        final AbstractSubcommand subcommand = this.subcommands.get(name);

        if (subcommand == null) {
            player.sendMessage(Component.text(ChatColor.RED + "Cette sous-commande n'existe pas."));
            return true;
        }

        if (!subcommand.hasPermission(player)) {
            player.sendMessage(Component.text(ChatColor.RED + "Vous n'avez pas la permission d'exécuter cette commande !"));
            return true;
        }

        subcommand.execute(player, Arrays.copyOfRange(args, 1, args.length));
        /*                         ↑ Omitting the first element (the subcommand name) for simplicity. */

        return true;
    }

}
