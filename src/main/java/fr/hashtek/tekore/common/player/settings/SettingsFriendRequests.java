package fr.hashtek.tekore.common.player.settings;

public enum SettingsFriendRequests implements PlayerSetting
{

    /**
     * Accept friend requests from all players.
     */
    ALL ("Tout le monde", "§b"),

    /**
     * Accept friend requests from friends of friends.
     */
    FRIENDS_OF_FRIENDS ("Amis d'amis uniquement", "§2"),

    /**
     * Do not accept friend requests from anyone.
     */
    NOBODY ("Personne", "§c");


    private static final String TITLE =
        "§b§lDemandes d'amis";
    private static final String DESCRIPTION =
        "§7Accepter de recevoir les §2demandes d'amis §7dans le serveur de :";

    private final String name;
    private final String color;


    /**
     * Creates a new instance of SettingsFriendRequests.
     *
     * @param   name    Setting's name
     * @param   color   Setting's color
     */
    SettingsFriendRequests(String name, String color)
    {
        this.name = name;
        this.color = color;
    }

    /**
     * @return  Setting title description
     */
    public static String getTitle()
    {
        return TITLE;
    }

    /**
     * @return  Setting category description
     */
    public static String getDescription()
    {
        return DESCRIPTION;
    }

    /**
     * @return  Setting's name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return  Setting's color
     */
    public String getColor()
    {
        return this.color;
    }

}
