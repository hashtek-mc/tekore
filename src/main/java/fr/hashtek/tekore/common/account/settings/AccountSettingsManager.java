package fr.hashtek.tekore.common.account.settings;

import fr.hashtek.tekore.common.account.settings.categories.SettingEFFN;
import fr.hashtek.tekore.common.account.settings.categories.SettingEFN;
import fr.hashtek.tekore.common.account.settings.categories.SettingEN;

public class AccountSettingsManager
{

    private SettingEN showLobbyPlayers;
    private SettingEFN privateMessagesSetting;
    private SettingEFFN friendRequestsSetting;
    private SettingEFN partyRequestsSetting;
    private SettingEFN guildRequestsSetting;


    /**
     * Creates a new account settings manager.
     */
    public AccountSettingsManager()
    {
        this.showLobbyPlayers = SettingEN.EVERYONE;
        this.privateMessagesSetting = SettingEFN.EVERYONE;
        this.friendRequestsSetting = SettingEFFN.EVERYONE;
        this.partyRequestsSetting = SettingEFN.EVERYONE;
        this.guildRequestsSetting = SettingEFN.EVERYONE;
    }


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
    public SettingEFN getPartyRequestsSetting()
    {
        return partyRequestsSetting;
    }

    /**
     * @return  Guild requests setting
     */
    public SettingEFN getGuildRequestsSetting()
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
    public void setPartyRequestsSetting(SettingEFN partyRequestsSetting)
    {
        this.partyRequestsSetting = partyRequestsSetting;
    }

    /**
     * @param   guildRequestsSetting   Guild requests setting
     */
    public void setGuildRequestsSetting(SettingEFN guildRequestsSetting)
    {
        this.guildRequestsSetting = guildRequestsSetting;
    }

}
