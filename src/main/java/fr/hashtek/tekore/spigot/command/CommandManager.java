package fr.hashtek.tekore.spigot.command;

import fr.hashtek.tekore.common.command.AbstractCommandManager;
import fr.hashtek.tekore.spigot.command.debug.CommandAccountDump;
import fr.hashtek.tekore.spigot.command.debug.CommandAccountEdit;
import fr.hashtek.tekore.spigot.command.debug.CommandRankPush;
import fr.hashtek.tekore.spigot.command.friend.CommandFriend;
import fr.hashtek.tekore.spigot.command.party.CommandParty;
import org.bukkit.plugin.PluginManager;

public class CommandManager
    extends AbstractCommandManager
{

    public CommandManager(PluginManager pluginManager)
    {
        super(pluginManager);
    }


    @Override
    protected void registerCommands(PluginManager pluginManager)
    {
        super.registerCommand("accountdump", CommandAccountDump.class);
        super.registerCommand("accountedit", CommandAccountEdit.class);
        super.registerCommand("rankpush", CommandRankPush.class);

        super.registerCommand("friend", CommandFriend.class);
        super.registerCommand("party", CommandParty.class);
    }

}
