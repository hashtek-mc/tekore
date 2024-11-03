package fr.hashtek.tekore.spigot.command.party.subcommand.management;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.spigot.Tekore;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyDisband
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyDisband(AbstractCommand parent)
        throws InvalidCommandContextException
    {
        super(parent, "disband", "");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull String[] args
    )
    {
        final PlayerManager playerManager = CORE.getPlayerManagersManager()
            .getPlayerManager(player);

        final Account playerAccount = playerManager.getAccount();
        final Party playerParty = playerAccount.getParty();

        if (playerParty == null) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not in a party."));
            return;
        }

        if (!playerParty.getOwner().equals(player)) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not the owner of this party. Type /party leave to leave."));
            return;
        }
    }

}
