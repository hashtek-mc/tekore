package fr.hashtek.tekore.common.command;

import fr.hashtek.tekore.common.command.subcommand.SubcommandManager;
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
    private final SubcommandManager subcommandManager;


    /**
     * Creates a new command, without assigned permission
     * (so, anyone can execute it) and without subcommand manager.
     */
    public AbstractCommand()
    {
        this(null, false);
    }

    /**
     * Creates a new command, with assigned permission but
     * without subcommand manager.
     *
     * @param   permission  Command's permission
     */
    public AbstractCommand(String permission)
    {
        this(permission, false);
    }

    /**
     * Creates a new command, without assigned permission but
     * with subcommand manager.
     *
     * @param   useSubcommands  Set to {@code true} if you want to assign a new subcommand manager for this command
     */
    public AbstractCommand(boolean useSubcommands)
    {
        this(null, useSubcommands);
    }

    /**
     * Creates a new command, with assigned permission and
     * with subcommand manager.
     *
     * @param   permission      Command's permission
     * @param   useSubcommands  Set to {@code true} if you want to assign a new subcommand manager for this command
     */
    public AbstractCommand(
        String permission,
        boolean useSubcommands
    )
    {
        this.permission = permission;
        this.subcommandManager = useSubcommands
            ? new SubcommandManager()
            : null;
    }


    @Override
    public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    )
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Vous devez être un joueur pour exécuter cette commande.");
            return true;
        }

        if (!this.hasPermission(player)) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'exécuter cette commande.");
            return true;
        }

        if (this.subcommandManager != null && args.length > 0) {
            this.subcommandManager.executeSubcommand(args[0].toLowerCase(), player, args);
            return true;
        }

        this.execute(player, command, label, args);
        return true;
    }

    /**
     * Executes the given command, returning its success.
     * <p>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     * </p>
     * <p>
     * <strong>If command is subcommand-managed (c.f. {@link AbstractCommand#isSubcommandManaged()}),
     * this will be fired ONLY if arguments are empty.</strong>
     * </p>
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
     * @return  {@code true} if command has a subcommand manager
     */
    private boolean isSubcommandManaged()
    {
        return this.subcommandManager != null;
    }

    /**
     * @return  Command's permission
     */
    public String getPermission()
    {
        return this.permission;
    }

    /**
     * @return  Command's subcommand manager
     */
    public SubcommandManager getSubcommandManager()
    {
        return this.subcommandManager;
    }

}
