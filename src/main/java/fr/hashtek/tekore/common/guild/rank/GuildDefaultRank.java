package fr.hashtek.tekore.common.guild.rank;

import org.bukkit.ChatColor;

import java.util.Set;

public enum GuildDefaultRank
{

    MASTER (new GuildRank(
        "1d5da608-31a9-5f39-a371-897146851cce",
        "master",
        ChatColor.RED + "Master",
        Set.of("*")
    ), true),

    OFFICER (new GuildRank(
        "8602e2fc-597d-4b23-8975-eab27b9aa3f4",
        "officer",
        ChatColor.AQUA + "Officer",
        Set.of(
            "guild.kick",
            "guild.chat.officer"
        )
    )),

    MEMBER (new GuildRank(
        "b634f575-b216-51e4-92ea-414e44e3acaf",
        "member",
        ChatColor.GRAY + "Member",
        Set.of()
    ))
    ;

    private final GuildRank rank;
    private final boolean untouchable;


    GuildDefaultRank(GuildRank rank)
    {
        this(rank, false);
    }

    GuildDefaultRank(GuildRank rank, boolean untouchable)
    {
        this.rank = rank;
        this.untouchable = untouchable;
    }


    /**
     * @return  Rank
     */
    public GuildRank getRank()
    {
        return this.rank;
    }

    /**
     * @return  {@code true} if Rank is "untouchable" (when it cannot be deleted, modified or anything). Otherwise, {@code false}.
     */
    public boolean isUntouchable()
    {
        return this.untouchable;
    }

}
