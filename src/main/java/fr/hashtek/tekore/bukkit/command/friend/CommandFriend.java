package fr.hashtek.tekore.bukkit.command.friend;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bukkit.Tekore;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.common.player.friend.PlayerFriendLink;
import fr.hashtek.tekore.common.player.friend.PlayerFriendManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandFriend implements CommandExecutor, HashLoggable
{

    private final Tekore core;
    private final HashLogger logger;


    /**
     * Creates a new instance of CommandLogs
     *
     * @param   core    Tekore instance
     */
    public CommandFriend(Tekore core)
    {
        this.core = core;
        this.logger = this.core.getHashLogger();
    }


    private boolean sendSyntax(Player sender)
    {
        final List<String> helpContent = Arrays.asList(
            "/friends",
            "Affiche les commandes disponibles.",
            "/friends",

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

        final TextComponent message = new TextComponent("");

        final TextComponent header = new TextComponent(
            ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "-------- " +
            ChatColor.GREEN + ChatColor.UNDERLINE + "Système d'amis" +
            ChatColor.DARK_GREEN + ChatColor.STRIKETHROUGH + " --------"
        );

        message.addExtra(header);
        message.addExtra(new TextComponent("\n"));

        for (int k = 0; k < helpContent.size(); k += 3) {
            final TextComponent command = new TextComponent(ChatColor.GREEN + helpContent.get(k));
            command.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, helpContent.get(k + 2)));
            command.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(helpContent.get(k + 2))));

            final TextComponent description = new TextComponent(ChatColor.DARK_GREEN + " ▸ " + helpContent.get(k + 1));

            message.addExtra(command);
            message.addExtra(description);
            message.addExtra(new TextComponent("\n"));
        }

        final TextComponent footer = new TextComponent(
            ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "--------------------------------"
        );

        message.addExtra(footer);
        message.addExtra(new TextComponent("\n"));

        this.logger.debug(this, message.toLegacyText());

        sender.spigot().sendMessage(message);

        return true;
    }

    /**
     * Command parsing.
     *
     * @param	sender	Player who executed the command
     * @param	args	Arguments passed
     */
    private boolean parseInput(Player sender, String[] args)
    {
        if (args.length == 0) {
            this.sendSyntax(sender);
            return false;
        }

        return true;
    }

    private void list(Player sender, PlayerFriendManager friendManager, String[] args)
    {
        if (friendManager.getFriendLinks() == null ||
            friendManager.getFriendLinks().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas d'amis. Vous êtes vraiment une merde.");
            return;
        }

        for (PlayerFriendLink link : friendManager.getFriendLinks()) {
            final PlayerManager pManager = new PlayerManager(link.getUuid());

            sender.sendMessage(pManager.getData().getUsername());
        }

        this.logger.info(this, "Command executed successfully.");
    }

    private void gui(Player sender, PlayerFriendManager friendManager, String[] args)
    {
        if (friendManager.getFriendLinks() == null ||
                friendManager.getFriendLinks().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas d'amis. Vous êtes vraiment une merde.");
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "En cours de développement...");
        // ...

        this.logger.info(this, "Command executed successfully.");
    }

    private void add(Player sender, PlayerFriendManager friendManager, String[] args)
    {
        this.logger.info(this, "Command executed successfully.");
    }

    private void remove(Player sender, PlayerFriendManager friendManager, String[] args)
    {
        this.logger.info(this, "Command executed successfully.");
    }

    /**
     * Called when command is executed.
     *
     * @param	sender	    Player who executed the command
     * @param   command     Command
     * @param   label       Command
     * @param	args	    Arguments passed
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Vous devez être un joueur pour exécuter cette commande !");
            return true;
        }

        final Player player = (Player) sender;
        final PlayerManager playerManager = this.core.getPlayerManager(player);
        final PlayerFriendManager friendManager = playerManager.getFriendManager();

        this.logger.info(this, "Executed by " + player.getName() + " (" + player.getUniqueId() + ")");

        if (!parseInput(player, args))
            return true;

        switch (args[0]) {
            case "help":
                return this.sendSyntax(player);
            case "list":
                list(player, friendManager, args);
                break;
            case "gui":
                gui(player, friendManager, args);
                break;
            case "add":
                add(player, friendManager, args);
                break;
            case "remove":
            case "rm":
                remove(player, friendManager, args);
                break;
            default:
                player.chat("/f");
                return true;
        }

        return true;
    }

}
