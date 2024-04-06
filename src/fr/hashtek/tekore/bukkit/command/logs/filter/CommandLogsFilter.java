package fr.hashtek.tekore.bukkit.command.logs.filter;

import fr.hashtek.hashlogger.HashLog;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Logs filter utility.
 */
public class CommandLogsFilter
{

    private final List<HashLog> logs;
    private final Predicate<HashLog> filter;


    /**
     * Creates a new instance of CommandLogsFilter.
     *
     * @param   logs    List of logs
     * @param   filter  Filter rule
     */
    public CommandLogsFilter(List<HashLog> logs, Predicate<HashLog> filter)
    {
        this.logs = logs;
        this.filter = filter;
    }


    /**
     * Filters the list of logs following a given rule.
     *
     * @return  Filtered log list
     */
    public List<HashLog> get()
    {
        return this.logs.stream()
            .filter(filter)
            .collect(Collectors.toList());
    }

}
