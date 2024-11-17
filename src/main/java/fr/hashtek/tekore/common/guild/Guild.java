package fr.hashtek.tekore.common.guild;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.guild.rank.GuildDefaultRank;
import fr.hashtek.tekore.common.guild.rank.GuildRank;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.*;

public class Guild
{

    private String uuid;
    private String displayName;
    private String description; // MOTD
    private Map<String, GuildProfile> members;
    private String ownerUuid;
    private Set<GuildRank> ranks;
    private Timestamp createdAt;
    private GuildSettings settings;
    // TODO: XP system


    /**
     * Creates an empty Guild.
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Guild()
    {
        this(null);
    }

    /**
     * Creates an empty Guild, just with the UUID.
     * <br>
     * Basically only used at the very first state of
     * an {@link Account}, during its creation.
     *
     * @param   uuid    Guild's UUID
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Guild(String uuid)
    {
        this.uuid = uuid;
        this.members = new HashMap<String, GuildProfile>();
        this.ranks = new HashSet<GuildRank>();
        this.settings = new GuildSettings();
    }


    /**
     * Creates a new Guild.
     *
     * @param   owner   Guild owner
     * @return  Freshly created Guild
     */
    public static Guild create(Player owner)
    {
        final String ownerUuid = owner.getUniqueId().toString();

        final Guild guild = new Guild(UUID.randomUUID().toString())
            .setOwnerUuid(ownerUuid)
            .setCreatedAt(new Timestamp(System.currentTimeMillis()));

        for (GuildDefaultRank rank : GuildDefaultRank.values()) {
            guild.addRank(rank.getRank());
        }

        guild.getSettings().setDefaultRankUuid(GuildDefaultRank.MEMBER.getRank().getUuid());

        final GuildProfile ownerProfile = new GuildProfile();
        ownerProfile.setRank(GuildDefaultRank.MASTER.getRank().getUuid());

        guild.addMember(ownerUuid, ownerProfile);
        return guild;
    }

    /**
     * Adds a member to the Guild.
     *
     * @param   memberUuid  UUID of the member to add
     * @return  Itself
     */
    public Guild addMember(String memberUuid)
    {
        return this.addMember(
            memberUuid,
            new GuildProfile()
                .setRank(this.settings.getDefaultRankUuid())
        );
    }

    /**
     * Adds a member to the Guild, with a specific associated
     * Guild profile.
     *
     * @param   memberUuid  UUID of the member to add
     * @param   profile     Profile to associate to the member
     * @return  Itself
     */
    public Guild addMember(String memberUuid, GuildProfile profile)
    {
        profile.setRank(this);
        this.members.put(memberUuid, profile);
        return this;
    }

    // TODO: remove member

    /**
     * Adds a rank to the Guild.
     *
     * @param   rank    Rank to add
     * @return  Itself
     */
    public Guild addRank(GuildRank rank)
    {
        this.ranks.add(rank);
        return this;
    }

    /**
     * Removes a rank from the Guild.
     *
     * @param   rankUuid    UUID of the rank to remove
     * @return  Itself
     */
    public Guild removeRank(String rankUuid)
    {
        this.ranks.removeIf(rank -> rank.getUuid().equals(rankUuid));
        return this;
    }


    /**
     * @return  Guild's UUID
     */
    public String getUuid()
    {
        return this.uuid;
    }

    /**
     * @return  Guild's display name
     */
    public String getDisplayName()
    {
        return this.displayName;
    }

    /**
     * @return  Guild's description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * @param   memberUuid  UUID of the target
     * @return  If found, associated Guild profile. Otherwise, {@code null}.
     */
    public GuildProfile getMemberProfile(String memberUuid)
    {
        return this.members.get(memberUuid)
            .setRank(this);
    }

    /**
     * @return  Every guild member's profile
     */
    public Map<String, GuildProfile> getMembersProfile()
    {
        return this.members;
    }

    /**
     * @return  Guild owner's UUID
     */
    public String getOwnerUuid()
    {
        return this.ownerUuid;
    }

    /**
     * @param   rankUuid    UUID of the rank to get
     * @return  If found, fetched rank. Otherwise, {@code null}.
     */
    public GuildRank getRank(String rankUuid)
    {
        return this.ranks.stream().filter(r -> r.getUuid().equals(rankUuid)).findFirst().orElse(null);
    }

    /**
     * @return  Guild's ranks
     */
    public Set<GuildRank> getRanks()
    {
        return this.ranks;
    }

    /**
     * @return  Guild creation timestamp
     */
    public Timestamp getCreatedAt()
    {
        return this.createdAt;
    }

    /**
     * @return  Guild's settings
     */
    public GuildSettings getSettings()
    {
        return this.settings;
    }

    /**
     * @param   uuid    New UUID
     * @return  Itself
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Guild setUuid(String uuid)
    {
        this.uuid = uuid;
        return this;
    }

    /**
     * @param   displayName New display name
     * @return  Itself
     */
    public Guild setDisplayName(String displayName)
    {
        this.displayName = displayName;
        return this;
    }

    /**
     * @param   description New description
     * @return  Itself
     */
    public Guild setDescription(String description)
    {
        this.description = description;
        return this;
    }

    /**
     * @param   members New members
     * @return  Itself
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Guild setMembers(Map<String, GuildProfile> members)
    {
        this.members = members;
        return this;
    }

    /**
     * @param   ownerUuid   New owner's UUID
     * @return  Itself
     */
    public Guild setOwnerUuid(String ownerUuid)
    {
        this.ownerUuid = ownerUuid;
        return this;
    }

    /**
     * @param   ranks   New ranks
     * @return  Itself
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Guild setRanks(Set<GuildRank> ranks)
    {
        this.ranks = ranks;
        return this;
    }

    /**
     * @param   createdAt   New creation timestamp
     * @return  Itself
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Guild setCreatedAt(Timestamp createdAt)
    {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @param   settings    New settings
     * @return  Itself
     * @apiNote Solely used for Redis access. Not for public use!<br>
     *          Please edit the settings directly in the existing {@link GuildSettings} instance stored here.
     */
    public Guild setSettings(GuildSettings settings)
    {
        this.settings = settings;
        return this;
    }

}
