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
     * @return  Lobby players setting
     */
    public boolean getLobbyPlayersSetting()
    {
        return showLobbyPlayers;
    }

    /**
     * @return  Friend requests setting
     */
    public SettingsFriendRequests getFriendRequestsSetting()
    {
        return friendRequestsSetting;
    }

    /**
     * @return  Private messages setting
     */
    public SettingsPrivateMessages getPrivateMessagesSetting()
    {
        return privateMessagesSetting;
    }

    /**
     * @param   showLobbyPlayers    Should players in lobby be shown?
     */
    public void setLobbyPlayersSetting(boolean showLobbyPlayers)
    {
        this.showLobbyPlayers = showLobbyPlayers;
    }

    /**
     * @param   friendRequestsSetting   Friend requests setting
     */
    public void setFriendRequestsSetting(SettingsFriendRequests friendRequestsSetting)
    {
        this.friendRequestsSetting = friendRequestsSetting;
    }

    /**
     * @param   privateMessagesSetting  Private messages setting
      */
    public void setPrivateMessagesSetting(SettingsPrivateMessages privateMessagesSetting)
    {
        this.privateMessagesSetting = privateMessagesSetting;
    }

}
