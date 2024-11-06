package fr.hashtek.tekore.spigot.command.party.subcommand.management;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.common.party.PartyManager;
import fr.hashtek.tekore.common.party.io.PartyPublisher;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.spigot.Tekore;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyCreate
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyCreate(AbstractCommand parent)
        throws InvalidCommandContextException
    {
        super(parent, "create", "");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull String[] args
    )
    {
        final PlayerManager playerManager = CORE.getPlayerManagersManager()
            .getPlayerManager(player);

        final Account account = playerManager.getAccount();
        final PartyManager partyManager = account.getPartyManager();

        if (partyManager.getCurrentParty() != null) {
            player.sendMessage(Component.text(ChatColor.RED + "You already are in a party. If you want to create a new one, type /party leave and go ahead."));
            return;
        }

        /* Creating a new Party */
        final Party createdParty = Party.create(player);

        /* Setting player's party to the freshly new created party */
        partyManager.setCurrentParty(createdParty);

        /* Pushing modifications to the Redis access */
        new PartyPublisher(CORE.getRedisAccess()).push(createdParty);
        account.pushData(CORE.getRedisAccess());

        player.sendMessage(Component.text(ChatColor.GREEN + "Party created!"));
    }

}
