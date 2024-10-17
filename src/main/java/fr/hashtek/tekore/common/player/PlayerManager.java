package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.account.io.AccountPublisher;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.exceptions.EntryNotFoundException;
import fr.hashtek.tekore.common.friendship.FriendshipManager;
import fr.hashtek.tekore.common.rank.Rank;
import fr.hashtek.tekore.common.rank.RankProvider;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public class PlayerManager
{

    private final Player player;

    private Account account;
    private final FriendshipManager friendshipManager;


    /**
     * Creates a new PlayerManager for a player.
     *
     * @param   player  Associated player
     */
    public PlayerManager(
        Player player,
        RedisAccess redisAccess
    )
    {
        this.player = player;
        this.friendshipManager = new FriendshipManager(player);

        this.setPlayerAccount(player.getUniqueId().toString(), player.getName(), redisAccess);
    }


    /**
     * Fetches the account of the player from the Redis database
     * then stores it in this class.
     *
     * @param   redisAccess     Redis access
     */
    private void setPlayerAccount(
        String playerUuid,
        String playerName,
        RedisAccess redisAccess
    )
    {
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

        this.fetchPlayerRank(redisAccess);
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


    /**
     * @return  Associated player
     */
    public Player getPlayer()
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
     * @return  Associated Friendship manager
     */
    public FriendshipManager getFriendshipManager()
    {
        return this.friendshipManager;
    }

    /**
     * @param   account New account
     * @return  Player manager
     */
    public PlayerManager setAccount(Account account)
    {
        this.account = account;
        return this;
    }

}
