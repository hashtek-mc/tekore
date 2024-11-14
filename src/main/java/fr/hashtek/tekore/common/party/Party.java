package fr.hashtek.tekore.common.party;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.constant.Constants;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.exception.PlayerNotInPartyException;
import fr.hashtek.tekore.common.party.io.PartyPublisher;
import fr.hashtek.tekore.spigot.Tekore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Party
{

    private static final Tekore CORE = Tekore.getInstance();


    private String uuid;
    private Set<String> members;
    private Set<String> activeRequests;
    private String owner;
    private Timestamp createdAt;


    /**
     * Creates an empty Party.
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Party() {}

    /**
     * Creates an empty Party, just with the UUID.
     * <br>
     * Basically only used at the very first state of
     * an {@link Account}, during its creation.
     *
     * @param   uuid    Party's UUID
     * @apiNote Solely used for Redis access. Not for public use!
     */
    public Party(String uuid)
    {
        this.uuid = uuid;
    }

    /**
     * Creates a new Party.
     *
     * @param   owner   Party owner
     */
    public static Party create(Player owner)
    {
        final String ownerUuid = owner.getUniqueId().toString();

        return new Party(UUID.randomUUID().toString())
            .setMembers(new HashSet<String>())
            .setOwner(ownerUuid)
            .setCreatedAt(new Timestamp(System.currentTimeMillis()))
            .addMember(ownerUuid);
    }


    /**
     * Broadcasts a message to the party's members.
     *
     * @param   author  Broadcast author
     * @param   message Message to broadcast
     */
    public void broadcastToMembers(Player author, String message)
    {
        for (String memberUuid : this.members) {
            try {
                final String memberName = new AccountProvider(CORE.getRedisAccess())
                    .getUsernameFromUuid(memberUuid);

                CORE.getMessenger().sendPluginMessage(
                    author,
                    "Message",
                    memberName,
                    message
                );
            }
            catch (EntryNotFoundException exception) {
                // osef mdrrr
            }
        }
    }

    /**
     * Adds a member to the party.
     *
     * @param   playerUuid  Member to add's UUID
     * @return  Itself
     */
    public Party addMember(String playerUuid)
    {
        this.members.add(playerUuid);
        return this;
    }

    /**
     * Removes a member from the party.
     *
     * @param   memberUuid  Member to remove's UUID
     * @return  Itself
     */
    public Party removeMember(String memberUuid)
    {
        this.members.remove(memberUuid);
        return this;
    }

    /**
     * @see #kickMember(Player, String, boolean)
     */
    public String kickMember(String memberName)
        throws EntryNotFoundException, PlayerNotInPartyException
    {
        return this.kickMember(this.getOwner(), memberName, false);
    }

    /**
     * <p>
     * Kicks a member from the Party.
     * <p>
     * Full self-sufficient, everything is done:
     * <ul>
     *     <li>Updating party informations</li>
     *     <li>Updating RAM-stored members accounts party informations</li>
     *     <li>Sending a message to the kicked member to notify him from the kick.</li>
     * </ul>
     *
     * @param   author      Player who kicked the member
     * @param   memberTag   Member to kick (tag = either a username or a UUID)
     * @param   disbandMode If kick provides from a party disband.<br>
     *                      Basically just modifies the message sent to the targeted member.<br>
     *                      If set to true, memberTag MUST be a UUID.
     * @return  Kicked member's username
     * @throws  EntryNotFoundException      If targeted member doesn't exist or never connected to the server
     * @throws  PlayerNotInPartyException   If targeted member isn't in the party
     */
    public String kickMember(
        Player author,
        String memberTag,
        boolean disbandMode
    )
        throws EntryNotFoundException, PlayerNotInPartyException
    {
        final AccountProvider accountProvider = new AccountProvider(CORE.getRedisAccess());

        final Account memberAccount = accountProvider.get(
            disbandMode
                ? memberTag
                : accountProvider.getUuidFromUsername(memberTag)
        );
        final String memberUsername = memberAccount.getUsername();
        final String memberUuid = memberAccount.getUuid();

        if (!this.members.contains(memberUuid)) {
            throw new PlayerNotInPartyException(memberUsername);
        }

        if (!disbandMode) {
            /* Here we remove the member from the party... */
            this.members.remove(memberUuid);

            /* ...we push the modifications to the Redis database... */
            this.pushData();
        }

        /**
         * ...we ask BungeeCord to update the RAM-stored removed member's account to take into account the modifications...
         * NOTE: The party UUID stored in the account of the kicked member will automatically be deleted.
         */
        CORE.getMessenger().sendPluginMessage(
            author,
            Constants.UPDATE_PARTY_SUBCHANNEL,
            memberUsername
        );

        /* ...and we send him a message (only for players that are not the disband author). */
        if (!author.getUniqueId().toString().equals(memberAccount.getUuid())) {
            CORE.getMessenger().sendPluginMessage(
                author,
                "Message",
                memberUsername,
                disbandMode
                    ? ChatColor.RED + "The party you were in has been disbanded."
                    : ChatColor.RED + "You've been kicked from the party."
            );
        }

        return memberAccount.getUsername();
    }

    /**
     * @see     Party#disband(Player)
     * @apiNote Must be called when the party owner is online.<br>
     *          Otherwise, an error will be thrown during runtime at some point.
     */
    public void disband()
    {
        this.disband(this.getOwner()); // TODO: Maybe check if owner is online?
    }

    /**
     * Disbands the party.
     *
     * @param   author  Disband author
     */
    public void disband(Player author)
    {
        /* Kicking everyone from the party */
        for (String memberUuid : this.members) {
            try {
                this.kickMember(author, memberUuid, true);
            }
            catch (EntryNotFoundException | PlayerNotInPartyException exception) {
                /**
                 * @OccursWhen  Targeted member's account has been inappropriately terminated.
                 * ... but we can pretty much don't care about this as the party is getting
                 * disbanded.
                 */
            }
        }

        /* Deleting the party. */
        this.deleteParty();
    }

    /**
     * Removes this party from the Redis database.
     */
    public void deleteParty()
    {
        this.pushData(this.getUuid(), null);
    }

    /**
     * Sends a PartyUpdate signal to every member of this party.
     * <p>
     * Used for synchronization.
     */
    public void refreshMembersRamStoredParty(Player author)
    {
        for (String memberUuid : this.members) {
            CORE.getMessenger().sendPluginMessage(
                author,
                Constants.UPDATE_PARTY_SUBCHANNEL,
                memberUuid
            );
        }
    }

    /**
     * Pushes the party's data to the Redis database.
     */
    public Party pushData()
    {
        return this.pushData(this.getUuid(), this);
    }

    /**
     * Pushes the party's data to the Redis database.
     * <p>
     * For basic push, please use {@link #pushData()}.
     *
     * @param   uuid    Party's UUID
     * @param   data    Data to push
     */
    public Party pushData(String uuid, Party data)
    {
        new PartyPublisher(CORE.getRedisAccess()).push(uuid, data);
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
     * @return  Party's active requests
     */
    @JsonIgnore
    public Set<String> getActiveRequests()
    {
        /* Lazy loading */
        if (this.activeRequests == null) {
            this.activeRequests = new HashSet<String>();
        }
        return this.activeRequests;
    }

    /**
     * Same as {@link #getActiveRequests()} but without lazy loading.
     * @apiNote Solely used for Redis access. Not for public use, unless you know what you're doing!
     */
    @JsonGetter("activeRequests")
    public Set<String> getRawActiveRequests()
    {
        if (this.activeRequests == null) {
            return null;
        }
        return this.activeRequests;
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
    public Party setUuid(String uuid)
    {
        this.uuid = uuid;
        return this;
    }

    /**
     * @param   members New party members
     */
    @JsonSetter("members")
    public Party setMembers(Set<String> members)
    {
        this.members = members;
        return this;
    }

    /**
     * @param   activeRequests  New active requests
     * @return  Itself
     */
    @JsonSetter("activeRequests")
    public Party setActiveRequests(Set<String> activeRequests)
    {
        this.activeRequests = activeRequests;
        return this;
    }

    /**
     * @param   owner   New party owner
     */
    @JsonSetter("owner")
    public Party setOwner(String owner)
    {
        this.owner = owner;
        return this;
    }

    /**
     * @param   createdAt   Party creation date
     */
    public Party setCreatedAt(Timestamp createdAt)
    {
        this.createdAt = createdAt;
        return this;
    }

}
