package fr.hashtek.tekore.common.guild;

public class GuildSettings
{

    private String defaultRankUuid;


    /**
     * @return  Default rank UUID to set for new members
     */
    public String getDefaultRankUuid()
    {
        return this.defaultRankUuid;
    }

    /**
     * @param   defaultRankUuid New default rank UUID to set for new members
     * @return  Itself
     */
    public GuildSettings setDefaultRankUuid(String defaultRankUuid)
    {
        this.defaultRankUuid = defaultRankUuid;
        return this;
    }

}
