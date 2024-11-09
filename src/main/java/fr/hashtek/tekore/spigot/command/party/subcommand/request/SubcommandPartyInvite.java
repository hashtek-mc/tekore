package fr.hashtek.tekore.spigot.command.party.subcommand.request;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.command.party.CommandParty;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyInvite
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyInvite(CommandParty parent)
        throws InvalidCommandContextException
    {
        super(parent, "add:invite", "");
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
            player.sendMessage(Component.text(ChatColor.RED + "You are not the owner of this party. Ask him to invite your dude ;)"));
            return;
        }

        final String targetName = args[0].toLowerCase();

        /* If player tries to invite itself, cancel. */
        if (targetName.equalsIgnoreCase(player.getName())) {
            player.sendMessage(Component.text(ChatColor.RED + "You can't invite yourself."));
            return;
        }

        if (!Regexes.matches(Regexes.USERNAME_REGEX, targetName)) {
            player.sendMessage(Component.text(ChatColor.RED + "Invalid username."));
            return;
        }

        final Account targetAccount;
        final String targetAccountUuid;

        try {
            final AccountProvider accountProvider = new AccountProvider(CORE.getRedisAccess());

            targetAccount = accountProvider.get(accountProvider.getUuidFromUsername(targetName));
            targetAccountUuid = targetAccount.getUuid().toString();
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(Component.text(ChatColor.RED + "This player doesn't exist or never connected to this server."));
            return;
        }

        if (playerParty.getMembersUuid().contains(targetAccountUuid)) {
            player.sendMessage(Component.text(ChatColor.RED + "This player is already in your party."));
            return;
        }

        if (playerParty.getActiveRequests().contains(targetAccountUuid)) {
            player.sendMessage(Component.text(ChatColor.RED + "You already invited this player to join your party."));
            return;
        }

        playerParty.getActiveRequests().add(targetAccountUuid);
        playerParty.pushData();

        CORE.getMessenger().sendPluginMessage(
            player,
            "MessageRaw",
            targetAccount.getUsername(),
            "[\"\",{\"text\":\"" + player.getName() + "\",\"color\":\"light_purple\"},{\"text\":\" vous a invité à rejoindre sa partie.\\n\"},{\"text\":\"[ACCEPTER]\",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party accept " + player.getName() + "\"}},{\"text\":\" -\",\"color\":\"dark_gray\"},{\"text\":\" \"},{\"text\":\"[REFUSER]\",\"bold\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party deny " + player.getName() + "\"}}]"
        );

        player.sendMessage(Component.text(ChatColor.GREEN + "You invited " + ChatColor.AQUA + targetAccount.getUsername() + ChatColor.GREEN + " to join your party."));
    }

}
