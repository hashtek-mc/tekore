package fr.hashtek.tekore.spigot.commands.debug;

import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.exceptions.EntryNotFoundException;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.common.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandAccountDump
    extends AbstractCommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public CommandAccountDump()
    {
        super("tekore.debug");
    }


    private void dumpAccount(CommandSender sender, Account account)
    {
        String output =
            ChatColor.GOLD + "Account dump for UUID \"" + account.getUuid() + "\"\n" + ChatColor.RESET +
            "UUID: " + account.getUuid() + "\n" +
            "Username: " + account.getUsername() + "\n" +
            "Created at: " + account.getCreatedAt() + "\n" +
            "Last update: " + account.getLastUpdate() + "\n" +
            "Rank: " + account.getRank().getName() + " (" + account.getRank().getUuid() + ")\n" +
            "Coins: " + account.getCoins() + "\n" +
            "HashCoins: " + account.getHashCoins() + "\n";

        sender.sendMessage(output);
    }

    @Override
    public void execute(
        @NotNull Player player,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if (args.length != 1) {
            player.sendMessage("Wrong syntax");
            return;
        }

        final String targetUuid = args[0];

        if (!Regexes.matches(Regexes.UUID_REGEX, targetUuid)) {
            player.sendMessage("Wrong UUID");
            return;
        }

        final PlayerManager targetPlayerManager =
            CORE.getPlayerManagersManager().getPlayerManager(targetUuid);

        if (targetPlayerManager != null) {
            player.sendMessage("(From RAM)");
            this.dumpAccount(player, targetPlayerManager.getAccount());
            return;
        }

        try {
            final Account account = new AccountProvider(CORE.getRedisAccess())
                .get(targetUuid);

            player.sendMessage("(Directly from Redis)");
            this.dumpAccount(player, account);
        }
        catch (EntryNotFoundException exception) {
            player.sendMessage(exception.getMessage());
        }
    }
}
