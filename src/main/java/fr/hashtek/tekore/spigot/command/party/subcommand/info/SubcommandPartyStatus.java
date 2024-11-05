package fr.hashtek.tekore.spigot.command.party.subcommand.info;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.spigot.Tekore;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyStatus
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyStatus(AbstractCommand parent)
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
            .getParty();

        if (currentParty == null) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not in party. Create one by executing /party create."));
            return;
        }

        player.sendMessage(Component.text("Party \"" + currentParty.getUuid() + "\""));
        player.sendMessage(Component.text("Created at: " + currentParty.getCreatedAt()));
        player.sendMessage(Component.text("Members:"));
        for (String memberUuid : currentParty.getMembersUuid()) {
            player.sendMessage("- " + memberUuid);
        }
        player.sendMessage(Component.text("Owner: " + currentParty.getOwnerUuid()));
    }

}