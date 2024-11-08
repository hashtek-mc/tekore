package fr.hashtek.tekore.spigot.command.friend.subcommand.request;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.friendship.Friendship;
import fr.hashtek.tekore.common.friendship.FriendshipManager;
import fr.hashtek.tekore.common.friendship.FriendshipRequestState;
import fr.hashtek.tekore.common.friendship.io.FriendshipPublisher;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.command.friend.CommandFriend;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

public class SubcommandFriendAccept
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandFriendAccept(CommandFriend parent)
        throws InvalidCommandContextException
    {
        super(parent, "accept", "");
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

        if (!Regexes.matches(Regexes.USERNAME_REGEX, targetName)) {
            player.sendMessage(Component.text("Invalid username."));
            return;
        }

        /* If player tries to accept itself, cancel friendship creation. */
        if (targetName.equalsIgnoreCase(player.getName())) {
            player.sendMessage(Component.text("haha veri funi"));
            return;
        }

        try {
            targetAccount = accountProvider.get(accountProvider.getUuidFromUsername(targetName));
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(Component.text("Player with name \"" + targetName + "\" doesn't exist or never connected to this server."));
            return;
        }

        if (!playerFriendshipManager.hasFriendshipWith(targetAccount.getUuid())) {
            player.sendMessage(Component.text("You did not send a friend request to this player."));
            return;
        }

        final Friendship friendship = playerFriendshipManager.getFriendshipBySender(targetAccount.getUuid());

        if (friendship == null) {
            player.sendMessage(Component.text("This player did not send a friend request to you."));
            return;
        }

        if (friendship.getState() != FriendshipRequestState.PENDING) {
            player.sendMessage(Component.text("You are already friend with that player."));
            return;
        }

        /* Update friendship properties */
        friendship.setState(FriendshipRequestState.ACCEPTED);
        friendship.setAcceptedAt(new Timestamp(System.currentTimeMillis()));

        /* And push it. */
        new FriendshipPublisher(CORE.getRedisAccess()).push(friendship);

        player.sendMessage(Component.text("You accepted " + ChatColor.AQUA + targetAccount.getUsername() + ChatColor.RESET + "'s friend request!"));

        CORE.getMessenger().sendPluginMessage(
            player,
            "Message",
            targetAccount.getUsername(), player.getName() + " accepted your friend request!"
        );

        CORE.getMessenger().sendPluginMessage(
            player,
            Constants.UPDATE_FRIENDS_SUBCHANNEL,
            targetAccount.getUsername()
        );
    }

}
