package fr.hashtek.tekore.common.commands;

import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand
    implements CommandExecutor
{

    private final String permission;


    /**
     * Creates a new command, without permission (anyone can execute it).
     */
    public AbstractCommand()
    {
        this(null);
    }

    /**
     * Creates a new command.
     *
     * @param   permission  Command's permission
     */
    public AbstractCommand(String permission)
    {
        this.permission = permission;
    }


    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param   sender      Source of the command
     * @param   command     Command which was executed
     * @param   label       Alias of the command which was used
     * @param   args        Passed command arguments
     * @return  True if command is valid. Otherwise, false.
     */
    @Override
    public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Vous devez être un joueur pour exécuter cette commande.");
            return true;
        }

        if (!this.hasPermission(player)) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'exécuter cette commande.");
            return true;
        }

        this.execute(player, command, label, args);
        return true;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param   player      Player who executed the command
     * @param   command     Command which was executed
     * @param   label       Alias of the command which was used
     * @param   args        Passed command arguments
     */
    public abstract void execute(
        @NotNull Player player,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    );

    /**
     * @param   player  Player to check on
     * @return  True if player has the permission to execute this command. Otherwise, false.
     */
    protected boolean hasPermission(Player player)
    {
        if (this.permission == null) {
            return true;
        }

        return Tekore.getInstance().getPlayerManagersManager()
            .getPlayerManager(player)
            .getAccount()
            .getRank()
            .hasPermission(this.permission);
    }


    /**
     * @return  Command's permission
     */
    public String getPermission()
    {
        return this.permission;
    }

}
