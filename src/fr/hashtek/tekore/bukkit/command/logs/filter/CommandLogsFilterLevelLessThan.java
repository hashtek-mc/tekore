package fr.hashtek.tekore.bukkit.command.logs.filter;

import fr.hashtek.hashlogger.HashLog;
import fr.hashtek.hashlogger.LogLevel;

import java.util.List;
import java.util.stream.Collectors;

public class CommandLogsFilterLevelLessThan
{

    public static List<HashLog> get(List<HashLog> list, String value)
    {
        try {
            LogLevel logLevel = LogLevel.valueOf(value.toUpperCase());

            return list.stream()
                .filter((HashLog hashLog) -> hashLog.getLogLevel().compareTo(logLevel) > 0)
                .collect(Collectors.toList());
        } catch (IllegalArgumentException exception) {
            // ...
        }

        return list;
    }

}
