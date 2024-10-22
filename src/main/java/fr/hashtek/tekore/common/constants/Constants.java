package fr.hashtek.tekore.common.constants;

public class Constants
{

    /* Default values ---------------------------------------------------- */
    public static final String DEFAULT_RANK_UUID = "4a20727b-5309-56cf-8145-1e1c24fd2cc5"; // "7cdb430d-491f-5c0e-a0f8-2be1cfc7360e";
    /*                                              ^ Admin rank                               ^ Player rank                       */
    /* ------------------------------------------------------------------- */

    /* Redis prefix keys ------------------------------------------------- */
    public static final String ACCOUNT_PREFIX_KEY = "account:";
    public static final String RANK_PREFIX_KEY = "rank:";
    public static final String GUILD_PREFIX_KEY = "guild:";
    public static final String PARTY_PREFIX_KEY = "party:";
    public static final String GAME_PREFIX_KEY = "game:";

    public static final String FRIENDSHIP_PREFIX_KEY = "friendship:";
    public static final String FRIENDSHIP_INVOLVES_PREFIX_KEY = "involves:";
    /* ------------------------------------------------------------------- */

    /* Bungeecord (sub)channels ------------------------------------------ */
    public static final String BUNGEECORD_CHANNEL = "BungeeCord";

    public static final String UPDATE_FRIENDS_SUBCHANNEL = "UpdateFriends";
    /* ------------------------------------------------------------------- */

    /* Symbols ----------------------------------------------------------- */
    public static final String COINS_SYMBOL = "⛂";
    public static final String HASHCOINS_SYMBOL = "⛃";
    /* ------------------------------------------------------------------- */

}
