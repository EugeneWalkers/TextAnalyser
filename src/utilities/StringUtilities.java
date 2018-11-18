package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StringUtilities {

    private StringUtilities() {

    }

    public static boolean isSubEquals(final String sencence, final String comparing) {
        final int length = sencence.length();

        if (sencence.length() == 0) {
            return false;
        }

        final int l = comparing.length();

        for (int i = 0; i + l <= length; i++) {
            if (comparing.equals(sencence.substring(i, i + l))) {
                return true;
            }
        }

        return false;
    }

    public static List<String> stringToList(final String string) {
        final String[] array = string.split("\\W");
        final List<String> list = new ArrayList<>();
        Collections.addAll(list, array);

        return list;
    }

    public static String listToString(final List<String> list) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i) + ";");
        }

        return builder.toString();
    }

}
