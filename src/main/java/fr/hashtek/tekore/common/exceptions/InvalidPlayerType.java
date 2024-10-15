package fr.hashtek.tekore.common.exceptions;

public class InvalidPlayerType
    extends Exception
{

    /**
     * @param   player  Player object
     */
    public InvalidPlayerType(Object player)
    {
        super("Invalid player type. (" + player + "): Neither Bukkit nor Bungee.");
    }

}
