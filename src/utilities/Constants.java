package utilities;

import java.util.Vector;

public class Constants {
    public final static Vector<String> HEADERS;

    public final static int TAG_WORD = 0;
    public final static int WORD = 1;
    public final static int COUNT = 2;
    public final static int WORD_LEMMA = 3;
    public final static int TAG_LEMMA = 4;

    static {
        HEADERS = new Vector<>();
        HEADERS.add("Тег слова");
        HEADERS.add("Слово");
        HEADERS.add("Количество повторений");
        HEADERS.add("Начальная форма");
        HEADERS.add("Тег начальной формы");
    }
}
