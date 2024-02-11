package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.player.settings.FriendRequestsSetting;
import fr.hashtek.tekore.common.player.settings.PrivateMessagesSetting;

public class PlayerSettings {

    private boolean showLobbyPlayers;
    private FriendRequestsSetting friendRequestsSetting;
    private PrivateMessagesSetting privateMessagesSetting;


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
    public FriendRequestsSetting getFriendRequestsSetting()
    {
        return friendRequestsSetting;
    }

    /**
     * Returns player's private messages setting.
     *
     * @return  Private messages setting
     */
    public PrivateMessagesSetting getPrivateMessagesSetting()
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
    public void setFriendRequestsSetting(FriendRequestsSetting friendRequestsSetting)
    {
        this.friendRequestsSetting = friendRequestsSetting;
    }

    /**
     * Sets player's private messages setting.
     *
     * @param   privateMessagesSetting  Private messages setting
      */
    public void setPrivateMessagesSetting(PrivateMessagesSetting privateMessagesSetting)
    {
        this.privateMessagesSetting = privateMessagesSetting;
    }

}
