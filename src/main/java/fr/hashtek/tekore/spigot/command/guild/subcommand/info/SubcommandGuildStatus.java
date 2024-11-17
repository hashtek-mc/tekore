package fr.hashtek.tekore.spigot.command.guild.subcommand.info;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.guild.Guild;
import fr.hashtek.tekore.common.guild.GuildProfile;
import fr.hashtek.tekore.common.guild.rank.GuildRank;
import fr.hashtek.tekore.spigot.Tekore;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandGuildStatus
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandGuildStatus(AbstractCommand parent)
        throws InvalidCommandContextException
    {
        super(parent, "status", "");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull String[] args
    )
    {
        final Guild currentGuild = CORE.getPlayerManagersManager()
            .getPlayerManager(player)
            .getAccount()
            .getGuildManager()
            .getCurrentGuild();

        if (currentGuild == null) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not in a guild. Create one by executing /guild create."));
            return;
        }

        player.sendMessage(Component.text("Guild \"" + currentGuild.getUuid() + "\""));
        player.sendMessage(Component.text("Name: " + currentGuild.getDisplayName()));
        player.sendMessage(Component.text("Description: " + currentGuild.getDescription()));
        player.sendMessage(Component.text("Created at: " + currentGuild.getCreatedAt()));
        player.sendMessage(Component.text("Members:"));
        for (String key : currentGuild.getMembersProfile().keySet()) {
            final GuildProfile profile = currentGuild.getMemberProfile(key);
            player.sendMessage(Component.text("- " + key));
            player.sendMessage(Component.text("  Profile:"));
            player.sendMessage(Component.text("  - Rank: " + profile.getRank().getName()));
            player.sendMessage(Component.text("  - Joined at: " + profile.getJoinedAt()));
        }
        player.sendMessage(Component.text("Ranks:"));
        for (GuildRank rank : currentGuild.getRanks()) {
            player.sendMessage(Component.text("- " + rank.getName() + "(\"" + rank.getUuid() + ")"));
            if (rank.getPermissions() == null || rank.getPermissions().isEmpty()) {
                player.sendMessage(Component.text("   No permissions."));
            } else {
                player.sendMessage(Component.text("   Permissions:"));
                for (String permission : rank.getPermissions()) {
                    player.sendMessage(Component.text("  - " + permission));
                }
            }
        }
        player.sendMessage(Component.text("Guild owner: " + currentGuild.getOwnerUuid()));
    }

}
