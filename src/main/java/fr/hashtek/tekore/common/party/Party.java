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

public class Party {
    private static final Tekore CORE = Tekore.getInstance();
    private String uuid;
    private Set<String> members;
    private Set<String> activeRequests;
    private String owner;
    private Timestamp createdAt;

    public Party() {}

    public Party(String uuid) {
        this.uuid = uuid;
        this.members = new HashSet<>();
        this.activeRequests = new HashSet<>();
    }

    public static Party create(Player owner) {
        String ownerUuid = owner.getUniqueId().toString();
        return new Party(UUID.randomUUID().toString())
                .setMembers(new HashSet<>())
                .setOwner(ownerUuid)
                .setCreatedAt(new Timestamp(System.currentTimeMillis()))
                .addMember(ownerUuid);
    }

    public Party addMember(String playerUuid) {
        this.members.add(playerUuid);
        return this;
    }

    public Party removeMember(String playerUuid) {
        this.members.remove(playerUuid);
        return this;
    }

    public String kickMember(String memberName) throws EntryNotFoundException, PlayerNotInPartyException {
        return kickMember(this.getOwner(), memberName, false);
    }

    public void disband(Player author) {
        for (String memberUuid : this.members) {
            try {
                kickMember(author, memberUuid, true);
            } catch (EntryNotFoundException e) {
                // Log the exception or handle it as needed
                e.printStackTrace();
            } catch (PlayerNotInPartyException e) {
                // Log the exception or handle it as needed
                e.printStackTrace();
            }
        }
        deleteParty();
    }

    public void deleteParty() {
        pushData(this.getUuid(), null);
    }

    public void viewPartyStats(Player player) {
        player.sendMessage("Party UUID: " + this.uuid);
        player.sendMessage("Members: " + this.members.size());
        player.sendMessage("Created At: " + this.createdAt);
    }

    public Party pushData() {
        return pushData(this.getUuid(), this);
    }

    public Party pushData(String uuid, Party data) {
        new PartyPublisher(CORE.getRedisAccess()).push(uuid, data);
        return this;
    }

    @JsonGetter("uuid")
    public String getUuid() {
        return this.uuid;
    }

    @JsonSetter("uuid")
    public Party setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    @JsonGetter("members")
    public Set<String> getMembersUuid() {
        return this.members;
    }

    @JsonSetter("members")
    public Party setMembers(Set<String> members) {
        this.members = members;
        return this;
    }

    @JsonGetter("owner")
    public String getOwnerUuid() {
        return this.owner;
    }

    @JsonSetter("owner")
    public Party setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    @JsonGetter("createdAt")
    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    @JsonSetter("createdAt")
    public Party setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @JsonIgnore
    public Player getOwner() {
        return Bukkit.getPlayer(UUID.fromString(this.owner));
    }
}
