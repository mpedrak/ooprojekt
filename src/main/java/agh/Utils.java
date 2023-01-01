package agh;

import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class Utils {

    static boolean isInputInteger (String input) {
        if (input.isEmpty())
            return false;

        try {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    static String getOrThrow (String key, Map<String, String> input) {

        if (input.get(key) == null)
            throw new NoSuchElementException(key);

        return input.get(key);
    }

    static boolean parseBoolean (String value) {
        if (value.equals("false"))
            return false;
        if (value.equals("true"))
            return true;

        throw new NumberFormatException();
    }

    static String arrayToOneString (int[] tab) {
        StringBuilder output= new StringBuilder();

        for (int x: tab)
            output.append(x);

        return output.toString();
    }

}
