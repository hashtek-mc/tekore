package fr.hashtek.tekore.spigot.command.friend.subcommand.request;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.friendship.Friendship;
import fr.hashtek.tekore.common.friendship.FriendshipManager;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.spigot.Tekore;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandFriendRemove
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandFriendRemove(AbstractCommand parent)
        throws InvalidCommandContextException
    {
        super(parent, "rm:remove", "");
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

        final Friendship friendship = playerFriendshipManager.getFriendshipByUuid(targetAccount.getUuid());

        if (friendship == null) {
            player.sendMessage(Component.text("You are not friend with that player."));
            return;
        }

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
