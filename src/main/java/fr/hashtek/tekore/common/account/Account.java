package fr.hashtek.tekore.common.account;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.account.settings.AccountSettingsManager;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.common.party.io.PartyProvider;
import fr.hashtek.tekore.common.rank.Rank;
import fr.hashtek.tekore.common.rank.RankProvider;
import fr.hashtek.tekore.spigot.Tekore;
import org.redisson.api.RFuture;

/**
 * Unless you want beef with Redis, NEVER move
 * this file and NEVER rename its parent package.
 */
public class Account
{

    private static final Tekore CORE = Tekore.getInstance();


    private final String uuid;
    private String username;

    private Timestamp createdAt;
    private Timestamp lastUpdate;

    private Rank rank;

    private Party party;

    private int coins;
    private int hashCoins;

    private AccountSettingsManager settingsManager;


    /**
     * Creates an empty account.
     *
     * @apiNote Solely used for Redis stuff. Not for public use!
     */
    public Account()
    {
        this("");
    }

    /**
     * Creates a new Account.
     * <p>
     * To set the values inside, please use the setters.
     * </p>
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
            .setRank(Constants.DEFAULT_RANK_UUID)
            .setParty((Party) null)
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
     * @return  Player's party
     */
    @JsonIgnore
    public Party getParty()
    {
        return this.party;
    }

    /**
     * @return  Player's party UUID
     * @apiNote Solely used for Redis access. Prefer using {@link Account#getParty()#getUuid()}.
     */
    @JsonGetter("party")
    public String getPartyUuid()
    {
        if (this.party == null) {
            return null;
        }
        return this.party.getUuid();
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
     * @return  Itself
     */
    public Account setUsername(String username)
    {
        this.username = username;
        return this;
    }

    /**
     * @param   createdAt   New creation date timestamp
     * @return  Itself
     */
    public Account setCreatedAt(Timestamp createdAt)
    {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @param   lastUpdate  New last update timestamp
     * @return  Itself
     */
    public Account setLastUpdate(Timestamp lastUpdate)
    {
        this.lastUpdate = lastUpdate;
        return this;
    }

    /**
     * @param   rank    New rank
     * @return  Itself
     */
    public Account setRank(Rank rank)
    {
        this.rank = rank;
        return this;
    }

    /**
     * @param   rankUuid    New rank's UUID
     * @return  Itself
     * @apiNote Solely used for Redis access. If you're not, please use {@link Account#setRank(Rank)}.
     */
    @JsonSetter("rank")
    public Account setRank(String rankUuid)
    {
        this.rank = new Rank(rankUuid);
        return this;
    }

    /**
     * @param   party   New party
     * @return  Account
     */
    public Account setParty(Party party)
    {
        this.party = party;
        return this;
    }

    /**
     * @param   partyUuid   New party's UUID
     * @return  Itself
     * @apiNote Solely used for Redis access. Prefer using {@link Account#setParty(Party)}.
     */
    @JsonSetter("party")
    public Account setParty(String partyUuid)
    {
        this.party = new Party(partyUuid);
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
     * @return  Itself
     */
    public Account setHashCoins(int hashCoins)
    {
        this.hashCoins = hashCoins;
        return this;
    }

    /**
     * @param   settingsManager     New settings manager
     * @return  Itself
     * @apiNote Solely used for Redis access. If not please just edit the values in the existing settings manager.
     */
    public Account setSettingsManager(AccountSettingsManager settingsManager)
    {
        this.settingsManager = settingsManager;
        return this;
    }

}
