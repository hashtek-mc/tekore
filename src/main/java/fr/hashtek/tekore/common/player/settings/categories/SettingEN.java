package fr.hashtek.tekore.common.player.settings.categories;

import fr.hashtek.tekore.common.player.settings.PlayerSettingValue;

/**
 * <strong>EN</strong> stands for:
 * <p>
 * <strong>E</strong><i>veryone</i>
 * <br/>
 * <strong>N</strong><i>obody</i>
 */
public enum SettingEN implements PlayerSettingValue
{

    EVERYONE    ("Tout le monde",   "§b"),
    NOBODY      ("Personne",        "§c");


    private final String name;
    private final String color;


    /**
     * Creates a new EN-typed setting.
     *
     * @param   name    Setting name
     * @param   color   Setting color
     */
    SettingEN(String name, String color)
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
    public SettingEN next()
    {
        return values()[(ordinal() + 1) % values().length];
    }

}
