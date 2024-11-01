package fr.hashtek.tekore.spigot;

import fr.hashtek.hashconfig.HashConfig;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.data.redis.RedisConfiguration;
import fr.hashtek.tekore.common.data.redis.RedisCredentials;
import fr.hashtek.tekore.common.player.PlayerManagersManager;
import fr.hashtek.tekore.spigot.command.CommandManager;
import fr.hashtek.tekore.spigot.listener.ListenerJoin;
import fr.hashtek.tekore.spigot.listener.ListenerQuit;
import fr.hashtek.tekore.spigot.messenger.TekoreMessenger;
import io.github.cdimascio.dotenv.Dotenv;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Tekore
    extends JavaPlugin
    implements HashLoggable
{

    private static Tekore INSTANCE;

    private HashLogger logger;
    private HashConfig config;

    private RedisAccess redisAccess;
    private TekoreMessenger messenger;

    private PlayerManagersManager playerManagersManager;


    /**
     * Called on server start.
     */
    @Override
    public void onEnable()
    {
        INSTANCE = this;

        this.setupConfig();
        this.setupHashLogger();

        logger.info(this, "Starting up...");

        this.initializeRedisAccess();
        this.initializePlayerManagersManager();

        this.loadMessenger();

        this.registerListeners();
        new CommandManager(this.getServer().getPluginManager());

        logger.info(this, "Successfully loaded.");
    }

    /**
     * Called on server stop.
     */
    @Override
    public void onDisable()
    {
        logger.info(this, "Disabling...");

        for (Player player: this.getServer().getOnlinePlayers()) {
            player.kick(Component.text("Nous revenons dans quelques minutes..."));
        }

        this.redisAccess.close();
        this.unloadMessenger();

        logger.info(this, "Successfully disabled.");
    }


    /**
     * Creates a new instance of HashConfig, to read configuration files.
     * </br>
     * This function doesn't use HashLogger because it is called before the
     * initialization of HashLogger. System.err.println is used instead.
     */
    private void setupConfig()
    {
        String configFilename = "config.yml";

        try {
            this.config = new HashConfig(
                this.getClass(),
                configFilename,
                this.getDataFolder().getPath() + "/" + configFilename,
                true
            );
        } catch (IOException exception) {
            System.err.println("Failed to read config file.");
            System.err.println(exception.getMessage());
            this.getServer().shutdown();
        }
    }

    /**
     * Creates an instance of HashLogger.
     * </br>
     * This function doesn't use HashLogger because it is called before the
     * initialization of HashLogger. System.err.println is used instead.
     */
    private void setupHashLogger()
    {
        try {
            this.logger = HashLogger.fromEnvConfig(this, this.config.getEnv());
        } catch (IllegalArgumentException | NullPointerException exception) {
            System.err.println("Can't initialize HashLogger. Stopping.");
            this.getServer().shutdown();
        }
    }

    /**
     * Initializes Redis access.
     */
    private void initializeRedisAccess()
    {
        logger.info(this, "Initializing Redis access...");

        final Dotenv env = this.getHashConfig().getEnv();

        this.redisAccess = new RedisAccess(
            new RedisCredentials(
                env.get("REDIS_HOST"),
                env.get("REDIS_PASSWORD")
            ),
            new RedisConfiguration()
        );

        logger.info(this, "Redis access successfully initialized.");
    }

    /**
     * Initializes Player managers manager.
     */
    private void initializePlayerManagersManager()
    {
        logger.info(this, "Initializing PlayerManagersManager...");

        this.playerManagersManager = new PlayerManagersManager();

        logger.info(this, "PlayerManagersManager successfully initialized.");
    }

    /**
     * Loads plugin's messenger.
     */
    private void loadMessenger()
    {
        this.logger.info(this, "Loading messenger...");

        this.messenger = new TekoreMessenger();
        this.messenger.load();

        this.logger.info(this, "Messenger loaded!");
    }

    /**
     * Unloads plugin's messenger.
     */
    private void unloadMessenger()
    {
        this.logger.info(this, "Unloading messenger...");

        this.messenger.unload();

        this.logger.info(this, "Messenger unloaded.");
    }


    /**
     * Registers event listeners.
     */
    private void registerListeners()
    {
        final PluginManager pluginManager = this.getServer().getPluginManager();

        logger.info(this, "Registering listeners...");

        pluginManager.registerEvents(new ListenerJoin(), this);
        pluginManager.registerEvents(new ListenerQuit(), this);

        logger.info(this, "Listeners loaded!");
    }


    /**
     * @return	Tekore instance
     */
    public static Tekore getInstance()
    {
        return INSTANCE;
    }

    /**
     * @return  Tekore's logger
     */
    public HashLogger getHashLogger()
    {
        return this.logger;
    }

    /**
     * @return  Configuration manager
     */
    public HashConfig getHashConfig()
    {
        return this.config;
    }

    /**
     * @return  Redis access
     */
    public RedisAccess getRedisAccess()
    {
        return this.redisAccess;
    }

    /**
     * @return  Tekore's messenger.
     * @apiNote Don't confuse with {@link fr.hashtek.tekore.bungee.messenger.TekordMessenger}.
     */
    public TekoreMessenger getMessenger()
    {
        return this.messenger;
    }

    /**
     * @return  Player managers manager
     */
    public PlayerManagersManager getPlayerManagersManager()
    {
        return this.playerManagersManager;
    }

}
