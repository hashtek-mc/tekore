package fr.hashtek.tekore.spigot.command.party;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.spigot.command.party.subcommand.info.SubcommandPartyStatus;
import fr.hashtek.tekore.spigot.command.party.subcommand.management.*;
import fr.hashtek.tekore.spigot.command.party.subcommand.management.promote.SubcommandPartyPromote;
import fr.hashtek.tekore.spigot.command.party.subcommand.request.SubcommandPartyAccept;
import fr.hashtek.tekore.spigot.command.party.subcommand.request.SubcommandPartyDeny;
import fr.hashtek.tekore.spigot.command.party.subcommand.request.SubcommandPartyInvite;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandParty
    extends AbstractCommand
{

    public CommandParty()
    {
        super(true);

        try {
            super.getSubcommandManager()
                // info
                .registerSubcommand(new SubcommandPartyStatus(this))

                // management
                .registerSubcommand(new SubcommandPartyCreate(this))
                .registerSubcommand(new SubcommandPartyDisband(this))
                .registerSubcommand(new SubcommandPartyKick(this))
                .registerSubcommand(new SubcommandPartyLeave(this))
                .registerSubcommand(new SubcommandPartyPromote(this))

                // request
                .registerSubcommand(new SubcommandPartyInvite(this))
                .registerSubcommand(new SubcommandPartyAccept(this))
                .registerSubcommand(new SubcommandPartyDeny(this));
        }
        catch (InvalidCommandContextException exception) {
            // ...
        }
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    )
    {
        player.performCommand("party status");
    }

}
