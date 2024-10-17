package fr.hashtek.tekore.bungee;

import fr.hashtek.hashconfig.HashConfig;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bungee.listeners.ListenerProxyDisconnect;
import fr.hashtek.tekore.bungee.listeners.ListenerProxyLogin;
import fr.hashtek.tekore.bungee.messenger.TekordMessenger;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.data.redis.RedisConfiguration;
import fr.hashtek.tekore.common.data.redis.RedisCredentials;
import fr.hashtek.tekore.common.player.PlayerManagersManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.IOException;

public class Tekord
    extends Plugin
    implements HashLoggable
{

    private static Tekord INSTANCE;

    private HashLogger logger;
    private HashConfig config;

    private RedisAccess redisAccess;
    private TekordMessenger messenger;


    /**
     * Called on proxy start.
     */
    @Override
    public void onEnable()
    {
        INSTANCE = this;

        this.setupConfig();
        this.setupHashLogger();

        logger.info(this, "Starting up...");

        this.initializeRedisAccess();

        this.loadMessenger();

        this.registerListeners();

        logger.info(this, "Successfully loaded.");
    }

    /**
     * Called on proxy stop.
     */
    @Override
    public void onDisable()
    {
        logger.info(this, "Disabling...");

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
            this.getProxy().stop();
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
            this.getProxy().stop();
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
     * Loads plugin's messenger.
     */
    private void loadMessenger()
    {
        logger.info(this, "Loading messenger...");

        this.messenger = new TekordMessenger();
        this.messenger.load();

        logger.info(this, "Messenger loaded!");
    }

    /**
     * Unloads plugin's messenger.
     */
    private void unloadMessenger()
    {
        logger.info(this, "Unloading messenger...");

        this.messenger.unload();

        logger.info(this, "Messenger unloaded.");
    }

    private void registerListeners()
    {
        final PluginManager pluginManager = this.getProxy().getPluginManager();

        logger.info(this, "Registering listeners...");

        pluginManager.registerListener(this, new ListenerProxyLogin());
        pluginManager.registerListener(this, new ListenerProxyDisconnect());

        logger.info(this, "Listeners registered.");
    }


    public static Tekord getInstance()
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
     * @return  Tekord's messenger
     * @apiNote Don't confuse with {@link fr.hashtek.tekore.spigot.messenger.TekoreMessenger}.
     */
    public TekordMessenger getMessenger()
    {
        return this.messenger;
    }

}
