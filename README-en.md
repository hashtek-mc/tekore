# ☢️ ️Tekore

**Server's core.**

This plugin handles synchronisation between player's data and the SQL database.
It ensures that servers work properly between them.

`⚠️` Every Hashtek plugin MUST use Tekore.

`ℹ️` You must put the .jar file in the `plugins` folder in your server.

## Usage

To use Tekore, you have to store an instance at the root of your plugin.

**Example :**
```java
public class Test extends JavaPlugin {

    private Tekore core;
    
    @Override
    public void onEnable()
    {
        try { // Important !
            this.core = Tekore.getInstance();
        } catch (NullPointerException exception) {
            System.err.println("Tekore failed to load. Stopping.");
            this.getServer().shutdown();
            return;
        }
    }
    
    public Tekore getCore()
    {
        return this.core;
    }
    
}
```
`⚠️` You must surround Tekore's loading with a `try/catch` block in case Tekore doesn't load properly.
(incorrect configuration, for example).

### Features

* `Tekore#getRanks()` : Returns every existing rank

## HashLogger

In order to centralize logs (and maybe in the future create a log history), you need to use the HashLogger
instance present in the Tekore.

You can get it using `Tekore#getHashLogger()`.

See [the example](#utilisation-1).

## PlayerData

The `PlayerData` class stores all player data.
This class is the main reason why we made this plugin.

### Usage

You will use Tekore to get player's data, modify it and save it to the database.

In an event handler, for example, here is how to get player's data, edit it and save it to the database:

```java
public class JoinEvent implements Listener, HashLoggable {
    
    private Tekore core;
    private HashLogger logger; // Tekore's Logger
    
    public JoinEvent(Tekore core)
    {
        this.core = core;
        this.logger = this.core.getHashLogger(); // Here we get Tekore's logger
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        PlayerData playerData = this.core.getPlayerData(player); // Data fetching
        
        /* Edit whatever you want */
        
        // Save data to the database.
        try {
            this.core.getAccountManager().updatePlayerAccount(playerData);
        } catch (SQLException exception) {
            this.logger.critical(this, "Failed to update PlayerData.", exception);
        }
    }
    
}
```

`⚠️` It is strongly recommended that you pass the Tekore instance to the class constructor,
and not to make the instance static in the root of the plugin.

`ℹ️` Whenever a player disconnects from the server, its data will be automatically saved to the database.

### Features

* `setRank()` : Sets a player's rank

`⚠️` All functions not listed above are not intended to be used by anything other than the Tekore!

## Database

To make the plugin work, it needs a database with a precise structure.

You will need:\
\- [MySQL](https://www.mysql.com/)\
\- [PhpMyAdmin](https://www.phpmyadmin.net/)\
\- [Apache](https://httpd.apache.org/)

Once these programs have been installed, import the
[`hashtekdb.sql`](https://github.com/hashtek-mc/hashrc/blob/main/hashtekdb.sql)
database with PhpMyAdmin.

## Configuration files

### HashLogger (`/plugins/Tekore`)

`logger-level` : Log level (see [HashLogger](https://github.com/hashtek-mc/hashlogger/blob/main/README.md))

### Database (`/.env`)

`DATABASE` : Database name (`hashtekdb`)\
`HOST` : Database's IP (`127.0.0.1`)\
`USER` : Username (`root` by default)\
`PASSWORD` : Password (nothing by default)

## Made with 💜 by [Lysandre B.](https://github.com/Shuvlyy) ・ [![wakatime](https://wakatime.com/badge/user/2f50fe6c-0368-4bef-aa01-3a67193b63f8/project/018d5ee2-0b76-40e6-85c7-41444ac26120.svg)](https://wakatime.com/badge/user/2f50fe6c-0368-4bef-aa01-3a67193b63f8/project/018d5ee2-0b76-40e6-85c7-41444ac26120)