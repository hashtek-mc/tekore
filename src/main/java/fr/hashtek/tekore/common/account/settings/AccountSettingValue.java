package fr.hashtek.tekore.common.account.settings;

/**
 * Template for any player setting.
 * <br>
 * This interface contains all the minimal functions that
 * a setting value must have.
 */
public interface AccountSettingValue
{

    /**
     * @return  Setting's name
     */
    String getName();

    /**
     * @return  Setting's color
     */
    String getColor();

    /**
     * @return  Next setting
     */
    AccountSettingValue next();

}
