package fr.hashtek.tekore.common.exception;

import fr.hashtek.tekore.common.command.AbstractCommand;
import fr.hashtek.tekore.common.command.subcommand.AbstractSubcommand;

public class InvalidCommandContextException
    extends Exception
{

    public InvalidCommandContextException(
        AbstractCommand parent,
        AbstractSubcommand child
    )
    {
        super(""); // TODO: Write error here pls lol
    }

}
