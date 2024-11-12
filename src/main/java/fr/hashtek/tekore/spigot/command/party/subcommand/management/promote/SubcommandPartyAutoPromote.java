package fr.hashtek.tekore.spigot.command.party.subcommand.management.promote;

import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.spigot.command.party.CommandParty;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyAutoPromote
    extends AbstractSubcommand
{

    public SubcommandPartyAutoPromote(CommandParty parent)
        throws InvalidCommandContextException
    {
        super(parent, "autopromote", "");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull String[] args
    )
    {
        player.performCommand("party promote --auto");
    }

}
