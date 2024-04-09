# ☢️ ️Tekore

**Coeur du serveur.**

Ce plugin s'occupe de toute la synchronisation entre les données des joueurs
et la base de données SQL. Il s'assure du bon fonctionnement des serveurs
entre eux.

> [!IMPORTANT]
> Tous les plugins de Hashtek doivent impérativement utiliser Tekore.\
> Vous devez impérativement mettre le .jar dans le dossier `plugins`
de votre serveur.

[🇬🇧 Also available in English!](https://github.com/hashtek-mc/tekore/blob/main/README-en.md)

## Utilisation

Pour utiliser Tekore, vous devez stocker une instance de ce dernier à la racine
de votre plugin.

**Exemple :**
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

> [!IMPORTANT]
> Initialisez toujours Tekore avec un bloc `try/catch` as cas-où Tekore ne se charge pas correctement
(à cause d'une mauvaise configuration par exemple).

### Fonctionnalités

* `Tekore#getRanks()` : Renvoie tous les ranks existants

## HashLogger

Afin de centraliser les logs (et peut-être dans le futur faire un historique
des logs), vous devez utiliser l'instance de HashLogger présente dans le Tekore.

Il s'obtient avec la fonction `Tekore#getHashLogger()`.

Voir [l'exemple ci-dessous](#utilisation-1).

## PlayerData

La classe `PlayerData` est une classe stockant l'intégralité des données des
joueurs. Cette classe est la raison principale de pourquoi nous avons fait
ce plugin.

### Utilisation

Vous vous servirez de Tekore pour récupérer les données des joueurs, les
modifier et les enregistrer dans la base de données.

Dans un fichier d'event par exemple, voici comment récupérer les données des
joueurs, les modifier et les enregistrer dans la base de données :

```java
public class JoinEvent implements Listener, HashLoggable {
    
    private Tekore core;
    private HashLogger logger; // Logger du Tekore
    
    public JoinEvent(Tekore core)
    {
        this.core = core;
        this.logger = this.core.getHashLogger(); // Récupération du logger du Tekore
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        PlayerData playerData = this.core.getPlayerData(player); // Récupération des données
        
        /* Modifiez ce que vous voulez */
        
        // Mise à jour des données dans la base de données.
        try {
            this.core.getAccountManager().updatePlayerAccount(playerData);
        } catch (SQLException exception) {
            this.logger.critical(this, "Failed to update PlayerData.", exception);
        }
    }
    
}
```

> [!TIP]
> Il est fortement recommandé de faire passer l'instance de Tekore dans le
constructeur de la classe, et non pas de rendre l'instance statique à la
racine du plugin.

> [!NOTE]
> Lorsqu'un joueur se déconnecte, ses données sont automatiquement mises à jour
dans la base de données.

### Fonctionnalités

* `setRank()` : Met à jour le rank du joueur

> [!CAUTION]
> Toutes les fonctions qui ne sont pas écrites ci-dessus ne sont pas censées
être utilisées par autre chose que le Tekore !

## Base de données

Pour que le plugin puisse fonctionner, il a besoin d'une base de données avec
une structure précise.

Vous aurez besoin de :\
\- [MySQL](https://www.mysql.com/)\
\- [PhpMyAdmin](https://www.phpmyadmin.net/)\
\- [Apache](https://httpd.apache.org/)

Une fois ces logiciels installés, importez la base de données
[`hashtekdb.sql`](https://github.com/hashtek-mc/hashrc/blob/main/hashtekdb.sql)
avec PhpMyAdmin.

## Fichiers de configuration

### HashLogger

`loggerLevel` : Niveau de log (voir [HashLogger](https://github.com/hashtek-mc/hashlogger/blob/main/README.md))

### Base de données (`.env`)

`DB_DATABASE` : Nom de la base de données (`hashtekdb`)\
`DB_HOST` : IP de la base de données (`127.0.0.1` pour du local)\
`DB_PORT` : Port à utiliser (par défaut `3306`)\
`DB_USER` : Nom d'utilisateur (`root` par défaut)\
`DB_PASSWORD` : Mot de passe (rien par défaut)

> [!IMPORTANT]
> Vous devez créer le `.env` à la racine du serveur.

## Fait avec 💜 par [Lysandre B.](https://github.com/Shuvlyy) ・ [![wakatime](https://wakatime.com/badge/user/2f50fe6c-0368-4bef-aa01-3a67193b63f8/project/018d5ee2-0b76-40e6-85c7-41444ac26120.svg)](https://wakatime.com/badge/user/2f50fe6c-0368-4bef-aa01-3a67193b63f8/project/018d5ee2-0b76-40e6-85c7-41444ac26120)
