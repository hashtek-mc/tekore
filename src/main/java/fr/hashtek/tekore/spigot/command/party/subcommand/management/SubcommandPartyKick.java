package fr.hashtek.tekore.spigot.command.party.subcommand.management;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyKick
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyKick(AbstractCommand parent)
        throws InvalidCommandContextException
    {
        super(parent, "rm:remove:kick", "");
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
