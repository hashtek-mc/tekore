package fr.hashtek.tekore.spigot.command.party.subcommand.management;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;
import fr.hashtek.tekore.common.exception.InvalidCommandContextException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.common.party.PartyManager;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.spigot.Tekore;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubcommandPartyLeave
    extends AbstractSubcommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public SubcommandPartyLeave(AbstractCommand parent)
        throws InvalidCommandContextException
    {
        super(parent, "leave", "");
    }


    /**
     * Flag handling.
     *
     * @param   arg     Argument passed
     * @param   player  Player who executed the command
     * @return  {@code true} if code can continue (if flag executed successfully). Otherwise, {@code false}.
     */
    public boolean handleFlags(String arg, Player player)
    {
        if (!arg.startsWith("-")) {
            return false;
        }

        switch (arg) {
            /**
             * Flag: -f; --force
             *
             * If player is owner, automatically give the ownership
             * to a random member in the party to let the player leave.
             */
            case "-f":
            case "--force":
                player.performCommand("party promote --auto");
                return true;

            default:
                player.sendMessage(Component.text(ChatColor.RED + "Unknown flag. Executing basic leave..."));
                player.performCommand("party leave");
                return false;
        }
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
        final Party currentParty = partyManager.getCurrentParty();

        if (currentParty == null) {
            player.sendMessage(Component.text(ChatColor.RED + "You are not in a party."));
            return;
        }

        if (currentParty.getOwnerUuid().equals(player.getUniqueId().toString())) {
            if (currentParty.getMembers().size() == 1) {
                player.performCommand("party disband");
                return;
            }

            /* Flag handling */
            if (args.length == 0) {
                player.sendMessage(Component.text(
                    ChatColor.RED + "As a party leader, you must give the ownership of the party to someone before leaving.\n" +
                    "To automatically make that, execute /party leave --force (or -f)"
                ));
                return;
            }

            if (!this.handleFlags(args[0], player)) {
                return;
            }
        }

        account.getPartyManager().setCurrentParty(null);
        account.pushData(CORE.getRedisAccess());
        currentParty.removeMember(account.getUuid());
        currentParty.pushData();
        currentParty.refreshMembersRamStoredParty(player);
    }

}
