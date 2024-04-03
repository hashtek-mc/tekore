package fr.hashtek.tekore.bukkit.command.logs.filter;

import fr.hashtek.hashlogger.HashLog;

import java.util.List;
import java.util.stream.Collectors;

public class CommandLogsFilterContains
{

    public static List<HashLog> get(List<HashLog> list, String value)
    {
        return list.stream()
            .filter((HashLog hashLog) -> hashLog.getLog().contains(value))
            .collect(Collectors.toList());
    }

}
