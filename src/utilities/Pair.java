package utilities;

import java.util.Vector;

public class Pair implements Comparable<Pair> {
    private String word;
    private int number;

    Pair(String word) {
        this.word = word;
        number = 1;
    }

    public Pair(String word, int number) {
        this.word = word;
        this.number = number;
    }

    public Pair(Vector v) {
        this.word = (String) v.get(0);
        this.number = Integer.parseInt((String) v.get(1));
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return word + "\t" + number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Pair pair = (Pair) obj;

        return pair.number == this.number && pair.word.equals(this.getWord());
    }

    public String[] toStringArray() {
        String[] pairInStringArray = new String[2];
        pairInStringArray[0] = word;
        pairInStringArray[1] = "" + number;

        return pairInStringArray;
    }

    @Override
    public int compareTo(final Pair o) {
        return word.compareTo(o.getWord());
    }
}
