package fr.hashtek.tekore.bukkit.command.logs;

import fr.hashtek.hashdate.HashDate;
import fr.hashtek.hashdate.HashDateType;
import fr.hashtek.hashlogger.HashLog;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.hashlogger.LogLevel;
import fr.hashtek.tekore.bukkit.Tekore;
import fr.hashtek.tekore.bukkit.command.logs.filter.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandLogs implements CommandExecutor, HashLoggable
{

    private final HashLogger logger;


    /**
     * Creates a new instance of CommandLogs
     *
     * @param   core    Tekore instance
     */
    public CommandLogs(Tekore core)
    {
        this.logger = core.getHashLogger();
    }


    /**
     * Command parsing.
     *
     * @param	sender	Player who executed the command
     * @param	args	Arguments passed
     */
    private boolean parseInput(CommandSender sender, String[] args)
    {
        if (args.length % 2 != 0) {
            logger.info(this, "Invalid usage.");
            sender.sendMessage(ChatColor.RED + "Syntaxe: /logs [<category>] [<find>]");
            return false;
        }
        return true;
    }

    private List<HashLog> filterHistory(String[] args)
    {
        List<HashLog> logHistory = new ArrayList<HashLog>(this.logger.getHistory());

        for (int k = 0; k < args.length; k += 2) {
            String category = args[k];
            String value = args[k + 1];

            switch (category) {
                case "level":
                    logHistory = CommandLogsFilterLevel.get(logHistory, value);
                    break;
                case "levelMoreThan":
                    logHistory = CommandLogsFilterLevelMoreThan.get(logHistory, value);
                    break;
                case "levelLessThan":
                    logHistory = CommandLogsFilterLevelLessThan.get(logHistory, value);
                    break;
                case "levelMoreEqualThan":
                    logHistory = CommandLogsFilterLevelMoreEqualThan.get(logHistory, value);
                    break;
                case "levelLessEqualThan":
                    logHistory = CommandLogsFilterLevelLessEqualThan.get(logHistory, value);
                    break;
                case "contains":
                    logHistory = CommandLogsFilterContains.get(logHistory, value);
                    break;
                case "before":
                    logHistory = CommandLogsFilterBefore.get(logHistory, value);
                    break;
                case "after":
                    logHistory = CommandLogsFilterAfter.get(logHistory, value);
                    break;
                case "from":
                    logHistory = CommandLogsFilterFrom.get(logHistory, value);
                    break;
                default:
                    break;
            }
        }

        return logHistory;
    }

    /**
     * Displays the log history to the sender.
     *
     * @param   sender  Player who executed the command
     * @param   logs    Log history
     */
    private void displayLogs(CommandSender sender, List<HashLog> logs)
    {
        for (HashLog log : logs) {
            String line = String.format(
                "\n&7&o[%s: %s.java] %s\n&r%s<%s>%s %s",
                log.getInstance().getClass().getSimpleName(),
                log.getAuthor().getClass().getSimpleName(),
                HashDate.format(HashDateType.SHORT_DATE_TIME, log.getCreatedAt()),
                log.getLogLevel().getMinecraftColor(),
                log.getLogLevel().getFullName(),
                LogLevel.INFO.getMinecraftColor(),
                log.getLog()
            );

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
    }

    /**
     * Called when command is executed.
     *
     * @param	sender	    Player who executed the command
     * @param   command     Command
     * @param   label       Command
     * @param	args	    Arguments passed
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!parseInput(sender, args))
            return true;

        if (args.length == 0) {
            this.displayLogs(sender, this.logger.getHistory());
            return true;
        }

        final List<HashLog> filteredLogHistory = this.filterHistory(args);

        if (filteredLogHistory == null) {
            logger.info(this, "Invalid usage.");
            sender.sendMessage(ChatColor.RED + "Syntaxe: /logs [<category>] [<find>]");
            return true;
        }

        this.displayLogs(sender, filteredLogHistory);

        return true;
    }
}
