package fr.hashtek.tekore.common.command.subcommand;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSubcommand
{

    private final String name;
    private final String description;
    private final String permission;


    /**
     * Creates a new subcommand, without permission (so, anyone can execute it).
     * <p>
     * Even if subcommand has no permission, player must have the subcommand's parent permission
     * (if defined) to execute the subcommand.
     * </p>
     *
     * @param   parent      Parent
     * @param   name        Name
     * @param   description Description
     * @throws  InvalidCommandContextException  You can ignore this one.
     */
    public AbstractSubcommand(AbstractCommand parent, String name, String description)
        throws InvalidCommandContextException
    {
        this(parent, name, description, null);
    }

    /**
     * Creates a new subcommand, with a defined permission.
     * <p>
     * <strong>NOTE:</strong> Subcommand's permission <u>MUST</u> start with its parent's permission.
     * </br>
     * For example, let's say the parent have the permission {@code "player.friend"}.
     * </br>
     * Therefore, the subcommand's permission <u>MUST</u> start with {@code "player.friend."}.
     * </br>
     * Otherwise, {@link InvalidCommandContextException} will be thrown.
     * </p>
     *
     * @param   parent      Parent
     * @param   name        Name
     * @param   description Description
     * @param   permission  Permission
     * @throws  InvalidCommandContextException  If subcommand's permission does not match with parent's permission.
     */
    public AbstractSubcommand(
        AbstractCommand parent,
        String name,
        String description,
        String permission
    )
        throws InvalidCommandContextException
    {
        this.name = name;
        this.description = description;

        if (
            permission != null &&
            parent.getPermission() != null &&
            !permission.startsWith(parent.getPermission() + ".")
        ) {
            throw new InvalidCommandContextException(parent, this);
        }
        this.permission = permission;
    }


    public abstract void execute(
        @NotNull Player player,
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
     * @return  Subcommand's name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return  Subcommand's description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * @return  Subcommand's permission
     */
    public String getPermission()
    {
        return this.permission;
    }

}
