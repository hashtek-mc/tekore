package fr.hashtek.tekore.spigot.commands;

import fr.hashtek.tekore.common.commands.AbstractCommandManager;
import fr.hashtek.tekore.spigot.commands.debug.CommandAccountDump;
import fr.hashtek.tekore.spigot.commands.debug.CommandAccountEdit;
import fr.hashtek.tekore.spigot.commands.debug.CommandRankPush;
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
    }

}
