package fr.hashtek.tekore.spigot.command.party.subcommand.info;

import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.spigot.command.party.CommandParty;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyStatus
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyStatus(CommandParty parent)
        throws InvalidCommandContextException
    {
        super(parent, "status", "");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull String[] args
    )
    {
        final Party currentParty = CORE.getPlayerManagersManager()
            .getPlayerManager(player)
            .getAccount()
            .getPartyManager()
            .getCurrentParty();

        if (currentParty == null) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not in a party. Create one by executing /party create."));
            return;
        }

        player.sendMessage(Component.text("Party \"" + currentParty.getUuid() + "\""));
        player.sendMessage(Component.text("Created at: " + currentParty.getCreatedAt()));
        player.sendMessage(Component.text("Members:"));
        for (String memberUuid : currentParty.getMembersUuid()) {
            player.sendMessage("- " + memberUuid);
        }
        if (currentParty.getActiveRequests().isEmpty()) {
            player.sendMessage(Component.text("No active requests."));
        } else {
            player.sendMessage(Component.text("Active requests:"));
            for (String request : currentParty.getActiveRequests()) {
                player.sendMessage("- " + request);
            }
        }
        player.sendMessage(Component.text("Party owner: " + currentParty.getOwnerUuid()));
    }

}
