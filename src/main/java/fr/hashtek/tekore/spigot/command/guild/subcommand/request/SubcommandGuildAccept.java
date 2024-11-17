package fr.hashtek.tekore.spigot.command.guild.subcommand.request;

import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.spigot.command.guild.CommandGuild;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandGuildAccept
    extends AbstractSubcommand
{

    public SubcommandGuildAccept(CommandGuild parent)
        throws InvalidCommandContextException
    {
        super(parent, "accept", "");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull String[] args
    )
    {
        // ...
    }

}
