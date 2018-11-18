package utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static utilities.Constants.SEPARATOR_FOR_LIST;
import static utilities.Constants.SEPARATOR_FOR_TAGS;

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
        final String[] array = string.split(SEPARATOR_FOR_LIST);
        final List<String> list = new ArrayList<>();
        Arrays.sort(array);
        Collections.addAll(list, array);

        return list;
    }

    public static String listToString(final List<String> list) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i)).append(i == (list.size() - 1) ? "" : SEPARATOR_FOR_LIST);
        }

        return builder.toString();
    }

    public static List<TagCountData> tagStringToList(final String string){
        final String[] array = string.split(SEPARATOR_FOR_LIST);
        Arrays.sort(array);

        final List<TagCountData> result = new ArrayList<>();

        for (final String element: array){
            final String[] temp = element.split(SEPARATOR_FOR_TAGS);

            result.add(new TagCountData(temp[0], Integer.parseInt(temp[1])));
        }

        return result;
    }

    public static String tagListToString(final List<TagCountData> list){
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).toString()).append(i == (list.size() - 1) ? "" : SEPARATOR_FOR_LIST);
        }

        return builder.toString();
    }

    public static String tagListToStringWithoutNumb(final List<TagCountData> list){
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).getName()).append(i == (list.size() - 1) ? "" : SEPARATOR_FOR_LIST);
        }

        return builder.toString();
    }

    public static List<String> tagListToStringListByName(final List<TagCountData> list){
        final List<String> result = new ArrayList<>();

        for (final TagCountData tag: list){
            result.add(tag.getName());
        }

        return result;
    }

}
