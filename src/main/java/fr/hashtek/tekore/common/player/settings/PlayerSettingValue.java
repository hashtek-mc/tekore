package fr.hashtek.tekore.common.player.settings;

/**
 * Template for any player setting.
 * This interface contains all the minimal functions that
 * a setting class must have.
 */
public interface PlayerSettingValue
{

    /**
     * @return  Setting's name
     */
    public String getName();

    /**
     * @return  Setting's color
     */
    public String getColor();

}
