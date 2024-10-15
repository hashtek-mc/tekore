package fr.hashtek.tekore.common.account;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import fr.hashtek.tekore.common.account.settings.AccountSettingsManager;

/**
 * Unless you want beef with Redis, NEVER move
 * this file and NEVER rename its parent package.
 */
public class Account
{

    private final String uuid;
    private String username;

    private Timestamp createdAt;
    private Timestamp lastUpdate;

    private Rank rank;

    private int coins;
    private int hashCoins;

    private AccountSettingsManager settingsManager;


    /**
     * Creates an empty account.
     *
     * @apiNote Solely used for Redis stuff, not for public use.
     */
    public Account()
    {
        this("");
    }

    /**
     * Creates a new Account.
     * <p>
     * To set the values inside, please use the setters.
     *
     * @param   uuid    Account's UUID
     * @apiNote Should never be instantiated anywhere other than in {@link AccountProvider}.
     */
    public Account(String uuid)
    {
        final Timestamp now = new Timestamp(System.currentTimeMillis());

        this.uuid = uuid;

        this.setUsername("")
            .setCreatedAt(now)
            .setLastUpdate(now)
            .setRank("player")
            .setCoins(0)
            .setHashCoins(0);

        this.settingsManager = new AccountSettingsManager();
    }


    /**
     * @return  Account's UUID
     */
    public String getUuid()
    {
        return this.uuid;
    }

    /**
     * @return  Account's username
     */
    public String getUsername()
    {
        return this.username;
    }

    /**
     * @return  Account's creation date
     */
    public Timestamp getCreatedAt()
    {
        return this.createdAt;
    }

    /**
     * @return  Account's last update date
     */
    public Timestamp getLastUpdate()
    {
        return this.lastUpdate;
    }

    /**
     * @return  Account's rank's UUID
     * @apiNote Solely used for Redis access. If you're not, please use {@link Account#getRank()#getUuid()}.
     */
    @JsonGetter("rank")
    public String getRankUuid()
    {
        return this.rank.getUuid();
    }

    /**
     * @return  Account's rank
     */
    public Rank getRank()
    {
        return this.rank;
    }

    /**
     * @return  Account's amount of coins
     */
    public int getCoins()
    {
        return this.coins;
    }

    /**
     * @return  Account's amount of HashCoins
     */
    public int getHashCoins()
    {
        return this.hashCoins;
    }

    /**
     * @return  Account's settings manager
     */
    @JsonGetter("settings")
    public AccountSettingsManager getSettingsManager()
    {
        return this.settingsManager;
    }

    /**
     * @param   username    Account's new username
     * @return  Account
     */
    public Account setUsername(String username)
    {
        this.username = username;
        return this;
    }

    /**
     * @param   createdAt   New creation date timestamp
     * @return  Account
     */
    public Account setCreatedAt(Timestamp createdAt)
    {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @param   lastUpdate  New last update timestamp
     * @return  Account
     */
    public Account setLastUpdate(Timestamp lastUpdate)
    {
        this.lastUpdate = lastUpdate;
        return this;
    }

    /**
     * @param   rankUuid    New rank's UUID
     * @return  Account
     * @apiNote Solely used for Redis access. If you're not, please use {@link Account#setRank(Rank)}.
     */
    @JsonSetter("rank")
    public Account setRank(String rankUuid)
    {
        this.rank = new Rank(rankUuid); // TODO: Don't forget to update the rank afterwards.
        return this;
    }

    /**
     * @param   rank    New rank
     * @return  Account
     */
    public Account setRank(Rank rank)
    {
        this.rank = rank;
        return this;
    }

    /**
     * @param   coins   New amount of coins
     * @return  Account
     */
    public Account setCoins(int coins)
    {
        this.coins = coins;
        return this;
    }

    /**
     * @param   hashCoins   New amount of HashCoins
     * @return  Account
     */
    public Account setHashCoins(int hashCoins)
    {
        this.hashCoins = hashCoins;
        return this;
    }

    /**
     * @param   settingsManager     New settings manager
     * @return  Account
     * @apiNote Should only be used by Redisson.
     *          Otherwise please just edit the values in the existing settings manager.
     */
    public Account setSettingsManager(AccountSettingsManager settingsManager)
    {
        this.settingsManager = settingsManager;
        return this;
    }

}
