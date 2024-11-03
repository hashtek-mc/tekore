package fr.hashtek.tekore.common.command;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractCommandManager
    implements HashLoggable
{

    private static final HashLogger LOGGER = Tekore.getInstance().getHashLogger();


    /**
     * Creates a new Command manager
     *
     * @param   pluginManager   Plugin manager used to register the commands
     */
    public AbstractCommandManager(PluginManager pluginManager)
    {
        this.registerCommands(pluginManager);
    }


    /**
     * Registers commands in the server.
     *
     * @param   pluginManager   Plugin manager used to register the commands
     */
    protected abstract void registerCommands(PluginManager pluginManager);

    /**
     * Registers a command in the server.
     *
     * @param   command         Command as string (in <code>/friend</code>, <code>friend</code> is the command)
     * @param   commandClass    Command class
     */
    protected void registerCommand(String command, Class<? extends AbstractCommand> commandClass)
    {
        try {
            final AbstractCommand commandInstance = commandClass.getDeclaredConstructor().newInstance();

            Tekore.getInstance().getServer().getPluginCommand(command).setExecutor(commandInstance);
            LOGGER.info(this, "Command \"" + command + "\" registered!");
        }
        catch (
            InstantiationException |
            IllegalAccessException |
            InvocationTargetException |
            NoSuchMethodException exception
        ) {
            LOGGER.critical(this, "Error while registering command \"" + command + "\"!", exception);
        }
    }

}
