package fr.hashtek.tekore.common.account.settings.categories;

import fr.hashtek.tekore.common.account.settings.AccountSettingValue;

/**
 * <h3>EFFN</h3>
 * <i>Stands for:</i>
 * <p>
 * <strong>E</strong><i>veryone</i>
 * <br/>
 * <strong>F</strong><i>riends of </i><strong>F</strong><i>riends only</i>
 * <br/>
 * <strong>N</strong><i>obody</i>
 */
public enum SettingEFFN
    implements AccountSettingValue
{

    EVERYONE                ("Tout le monde",           "§b"),
    FRIENDS_OF_FRIENDS_ONLY ("Amis d'amis uniquement",  "§2"),
    NOBODY                  ("Personne",                "§c");


    private final String name;
    private final String color;


    /**
     * Creates a new EFFN-typed setting.
     *
     * @param   name    Setting name
     * @param   color   Setting color
     */
    SettingEFFN(
        String name,
        String color
    )
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
    public SettingEFFN next()
    {
        return values()[(ordinal() + 1) % values().length];
    }

}
