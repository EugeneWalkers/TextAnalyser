package utilities;

import java.util.Vector;

public class Constants {
    public final static Vector<String> HEADERS;

    public final static int WORD = 0;
    public final static int TAG_WORD = 1;
    //public final static int DESCRIPTION_TAG_WORD = 2;
    public final static int COUNT = 2;
    public final static int WORD_LEMMA = 3;
    public final static int TAG_LEMMA = 4;
    //public final static int DESCRIPTION_TAG_LEMMA = 6;

    public final static String SEPARATOR_FOR_FILE = ";";
    public final static String SEPARATOR_FOR_LIST = ":";

    public final static String TAG_SEPARATOR = "_";

    public final static String LINE_SEPARATOR = "<br>";

    public final static String SEPARATOR_FOR_TAGS = "_";


    public final static int AMOUNT = 5;

    static {
        HEADERS = new Vector<>(AMOUNT);
        HEADERS.add(WORD, "Слово");
        HEADERS.add(TAG_WORD, "Тег слова");
        //HEADERS.add(DESCRIPTION_TAG_WORD, "Расшифровка тега");
        HEADERS.add(COUNT, "Частота");
        HEADERS.add(WORD_LEMMA, "Начальная форма");
        HEADERS.add(TAG_LEMMA, "Тег начальной формы");
        //HEADERS.add(DESCRIPTION_TAG_LEMMA, "Расшифровка тега начальной формы");
    }
}
