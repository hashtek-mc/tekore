package fr.hashtek.tekore.spigot.command.friend;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.spigot.command.friend.subcommand.request.SubcommandFriendAccept;
import fr.hashtek.tekore.spigot.command.friend.subcommand.request.SubcommandFriendAdd;
import fr.hashtek.tekore.spigot.command.friend.subcommand.info.SubcommandFriendList;
import fr.hashtek.tekore.spigot.command.friend.subcommand.request.SubcommandFriendDeny;
import fr.hashtek.tekore.spigot.command.friend.subcommand.management.SubcommandFriendRemove;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandFriend
    extends AbstractCommand
{

    /**
     * Creates the "friend" command.
     */
    public CommandFriend()
    {
        super(true);

        try {
            super.getSubcommandManager()
                .registerSubcommand(new SubcommandFriendList(this))
                .registerSubcommand(new SubcommandFriendAdd(this))
                .registerSubcommand(new SubcommandFriendRemove(this))
                .registerSubcommand(new SubcommandFriendAccept(this))
                .registerSubcommand(new SubcommandFriendDeny(this));
        }
        catch (InvalidCommandContextException exception) {
            // ...
        }
    }


    /**
     * Sends the syntax of the command to the player
     *
     * @param   player  Player
     */
//    private void sendSyntax(Player player)
//    {
//        final List<String> helpContent = Arrays.asList(
//            "/friends",                             // Command
//            "Affiche les commandes disponibles.",   // Command description
//            "/friends",                             // Command that will be written in player's chat on click
//
//            "/friends list (page)",
//            "Affiche votre liste d'amis.",
//            "/friends list",
//
//            "/friends add (joueur)",
//            "Envoie une demande d'ami à un joueur.",
//            "/friends add",
//
//            "/friends remove/rm (joueur)",
//            "Retire un joueur de votre liste d'amis.",
//            "/friends remove"
//        );
//
//        for (String line : helpContent) {
//            player.sendMessage(Component.text(line));
//        }
//    }

    @Override
    public void execute(
        @NotNull Player player,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    )
    {
        player.chat("/friend list");
    }

}
