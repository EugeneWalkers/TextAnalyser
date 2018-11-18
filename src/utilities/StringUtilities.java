package utilities;

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

}
