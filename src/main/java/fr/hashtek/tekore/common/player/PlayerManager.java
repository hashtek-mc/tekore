package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.AccountProvider;
import fr.hashtek.tekore.common.account.AccountPublisher;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exceptions.AccountNotFoundException;
import fr.hashtek.tekore.common.exceptions.InvalidPlayerType;

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

        Account account;
        final String playerUuid = PlayerManagersManager.getUuid(this.player);
        final String playerName = PlayerManagersManager.getUsername(this.player);

        try {
            /* Try to fetch the account from the Redis database or the API. */
            account = new AccountProvider()
                .getAccount(playerUuid, redisAccess);
        }
        catch (AccountNotFoundException exception) {
            /* If account does not exist, create it. */
            account = new Account(playerUuid)
                .setUsername(playerName);
            this.pushData(account, redisAccess);
        }

        this.account = account;
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
            final Account fetchedAccount = new AccountProvider()
                .getAccount(this.account.getUuid(), redisAccess);

            this.setAccount(fetchedAccount);
        }
        catch (AccountNotFoundException exception) {
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
        this.pushData(this.account, redisAccess);
    }

    /**
     * Same as {@link PlayerManager#pushData(RedisAccess)},
     * but with a specific account.
     * </br>
     * Only used in the constructor of this class, because
     * of design issues.
     *
     * @param   account         Account to push
     * @param   redisAccess     Redis access
     */
    private void pushData(Account account, RedisAccess redisAccess)
    {
        new AccountPublisher().sendAccountToRedis(account, redisAccess);
    }

    // TODO: Same pushData function as above but for the api.

}
