package fr.hashtek.tekore.common.exception;

public class PlayerNotInPartyException
    extends Exception
{

    public PlayerNotInPartyException(String playerTag)
    {
        super("Player with tag \"" + playerTag + "\" is not in the party.");
    }

}
