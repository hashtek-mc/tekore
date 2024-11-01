package fr.hashtek.tekore.spigot.command.friend;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.friendship.FriendshipManager;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.command.friend.subcommand.SubcommandFriendInformations;
import fr.hashtek.tekore.spigot.command.friend.subcommand.SubcommandFriendManagement;
import fr.hashtek.tekore.spigot.command.friend.subcommand.SubcommandFriendRequests;
import net.kyori.adventure.text.Component;
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
            /* Informations ----------------------------------------------------------------- */
            case "help":
                sendSyntax(player);
                break;
            case "list":
                new SubcommandFriendInformations().list(player, playerFriendshipManager, args);
                break;
            case "info": // TODO: You may want to delete this.
                // ...
                break;
            /* ------------------------------------------------------------------------------ */

            /* Management ------------------------------------------------------------------- */
            case "add":
                new SubcommandFriendManagement().add(player, playerFriendshipManager, args);
                break;
            case "rm":
            case "remove":
                new SubcommandFriendManagement().remove(player, playerFriendshipManager, args);
                break;
            /* ------------------------------------------------------------------------------ */

            /* Requests --------------------------------------------------------------------- */
            case "accept":
                new SubcommandFriendRequests().accept(player, playerFriendshipManager, args);
                break;
            case "deny":
                new SubcommandFriendRequests().deny(player, playerFriendshipManager, args);
                break;
            /* ------------------------------------------------------------------------------ */

            /* Unknown flag ----------------------------------------------------------------- */
            default:
                player.sendMessage(Component.text("Wrong syntax. Type /friend help for help."));
                break;
            /* ------------------------------------------------------------------------------ */
        }
    }

}
