package fr.hashtek.tekore.common.exceptions;

public class AccountNotFoundException
    extends Exception
{

    /**
     * @param   uuid    Account UUID
     */
    public AccountNotFoundException(String uuid)
    {
        super("Account with uuid \"" + uuid + "\" was not found.");
    }

}
