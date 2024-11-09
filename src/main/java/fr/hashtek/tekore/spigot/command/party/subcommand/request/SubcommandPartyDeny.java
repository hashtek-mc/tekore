package fr.hashtek.tekore.spigot.command.party.subcommand.request;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.common.party.PartyManager;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.command.party.CommandParty;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyDeny
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyDeny(CommandParty parent)
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
        if (args.length != 1) {
            player.sendMessage(Component.text(ChatColor.RED + "Wrong syntax."));
            return;
        }

        final String playerUuid = player.getUniqueId().toString();

        final PartyManager partyManager = CORE.getPlayerManagersManager()
            .getPlayerManager(player)
            .getAccount()
            .getPartyManager();

        if (partyManager.getCurrentParty() != null) {
            player.sendMessage(Component.text(ChatColor.RED + "You are already in a party. Leave it to join this one."));
            return;
        }

        final String targetName = args[0].toLowerCase();

        /* If player tries to "accept" itself, cancel. */
        if (targetName.equalsIgnoreCase(player.getName())) {
            player.sendMessage(Component.text(ChatColor.RED + "You can't accept an invite from yourself. This is impossible."));
            return;
        }

        if (!Regexes.matches(Regexes.USERNAME_REGEX, targetName)) {
            player.sendMessage(Component.text(ChatColor.RED + "Invalid username."));
            return;
        }

        final Account targetAccount;

        try {
            final AccountProvider accountProvider = new AccountProvider(CORE.getRedisAccess());

            targetAccount = accountProvider.get(accountProvider.getUuidFromUsername(targetName));
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(Component.text(ChatColor.RED + "This player doesn't exist or never connected to this server."));
            return;
        }

        final Party targetParty = targetAccount.getPartyManager()
            .updateParty(targetAccount.getUuid())
            .getCurrentParty();

        if (targetParty == null || !targetParty.getActiveRequests().contains(playerUuid)) {
            player.sendMessage(Component.text(ChatColor.RED + "This player didn't invite you."));
            return;
        }

        /* Removing player's uuid from target party's active requests... */
        targetParty.getActiveRequests().remove(playerUuid);
        /* ...then pushing party data to the Redis database... */
        targetParty.pushData();
        /* ...and finally refresh member's RAM-stored party data from the Redis database. */
        targetParty.refreshMembersRamStoredParty(player); // TODO(perf): Maybe refresh the RAM-stored party data of the owner only?

        CORE.getMessenger().sendPluginMessage(
            player,
            "Message",
            targetAccount.getUsername(),
            ChatColor.RED + player.getName() + " declined your party invite :("
        );

        player.sendMessage(Component.text(ChatColor.GREEN + "You declined " + targetAccount.getUsername() + "'s party invite."));
    }

}
