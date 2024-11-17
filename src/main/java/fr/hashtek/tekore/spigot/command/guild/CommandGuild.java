package fr.hashtek.tekore.spigot.command.guild;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.spigot.command.guild.subcommand.info.SubcommandGuildStatus;
import fr.hashtek.tekore.spigot.command.guild.subcommand.management.*;
import fr.hashtek.tekore.spigot.command.guild.subcommand.request.SubcommandGuildAccept;
import fr.hashtek.tekore.spigot.command.guild.subcommand.request.SubcommandGuildInvite;
import fr.hashtek.tekore.spigot.command.guild.subcommand.request.SubcommandGuildDeny;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandGuild
    extends AbstractCommand
{

    public CommandGuild()
    {
        super(true);

        try {
            super.getSubcommandManager()
                // info
                .registerSubcommand(new SubcommandGuildStatus(this))

                // management
                .registerSubcommand(new SubcommandGuildCreate(this))
                .registerSubcommand(new SubcommandGuildDisband(this))
                .registerSubcommand(new SubcommandGuildKick(this))
                .registerSubcommand(new SubcommandGuildLeave(this))
                .registerSubcommand(new SubcommandGuildForceOwn(this))

                // request
                .registerSubcommand(new SubcommandGuildInvite(this))
                .registerSubcommand(new SubcommandGuildAccept(this))
                .registerSubcommand(new SubcommandGuildDeny(this));
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
        player.performCommand("guild status");
    }

}
