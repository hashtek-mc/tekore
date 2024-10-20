package fr.hashtek.tekore.spigot.commands.friend.subcommands;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.exceptions.EntryNotFoundException;
import fr.hashtek.tekore.common.friendship.Friendship;
import fr.hashtek.tekore.common.friendship.FriendshipManager;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.spigot.Tekore;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SubcommandFriendManagement
{

    private static final Tekore CORE = Tekore.getInstance();


    public void add(
        Player player,
        FriendshipManager playerFriendshipManager,
        String[] args
    )
    {
        if (args.length != 2) {
            player.sendMessage(Component.text("Wrong syntax."));
            return;
        }

        final AccountProvider accountProvider = new AccountProvider(CORE.getRedisAccess());

        final String targetName = args[1];
        final Account targetAccount;
        final String targetAccountName;

        if (!Regexes.matches(Regexes.USERNAME_REGEX, targetName)) {
            player.sendMessage(Component.text("Invalid username."));
            return;
        }

        /* If player tries to add itself, cancel friendship creation. */
        if (targetName.equalsIgnoreCase(player.getName())) {
            player.sendMessage(Component.text("haha veri funi"));
            return;
        }

        try {
            targetAccount = accountProvider.get(accountProvider.getUuidFromUsername(targetName));
            targetAccountName = targetAccount.getUsername();
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(Component.text("Player with name \"" + targetName + "\" doesn't exist or never connected to this server."));
            return;
        }

        if (playerFriendshipManager.hasFriendshipWith(targetAccount.getUuid())) {
            player.sendMessage(Component.text("You already have a friendship with that player (accepted or not)."));
            return;
        }

        try {
            playerFriendshipManager.createFriendship(targetAccount.getUuid());

            player.sendMessage(Component.text("You asked " + ChatColor.AQUA + targetAccountName + ChatColor.RESET + " for a friendship!"));

            CORE.getMessenger().sendPluginMessage(
                player,
                "MessageRaw",
                targetAccountName,
                "[\"\",{\"text\":\"" + player.getName() + "\",\"color\":\"light_purple\"},{\"text\":\" vous a envoyé une demande d'ami.\\n\"},{\"text\":\"[ACCEPTER]\",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/friend accept " + player.getName() + "\"}},{\"text\":\" -\",\"color\":\"dark_gray\"},{\"text\":\" \"},{\"text\":\"[REFUSER]\",\"bold\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/friend deny Shuvly\"}},{\"text\":\" -\",\"color\":\"dark_gray\"},{\"text\":\" \"},{\"text\":\"[BLOQUER]\",\"bold\":true,\"color\":\"gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/friend block Shuvly\"}}]"
            );

            CORE.getMessenger().sendPluginMessage(
                player,
                Constants.UPDATE_FRIENDS_SUBCHANNEL,
                targetAccountName
            );
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(exception.getMessage());
        }
    }

    public void remove(
        Player player,
        FriendshipManager playerFriendshipManager,
        String[] args
    )
    {
        if (args.length != 2) {
            player.sendMessage(Component.text("Wrong syntax."));
            return;
        }

        final AccountProvider accountProvider = new AccountProvider(CORE.getRedisAccess());

        final String targetName = args[1];
        final Account targetAccount;
        final String targetAccountName;

        if (!Regexes.matches(Regexes.USERNAME_REGEX, targetName)) {
            player.sendMessage(Component.text("Invalid username."));
            return;
        }

        /* If player tries to add itself, cancel friendship creation. */
        if (targetName.equalsIgnoreCase(player.getName())) {
            player.sendMessage(Component.text("haha veri funi"));
            return;
        }

        try {
            targetAccount = accountProvider.get(accountProvider.getUuidFromUsername(targetName));
            targetAccountName = targetAccount.getUsername();
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(Component.text("Player with name \"" + targetName + "\" doesn't exist or never connected to this server."));
            return;
        }

        final Friendship friendship = playerFriendshipManager.getFriendshipByUuid(targetAccount.getUuid());

        try {
            playerFriendshipManager.destroyFriendship(friendship.getUuid());

            CORE.getMessenger().sendPluginMessage(
                player,
                Constants.UPDATE_FRIENDS_SUBCHANNEL,
                targetAccount.getUsername()
            );

            player.sendMessage(Component.text("You deleted your friendship with " + ChatColor.AQUA + targetAccountName + ChatColor.RESET + " :("));
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(Component.text("You do not have an active friendship with that player."));
        }
    }

}
