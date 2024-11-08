package fr.hashtek.tekore.spigot.command.party.subcommand.management;

import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.exception.PlayerNotInPartyException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.command.party.CommandParty;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyKick
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyKick(CommandParty parent)
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
        if (args.length != 1) {
            player.sendMessage(Component.text(ChatColor.RED + "Wrong syntax."));
            return;
        }

        final String targetName = args[0];

        if (!Regexes.matches(Regexes.USERNAME_REGEX, targetName)) {
            player.sendMessage(Component.text(ChatColor.RED + "Invalid username."));
            return;
        }

        if (targetName.equalsIgnoreCase(player.getName())) {
            player.sendMessage(Component.text(ChatColor.RED + "You can't kick yourself. If you want to leave the party, either disband it or give the ownership to another player and then leave."));
            return;
        }

        final Party playerParty = CORE.getPlayerManagersManager()
            .getPlayerManager(player)
            .getAccount()
            .getPartyManager()
            .getCurrentParty();

        if (playerParty == null) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not in a party."));
            return;
        }

        if (!playerParty.getOwnerUuid().equals(player.getUniqueId().toString())) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not the owner of this party. Type /party leave to leave."));
            return;
        }

        try {
            final String kickedMemberName = playerParty.kickMember(targetName);
            playerParty.refreshMembersRamStoredParty(player);

            player.sendMessage(Component.text(ChatColor.GREEN + "You successfully kicked " + ChatColor.AQUA + kickedMemberName + ChatColor.GREEN + " out of your party!"));
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(Component.text(ChatColor.RED + "This player doesn't exist or never connected to this server."));
        }
        catch (PlayerNotInPartyException exception) {
            player.sendMessage(Component.text(ChatColor.RED + "This player is not in your party."));
        }
    }

}
