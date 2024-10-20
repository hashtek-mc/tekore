package fr.hashtek.tekore.spigot.commands.friend.subcommands;

import fr.hashtek.tekore.common.friendship.Friendship;
import fr.hashtek.tekore.common.friendship.FriendshipManager;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SubcommandFriendInformations
{

    public void list(
        Player player,
        FriendshipManager playerFriendshipManager,
        String[] args
    )
    {
//        playerFriendshipManager.fetchFriendships();

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
