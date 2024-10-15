package fr.hashtek.tekore.common.regex;

import java.util.regex.Pattern;

public class Regexes
{

    public static final String UUID_REGEX =
        "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";


    /**
     * @param   regex   Regex
     * @param   input   String to test
     * @return  True if input matches the given regex
     */
    public static boolean matches(String regex, String input)
    {
        return Pattern.compile(regex).matcher(input).matches();
    }

}
