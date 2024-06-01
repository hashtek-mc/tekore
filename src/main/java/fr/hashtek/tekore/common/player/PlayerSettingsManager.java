package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.player.settings.categories.SettingEFFN;
import fr.hashtek.tekore.common.player.settings.categories.SettingEFN;
import fr.hashtek.tekore.common.player.settings.categories.SettingEN;

public class PlayerSettingsManager
{

    private SettingEN showLobbyPlayers;
    private SettingEFN privateMessagesSetting;
    private SettingEFFN friendRequestsSetting;
    private SettingEFFN partyRequestsSetting;
    private SettingEFFN guildRequestsSetting;


    /**
     * @return  Lobby players setting
     */
    public SettingEN getLobbyPlayersSetting()
    {
        return showLobbyPlayers;
    }

    /**
     * @return  Private messages setting
     */
    public SettingEFN getPrivateMessagesSetting()
    {
        return privateMessagesSetting;
    }

    /**
     * @return  Friend requests setting
     */
    public SettingEFFN getFriendRequestsSetting()
    {
        return friendRequestsSetting;
    }

    /**
     * @return  Party requests setting
     */
    public SettingEFFN getPartyRequestsSetting()
    {
        return partyRequestsSetting;
    }

    /**
     * @return  Guild requests setting
     */
    public SettingEFFN getGuildRequestsSetting()
    {
        return guildRequestsSetting;
    }

    /**
     * @param   showLobbyPlayers    Should players in lobby be shown?
     */
    public void setLobbyPlayersSetting(SettingEN showLobbyPlayers)
    {
        this.showLobbyPlayers = showLobbyPlayers;
    }

    /**
     * @param   privateMessagesSetting  Private messages setting
     */
    public void setPrivateMessagesSetting(SettingEFN privateMessagesSetting)
    {
        this.privateMessagesSetting = privateMessagesSetting;
    }

    /**
     * @param   friendRequestsSetting   Friend requests setting
     */
    public void setFriendRequestsSetting(SettingEFFN friendRequestsSetting)
    {
        this.friendRequestsSetting = friendRequestsSetting;
    }

    /**
     * @param   partyRequestsSetting   Party requests setting
     */
    public void setPartyRequestsSetting(SettingEFFN partyRequestsSetting)
    {
        this.partyRequestsSetting = partyRequestsSetting;
    }

    /**
     * @param   guildRequestsSetting   Guild requests setting
     */
    public void setGuildRequestsSetting(SettingEFFN guildRequestsSetting)
    {
        this.guildRequestsSetting = guildRequestsSetting;
    }

}
