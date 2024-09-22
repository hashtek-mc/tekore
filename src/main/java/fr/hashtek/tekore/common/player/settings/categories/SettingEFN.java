package fr.hashtek.tekore.common.player.settings.categories;

import fr.hashtek.tekore.common.player.settings.PlayerSettingValue;

/**
 * <strong>EFN</strong> stands for:
 * <p>
 * <strong>E</strong><i>veryone</i>
 * <br/>
 * <strong>F</strong><i>riends only</i>
 * <br/>
 * <strong>N</strong><i>obody</i>
 */
public enum SettingEFN implements PlayerSettingValue
{

    EVERYONE        ("Tout le monde",   "§b"),
    FRIENDS_ONLY    ("Amis uniquement", "§2"),
    NOBODY          ("Personne",        "§c");


    private final String name;
    private final String color;


    /**
     * Creates a new EFN-typed setting.
     *
     * @param   name    Setting name
     * @param   color   Setting color
     */
    SettingEFN(String name, String color)
    {
        this.name = name;
        this.color = color;
    }


    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getColor()
    {
        return this.color;
    }

    @Override
    public SettingEFN next()
    {
        return values()[(ordinal() + 1) % values().length];
    }

}
