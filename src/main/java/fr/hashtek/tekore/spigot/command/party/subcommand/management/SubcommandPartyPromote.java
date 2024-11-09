package fr.hashtek.tekore.spigot.command.party.subcommand.management;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.constant.Constants;
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

public class SubcommandPartyPromote
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();

    /* Here we use a ThreadLocal so that the handleFlags() and execute() function can both edit it, thread-safely. */
    private final ThreadLocal<String> buffer = ThreadLocal.withInitial(String::new);


    public SubcommandPartyPromote(CommandParty parent)
        throws InvalidCommandContextException
    {
        super(parent, "promote", "");
    }


    public boolean handleFlags(String arg, Player player, Party party)
    {
        switch (arg) {
            /**
             * Flag: --auto
             *
             * Automatically give the ownership to the first member of the party.
             */
            case "--auto":
                if (party.getMembers().size() == 1) {
                    player.sendMessage(Component.text(ChatColor.RED + "You are alone in your party, nobody can be automatically promoted."));
                    return false;
                }

                final String newOwnerUuid = party.getMembersUuid().stream()
                    .filter(p -> !p.equals(player.getUniqueId().toString()))
                    .findFirst()
                    .orElse(null); // Should never happen, we check everything before.

                try {
                    final String newOwnerName = new AccountProvider(CORE.getRedisAccess())
                        .getUsernameFromUuid(newOwnerUuid);

                    buffer.set(newOwnerName);
                    return true;
                }
                catch (EntryNotFoundException exception) {
                    // TODO: Should never happen, but maybe log?
                    return true;
                }

            default:
                player.sendMessage(Component.text(ChatColor.RED + "Unknown flag. Type /party help promote for help."));
                return false;
        }
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

        final PartyManager partyManager = CORE.getPlayerManagersManager()
            .getPlayerManager(player)
            .getAccount()
            .getPartyManager();

        final Party currentParty = partyManager.getCurrentParty();

        if (currentParty == null) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not in a party."));
            return;
        }

        if (!currentParty.getOwnerUuid().equals(player.getUniqueId().toString())) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not the leader of your party."));
            return;
        }

        String targetName = args[0].toLowerCase();

        /* Flag handling */
        if (targetName.startsWith("-")) {
            if (!handleFlags(targetName, player, partyManager.getCurrentParty())) {
                return;
            }
            targetName = buffer.get().toLowerCase();
        }

        /* If player tries to promote itself, cancel. */
        if (targetName.equalsIgnoreCase(player.getName())) {
            player.sendMessage(Component.text("haha veri funi"));
            return;
        }

        final Account targetAccount;
        final String targetAccountName;

        if (!Regexes.matches(Regexes.USERNAME_REGEX, targetName)) {
            player.sendMessage(Component.text("Invalid username."));
            return;
        }

        try {
            final AccountProvider accountProvider = new AccountProvider(CORE.getRedisAccess());

            targetAccount = accountProvider.get(accountProvider.getUuidFromUsername(targetName));
            targetAccountName = targetAccount.getUsername();
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(Component.text(ChatColor.RED + "This player doesn't exist or never connected to this server."));
            return;
        }

        if (!currentParty.getMembersUuid().contains(targetAccount.getUuid())) {
            player.sendMessage(Component.text(ChatColor.RED + "Targeted player is not in your party."));
            return;
        }

        currentParty
            .setOwner(targetAccount.getUuid())
            .pushData()
            .refreshMembersRamStoredParty(player);

        currentParty.broadcastToMembers(
            player,
            targetAccountName + " is the new party owner."
        );

        CORE.getMessenger().sendPluginMessage(
            player,
            "Message",
            targetAccountName,
            "You are the new party owner."
        );

        CORE.getMessenger().sendPluginMessage(
            player,
            Constants.UPDATE_ACCOUNT_SUBCHANNEL,
            targetAccountName
        );

        player.sendMessage(Component.text(ChatColor.GREEN + "You made " + targetAccountName + " the new party owner."));
    }

}
