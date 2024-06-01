package fr.hashtek.tekore.bukkit.command.whoami;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.tekore.bukkit.Tekore;
import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.player.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWhoAmI implements CommandExecutor, HashLoggable
{

    private final Tekore core;


    /**
     * Creates a new instance of CommandWhoAmI
     *
     * @param   core    Tekore instance
     */
    public CommandWhoAmI(Tekore core)
    {
        this.core = core;
        this.core.getHashLogger().info(this, "Command loaded.");
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
        if (!(sender instanceof Player))
            return true;

        final Player player = (Player) sender;
        final PlayerManager playerManager = this.core.getPlayerManager(player);
        final PlayerData playerData = playerManager.getData();
        final Rank playerRank = playerData.getRank();

        player.sendMessage(playerRank.getColor() + playerData.getUsername() + "@" + playerRank.getName());

        return true;
    }

}
