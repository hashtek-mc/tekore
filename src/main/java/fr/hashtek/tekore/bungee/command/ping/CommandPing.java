package fr.hashtek.tekore.bungee.command.ping;

import fr.hashtek.hashlogger.HashLoggable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandPing extends Command implements HashLoggable
{


    /**
     * Creates a new instance of CommandPing.
     * Used to register the command.
     */
    public CommandPing()
    {
        super("ping");
    }


    /**
     * Returns an appropriate color based on a given ping.
     *
     * @param   ping    Ping (in milliseconds)
     * @return  Ping color
     */
    private ChatColor getPingColor(int ping)
    {
        if (ping < 50)
            return ChatColor.DARK_GREEN;
        if (ping < 100)
            return ChatColor.GREEN;
        if (ping < 150)
            return ChatColor.GOLD;
        if (ping < 200)
            return ChatColor.RED;
        return ChatColor.DARK_RED;
    }

    /**
     * Called when command is executed.
     *
     * @param	sender	Player who executed the command
     * @param	args	Arguments passed
     */
    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (!(sender instanceof ProxiedPlayer))
            return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        int playerPing = player.getPing();
        ChatColor pingColor = this.getPingColor(playerPing);

        player.sendMessage(new TextComponent(ChatColor.AQUA + "Ping: " + pingColor + playerPing + "ms"));
    }
}
