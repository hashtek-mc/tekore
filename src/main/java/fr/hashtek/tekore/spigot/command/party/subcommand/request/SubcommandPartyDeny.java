package fr.hashtek.tekore.spigot.command.party.subcommand.request;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyDeny
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyDeny(AbstractCommand parent)
        throws InvalidCommandContextException
    {
        super(parent, "deny", "");
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
