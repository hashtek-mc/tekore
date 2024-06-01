package fr.hashtek.tekore.common.player.settings;

public enum SettingsPrivateMessages implements PlayerSetting
{

    /**
     * Receive private message from all players.
     */
    ALL ("Tout le monde", "§b"),

    /**
     * Receive private messages from friends only.
     */
    FRIENDS_ONLY ("Amis uniquement", "§2"),

    /**
     * Do not accept any private messages.
     */
    NOBODY ("Personne", "§c");


    private static final String TITLE =
        "§b§lMessages privés";
    private static final String DESCRIPTION =
        "§7Accepter de recevoir les §emessages privés §7dans le serveur de :";

    private final String name;
    private final String color;


    /**
     * Creates a new instance of SettingsPrivateMessages.
     *
     * @param   name    Setting's name
     * @param   color   Setting's color
     */
    SettingsPrivateMessages(String name, String color)
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
