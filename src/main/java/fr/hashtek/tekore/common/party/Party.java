package fr.hashtek.tekore.common.party;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import fr.hashtek.tekore.common.account.Account;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Party
{

    private String uuid;
    private Set<String> members;
    private String owner;
    private Timestamp createdAt;


    /**
     * Creates an empty Party.
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Party() {}

    /**
     * Creates an empty Party, just with the UUID.
     * </br>
     * Basically only used at the very first state of
     * an {@link Account}, during its creation.
     *
     * @param   uuid    Party's UUID
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Party(String uuid)
    {
        this.uuid = uuid;
        this.members = null;
        this.owner = null;
        this.createdAt = null;
    }

    /**
     * Creates a new Party.
     * <p>
     * This "replaces" a static {@code create} function.
     * </p>
     *
     * @param   owner   Party owner
     */
    public Party(Player owner)
    {
        this.uuid = UUID.randomUUID().toString();
        this.members = new HashSet<String>();
        this.owner = owner.getUniqueId().toString();
        this.createdAt = new Timestamp(System.currentTimeMillis());

        this.addMember(this.owner);
    }


    /**
     * Adds a new player to the party.
     *
     * @param   playerUuid  Player to add
     * @return  Itself
     */
    public Party addMember(String playerUuid)
    {
        this.members.add(playerUuid);
        return this;
    }

    /**
     * Removes a player from the party.
     *
     * @param   playerUuid  Player to remove
     * @return  Itself
     */
    public Party removeMember(String playerUuid)
    {
        this.members.remove(playerUuid);
        return this;
    }

    /**
     * @return  UUID
     */
    public String getUuid()
    {
        return this.uuid;
    }

    /**
     * @return  Party members UUIDs
     */
    @JsonGetter("members")
    public Set<String> getMembersUuid()
    {
        return this.members;
    }

    /**
     * @return  Party members
     */
    @JsonIgnore
    public Set<Player> getMembers()
    {
        final Set<Player> members = new HashSet<Player>();

        for (String memberUuid : this.members) {
            final Player p = Bukkit.getPlayer(UUID.fromString(memberUuid));

            if (p == null) {
                continue;
            }
            members.add(p);
        }

        return members;
    }

    /**
     * @return  Party owner / leader UUID
     */
    @JsonGetter("owner")
    public String getOwnerUuid()
    {
        return this.owner;
    }

    /**
     * @return  Party owner / leader
     */
    @JsonIgnore
    public Player getOwner()
    {
        return Bukkit.getPlayer(UUID.fromString(this.owner));
    }

    /**
     * @return  Party creation date
     */
    public Timestamp getCreatedAt()
    {
        return this.createdAt;
    }

    /**
     * @param   uuid    New UUID
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    /**
     * @param   members New party members
     */
    @JsonSetter("members")
    public void setMembers(Set<String> members)
    {
        this.members = members;
    }

    /**
     * @param   owner   New party owner
     */
    @JsonSetter("owner")
    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    /**
     * @param   createdAt   Party creation date
     */
    public void setCreatedAt(Timestamp createdAt)
    {
        this.createdAt = createdAt;
    }

}
