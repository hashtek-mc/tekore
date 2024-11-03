package fr.hashtek.tekore.spigot.command.friend.subcommand.info;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.friendship.Friendship;
import fr.hashtek.tekore.common.friendship.FriendshipManager;
import fr.hashtek.tekore.spigot.Tekore;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandFriendList
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandFriendList(AbstractCommand parent)
        throws InvalidCommandContextException
    {
        super(parent, "list", "");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull String[] args
    )
    {
        final FriendshipManager playerFriendshipManager = CORE.getPlayerManagersManager()
            .getPlayerManager(player)
            .getFriendshipManager();

        if (playerFriendshipManager.getFriendships().isEmpty()) {
            player.sendMessage(Component.text("You don't have any friendships :("));
            return;
        }

        player.sendMessage(Component.text("Here are all your friendships:\n"));

        for (Friendship friendship : playerFriendshipManager.getFriendships()) {
            player.sendMessage(Component.text(ChatColor.UNDERLINE + "Friendship " + friendship.getUuid()));
            player.sendMessage(Component.text("Sender UUID: " + friendship.getSenderUuid()));
            player.sendMessage(Component.text("Receiver UUID: " + friendship.getReceiverUuid()));
            player.sendMessage(Component.text("State: " + friendship.getState()));
            player.sendMessage(Component.text("Requested at: " + friendship.getRequestedAt()));
            player.sendMessage(Component.text("Accepted at: " + friendship.getAcceptedAt()));
            player.sendMessage(Component.text("\n"));
        }
    }

}
