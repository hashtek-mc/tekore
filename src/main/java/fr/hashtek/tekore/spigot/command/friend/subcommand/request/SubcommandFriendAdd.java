package fr.hashtek.tekore.spigot.command.friend.subcommand.request;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.friendship.FriendshipManager;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.command.friend.CommandFriend;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandFriendAdd
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandFriendAdd(CommandFriend parent)
        throws InvalidCommandContextException
    {
        super(parent, "add", "");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull String[] args
    )
    {
        if (args.length != 1) {
            player.sendMessage(Component.text("Wrong syntax."));
            return;
        }

        final FriendshipManager playerFriendshipManager = CORE.getPlayerManagersManager()
            .getPlayerManager(player)
            .getFriendshipManager();

        final AccountProvider accountProvider = new AccountProvider(CORE.getRedisAccess());

        final String targetName = args[0];
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

}
