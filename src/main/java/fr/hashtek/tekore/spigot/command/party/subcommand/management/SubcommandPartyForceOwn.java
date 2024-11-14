package fr.hashtek.tekore.spigot.command.party.subcommand.management;

import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.common.party.PartyManager;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.command.party.CommandParty;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyForceOwn
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyForceOwn(CommandParty parent)
        throws InvalidCommandContextException
    {
        super(parent, "forceown", "", "party.forceown");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull String[] args
    )
    {
        final PartyManager partyManager = CORE.getPlayerManagersManager()
            .getPlayerManager(player)
            .getAccount()
            .getPartyManager();

        final Party currentParty = partyManager.getCurrentParty();

        if (currentParty == null) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not in a party."));
            return;
        }

        if (currentParty.getOwnerUuid().equals(player.getUniqueId().toString())) {
            player.sendMessage(Component.text(ChatColor.RED + "You are already the leader of your party."));
            return;
        }

        currentParty
            .setOwner(player.getUniqueId().toString())
            .pushData()
            .refreshMembersRamStoredParty(player);

        currentParty.broadcastToMembers(
            player,
            player.getName() + " is the new party owner (forceown)."
        );

        player.sendMessage(Component.text(ChatColor.GREEN + "You force-owned your party."));
    }

}
