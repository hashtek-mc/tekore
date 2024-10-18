package fr.hashtek.tekore.spigot.commands.friend;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.commands.AbstractCommand;
import fr.hashtek.tekore.common.exceptions.EntryNotFoundException;
import fr.hashtek.tekore.common.friendship.Friendship;
import fr.hashtek.tekore.common.friendship.FriendshipManager;
import fr.hashtek.tekore.spigot.Tekore;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CommandFriend
    extends AbstractCommand
{

    private static final Tekore CORE = Tekore.getInstance();


    /**
     * Sends the syntax of the command to the player
     *
     * @param   player  Player
     */
    private void sendSyntax(Player player)
    {
        final List<String> helpContent = Arrays.asList(
            "/friends",                             // Command
            "Affiche les commandes disponibles.",   // Command description
            "/friends",                             // Command that will be written in player's chat on click

            "/friends list (page)",
            "Affiche votre liste d'amis.",
            "/friends list",

            "/friends add (joueur)",
            "Envoie une demande d'ami à un joueur.",
            "/friends add",

            "/friends remove/rm (joueur)",
            "Retire un joueur de votre liste d'amis.",
            "/friends remove"
        );

        for (String line : helpContent) {
            player.sendMessage(Component.text(line));
        }
    }


    private void list(
        Player player,
        FriendshipManager playerFriendshipManager,
        String[] args
    )
    {
        if (playerFriendshipManager.getFriendships().isEmpty()) {
            player.sendMessage(Component.text("You don't have any friendships :("));
            return;
        }

        player.sendMessage(Component.text("Here are all your friendships:\n"));

        for (Friendship friendship : playerFriendshipManager.getFriendships()) {
            player.sendMessage(Component.text("Friendship " + friendship.getUuid()));
            player.sendMessage(Component.text("Sender UUID: " + friendship.getSenderUuid()));
            player.sendMessage(Component.text("Receiver UUID: " + friendship.getReceiverUuid()));
            player.sendMessage(Component.text("State: " + friendship.getState()));
            player.sendMessage(Component.text("Requested at: " + friendship.getRequestedAt()));
            player.sendMessage(Component.text("Accepted at: " + friendship.getAcceptedAt()));
            player.sendMessage(Component.text("\n"));
        }
    }

    private void add(
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

        if (playerFriendshipManager.hasFriendshipWith(targetAccount.getUuid())) {
            player.sendMessage(Component.text("You already have a friendship with that player."));
            return;
        }

        try {
            playerFriendshipManager.createFriendship(targetAccount.getUuid());

            player.sendMessage(Component.text("You asked " + ChatColor.AQUA + targetAccount.getUsername() + ChatColor.RESET + " for a friendship!"));

            CORE.getMessenger().sendPluginMessage(
                player,
                "MessageRaw",
                targetAccount.getUsername(),
                "[\"\",{\"text\":\"Shuvly\",\"color\":\"light_purple\"},{\"text\":\" vous a envoyé une demande d'ami.\\n\"},{\"text\":\"[ACCEPTER]\",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/friend accept Shuvly\"}},{\"text\":\" -\",\"color\":\"dark_gray\"},{\"text\":\" \"},{\"text\":\"[REFUSER]\",\"bold\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/friend deny Shuvly\"}},{\"text\":\" -\",\"color\":\"dark_gray\"},{\"text\":\" \"},{\"text\":\"[BLOQUER]\",\"bold\":true,\"color\":\"gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/friend block Shuvly\"}}]"
            );
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(exception.getMessage());
        }
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    )
    {
        if (args.length == 0) {
            player.chat("/friend list");
            return;
        }

        final FriendshipManager playerFriendshipManager =
            CORE.getPlayerManagersManager()
                .getPlayerManager(player)
                .getFriendshipManager();

        /* Dispatch */
        switch (args[0]) {
            case "help":
                sendSyntax(player);
                break;
            case "list":
                list(player, playerFriendshipManager, args);
                break;
            case "info":
                // ...
                break;
            case "add":
                add(player, playerFriendshipManager, args);
                break;
            case "rm":
            case "remove":
                // ...
                break;
            case "accept":
                // ...
                break;
            case "deny":
                // ...
                break;
            default:
                this.sendSyntax(player);
                break;
        }
    }

}
