package fr.hashtek.tekore.common.exception;

public class EntryNotFoundException
    extends Exception
{

    /**
     * @param   key     Key
     */
    public EntryNotFoundException(String key)
    {
        super("Entry with key \"" + key + "\" was not found.");
    }

}
