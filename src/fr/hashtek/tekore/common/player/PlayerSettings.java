package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.player.settings.SettingsFriendRequests;
import fr.hashtek.tekore.common.player.settings.SettingsPrivateMessages;

public class PlayerSettings
{

    private boolean showLobbyPlayers;
    private SettingsFriendRequests friendRequestsSetting;
    private SettingsPrivateMessages privateMessagesSetting;


    /**
     * Creates an empty instance of PlayerSettings.
     */
    public PlayerSettings() {}


    /**
     * Returns true if players in lobby should be shown.
     *
     * @return  Lobby players setting
     */
    public boolean getLobbyPlayersSetting()
    {
        return showLobbyPlayers;
    }

    /**
     * Returns player's friend requests setting.
     *
     * @return  Friend requests setting
     */
    public SettingsFriendRequests getFriendRequestsSetting()
    {
        return friendRequestsSetting;
    }

    /**
     * Returns player's private messages setting.
     *
     * @return  Private messages setting
     */
    public SettingsPrivateMessages getPrivateMessagesSetting()
    {
        return privateMessagesSetting;
    }

    /**
     * Sets player's lobby players setting.
     *
     * @param   showLobbyPlayers    Should players in lobby be shown?
     */
    public void setLobbyPlayersSetting(boolean showLobbyPlayers)
    {
        this.showLobbyPlayers = showLobbyPlayers;
    }

    /**
     * Sets player's friend requests setting.
     *
     * @param   friendRequestsSetting   Friend requests setting
     */
    public void setFriendRequestsSetting(SettingsFriendRequests friendRequestsSetting)
    {
        this.friendRequestsSetting = friendRequestsSetting;
    }

    /**
     * Sets player's private messages setting.
     *
     * @param   privateMessagesSetting  Private messages setting
      */
    public void setPrivateMessagesSetting(SettingsPrivateMessages privateMessagesSetting)
    {
        this.privateMessagesSetting = privateMessagesSetting;
    }

}
