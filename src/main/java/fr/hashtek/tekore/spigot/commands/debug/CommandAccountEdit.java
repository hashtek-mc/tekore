package fr.hashtek.tekore.spigot.commands.debug;

import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.common.regex.Regexes;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.common.commands.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CommandAccountEdit
    extends AbstractCommand
{

    private static final Tekore CORE = Tekore.getInstance();


    public CommandAccountEdit()
    {
        super("tekore.debug");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if (args.length != 3) {
            player.sendMessage("Wrong syntax.");
            return;
        }

        final List<String> keys = Arrays.asList(
            "coins",
            "hashcoins"
        );

        final String targetUuid = args[0];
        final String keyToEdit = args[1];
        final String valueToPut = args[2];

        if (!Regexes.matches(Regexes.UUID_REGEX, targetUuid)) {
            player.sendMessage("Invalid UUID.");
            return;
        }

        if (!keys.contains(keyToEdit)) {
            player.sendMessage("Invalid key.");
            return;
        }

        final PlayerManager targetPlayerManager =
            CORE.getPlayerManagersManager().getPlayerManager(targetUuid);

        if (targetPlayerManager == null) {
            player.sendMessage("Player not found.");
            return;
        }

        try {
            final int parsedValue = Integer.parseInt(valueToPut);

            switch (keyToEdit) {
                case "coins":
                    targetPlayerManager.getAccount().setCoins(parsedValue);
                    break;
                case "hashcoins":
                    targetPlayerManager.getAccount().setHashCoins(parsedValue);
                    break;
                default:
                    player.sendMessage("???");
            }
        }
        catch (NumberFormatException exception) {
            player.sendMessage("Invalid value.");
        }

        player.sendMessage("Command executed successfully.");
    }
}
