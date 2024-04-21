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

    /**
     * Dispatches the log history filter.
     *
     * @param   logHistory  List of logs
     * @param   category    Filter category
     * @param   value       Filter value
     * @return  Filtered log history
     */
    private List<HashLog> dispatchFilter(List<HashLog> logHistory, String category, String value)
    {
        final LogLevel logLevel;

        if (category.startsWith("level")) {
            try {
                logLevel = LogLevel.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException exception) {
                // TODO: Send error.
                return logHistory;
            }
        } else
            logLevel = LogLevel.INFO;

        switch (category) {
            case "level":
                logHistory = new CommandLogsFilter(logHistory,
                    hashLog -> hashLog.getLogLevel().compareTo(logLevel) == 0
                ).get();
                break;
            case "levelMoreThan":
                logHistory = new CommandLogsFilter(logHistory,
                    hashLog -> hashLog.getLogLevel().compareTo(logLevel) < 0
                ).get();
                break;
            case "levelLessThan":
                logHistory = new CommandLogsFilter(logHistory,
                    hashLog -> hashLog.getLogLevel().compareTo(logLevel) > 0
                ).get();
                break;
            case "levelMoreEqualThan":
                logHistory = new CommandLogsFilter(logHistory,
                    hashLog -> hashLog.getLogLevel().compareTo(logLevel) <= 0
                ).get();
                break;
            case "levelLessEqualThan":
                logHistory = new CommandLogsFilter(logHistory,
                    hashLog -> hashLog.getLogLevel().compareTo(logLevel) >= 0
                ).get();
                break;
            case "contains":
                logHistory = new CommandLogsFilter(logHistory,
                    hashLog -> hashLog.getLog().toLowerCase().contains(value.toLowerCase())
                ).get();
                break;
            case "before":
                break;
            case "after":
                break;
            case "from":
                logHistory = new CommandLogsFilter(logHistory,
                    hashLog -> hashLog.getAuthor().getClass().getSimpleName().equalsIgnoreCase(value)
                ).get();
                break;
            default:
                break;
        }

        return logHistory;
    }

    /**
     * Filters log history.
     *
     * @param   args    Passed arguments
     * @return  Filtered log history
     */
    private List<HashLog> filterHistory(String[] args)
    {
        List<HashLog> logHistory = new ArrayList<HashLog>(this.logger.getHistory());

        for (int k = 0; k < args.length; k += 2) {
            String category = args[k];
            String value = args[k + 1];

            logHistory = this.dispatchFilter(logHistory, category, value);
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
                "\n&7[%s: %s.java] &8&o%s\n&r%s<%s>%s %s",
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
     * Displays the logger status to the sender.
     *
     * @param   sender  Player who executed the command
     */
    private void displayLoggerStatus(CommandSender sender)
    {
        /*List<String> output = new ArrayList<String>();

        output.add("HashLogger status");
        output.add("  ● " + 1 + " instances created");
        String[] test = {
            "HashLogger status",
            String.format("  - %d instances created", 1),
            "",
        };

        sender.sendMessage(output.toString());*/

        sender.sendMessage(ChatColor.RED + "Soon...");
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
        if (args.length == 0) {
            this.displayLogs(sender, this.logger.getHistory());
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("status")) {
            this.displayLoggerStatus(sender);
            return true;
        }

        if (!parseInput(sender, args))
            return true;

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
