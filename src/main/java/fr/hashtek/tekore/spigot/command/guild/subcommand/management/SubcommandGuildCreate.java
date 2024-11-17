package fr.hashtek.tekore.spigot.command.guild.subcommand.management;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.guild.Guild;
import fr.hashtek.tekore.common.guild.GuildManager;
import fr.hashtek.tekore.common.guild.io.GuildPublisher;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.command.guild.CommandGuild;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandGuildCreate
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandGuildCreate(CommandGuild parent)
        throws InvalidCommandContextException
    {
        super(parent, "create", "");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull String[] args
    )
    {
        final PlayerManager playerManager = CORE.getPlayerManagersManager()
            .getPlayerManager(player);

        final Account account = playerManager.getAccount();
        final GuildManager guildManager = account.getGuildManager();

        if (guildManager.getCurrentGuild() != null) {
            player.sendMessage(Component.text(ChatColor.RED + "You are already in a guild. If you want to create a new one, type /guild leave and go ahead."));
            return;
        }

        /* Creating a new Guild */
        final Guild createdGuild = Guild.create(player);

        /* Setting player's guild to the freshly new created guild */
        guildManager.setCurrentGuild(createdGuild);

        /* Pushing modifications to the Redis database */
        new GuildPublisher(CORE.getRedisAccess()).push(createdGuild);
        account.pushData(CORE.getRedisAccess());

        player.sendMessage(Component.text(ChatColor.GREEN + "Guild created!"));
    }

}
