package fr.hashtek.tekore.common.guild;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import fr.hashtek.tekore.common.guild.rank.GuildRank;

import java.sql.Timestamp;

public class GuildProfile
{

    private GuildRank rank;
    private Timestamp joinedAt;


    /**
     * Creates a new Guild profile.
     */
    public GuildProfile()
    {
        this.joinedAt = new Timestamp(System.currentTimeMillis());
    }


    /**
     * @return  Rank
     */
    @JsonIgnore
    public GuildRank getRank()
    {
        return this.rank;
    }

    /**
     * @return  Rank's UUID
     * @apiNote Solely used for Redis access. Not for public use!
     */
    @JsonGetter("rank")
    public String getRankUuid()
    {
        return this.rank.getUuid();
    }

    /**
     * @return  Profile creation timestamp
     */
    public Timestamp getJoinedAt()
    {
        return this.joinedAt;
    }

    /**
     * @param   parent  Guild where the rank will be fetched from
     * @return  Itself
     */
    public GuildProfile setRank(Guild parent)
    {
        if (this.rank == null) {
            return this;
        }

        return this.setRank(this.getRankUuid(), parent);
    }

    /**
     * @param   rankUuid    Rank UUID
     * @param   parent      Guild where the rank will be fetched from
     * @return  Itself
     */
    public GuildProfile setRank(String rankUuid, Guild parent)
    {
        this.rank = parent.getRank(rankUuid);
        return this;
    }

    /**
     * @param   rank    New rank
     * @return  Itself
     */
    @JsonIgnore
    public GuildProfile setRank(GuildRank rank)
    {
        this.rank = rank;
        return this;
    }

    /**
     * @param   rankUuid    New rank's UUID
     * @return  Itself
     * @apiNote Solely used for Redis access. Not for public use!
     */
    @JsonSetter("rank")
    public GuildProfile setRank(String rankUuid)
    {
        this.rank = new GuildRank(rankUuid);
        return this;
    }

    /**
     * @param   joinedAt    New join timestamp
     * @return  Itself
     */
    public GuildProfile setJoinedAt(Timestamp joinedAt)
    {
        this.joinedAt = joinedAt;
        return this;
    }

}
