package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.AccountProvider;
import fr.hashtek.tekore.common.account.AccountPublisher;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exceptions.EntryNotFoundException;
import fr.hashtek.tekore.common.exceptions.InvalidPlayerType;
import fr.hashtek.tekore.common.rank.Rank;
import fr.hashtek.tekore.common.rank.RankProvider;

import java.sql.Timestamp;

public class PlayerManager
    <T>
{

    private final T player;
    private Account account;


    /**
     * Creates a new PlayerManager for a player.
     *
     * @param   player  Associated player
     */
    public PlayerManager(
        T player,
        RedisAccess redisAccess
    )
        throws InvalidPlayerType
    {
        this.player = player;

        this.setPlayerAccount(redisAccess);
        this.fetchPlayerRank(redisAccess);
    }


    /**
     * Fetches the account of the player from the Redis database
     * then stores it in this class.
     *
     * @param   redisAccess     Redis access
     * @throws  InvalidPlayerType   If player type is invalid
     */
    private void setPlayerAccount(RedisAccess redisAccess)
        throws InvalidPlayerType
    {
        final String playerUuid = PlayerManagersManager.getUuid(this.player);
        final String playerName = PlayerManagersManager.getUsername(this.player);

        try {
            /* Try to fetch the account from the Redis database or the API. */
            this.account = new AccountProvider(redisAccess)
                .get(playerUuid);
        }
        catch (EntryNotFoundException exception) {
            /* If account does not exist, create it. */
            this.account = new Account(playerUuid)
                .setUsername(playerName);

            this.pushData(redisAccess);
        }
    }

    /**
     * At the very first state of a Rank, right after its creation,
     * only its UUID is stored in it.
     * <p>
     * What we're doing here is basically fetch the rank from the
     * Redis database and put it in the account, so that the
     * entire rank is stored, and we can access everything.
     *
     * @param   redisAccess     Redis access
     */
    private void fetchPlayerRank(RedisAccess redisAccess)
    {
        try {
            final Rank rank = new RankProvider(redisAccess)
                .get(this.account.getRank().getUuid());

            this.account.setRank(rank);
        }
        catch (EntryNotFoundException exception) {
            // TODO: Should NEVER happen but yeah log just in case
        }
    }


    /**
     * @return  Associated player
     */
    public T getPlayer()
    {
        return this.player;
    }

    /**
     * @return  Associated player's account
     */
    public Account getAccount()
    {
        return this.account;
    }


    /**
     * @param   account New account
     * @return  Player manager
     */
    public PlayerManager<T> setAccount(Account account)
    {
        this.account = account;
        return this;
    }


    /**
     * Fetches the fresh new account data from the
     * Redis database and then store it in RAM.
     *
     * @param   redisAccess     Redis access
     */
    public void refreshData(RedisAccess redisAccess)
    {
        try {
            final Account fetchedAccount = new AccountProvider(redisAccess)
                .get(this.account.getUuid());

            this.setAccount(fetchedAccount);
        }
        catch (EntryNotFoundException exception) {
            // TODO: Log?
        }
    }

    /**
     * Pushes player's account data to the Redis database.
     *
     * @param   redisAccess     Redis access
     */
    public void pushData(RedisAccess redisAccess)
    {
        /* Updating account's last update timestamp. */
        account.setLastUpdate(new Timestamp(System.currentTimeMillis()));

        new AccountPublisher(redisAccess)
            .push(this.account.getUuid(), account);
    }

}
