package utilities;

import textHandlers.Lemmatizer;

import java.util.Vector;

public class WordData implements Comparable<WordData> {
    private final static int numberOfProperties = 5;
    private String word;
    private String wordTag;
    private String lemmaWord;
    private String lemmaWordTag;
    private int number;

    public WordData(final String wordTag, final String word, final String lemmaWord, final String lemmaWordTag) {
        this.word = word;
        this.wordTag = wordTag;
        this.lemmaWord = lemmaWord;
        this.lemmaWordTag = lemmaWordTag;
        number = 0;
    }

    public WordData(final String wordTag, final String word, int number, final String lemmaWord, final String lemmaWordTag) {
        this(wordTag, word, lemmaWord, lemmaWordTag);
        this.number = number;
    }

    public String getWord() {
        return word;
    }

    public void setWord(final String word) {
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
        return wordTag + "\t" + word + "\t" + number + "\t" + lemmaWord + "\t" + lemmaWordTag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        final WordData pair = (WordData) obj;

        return pair.number == this.number && pair.word.equals(this.getWord());
    }

    @Override
    public int compareTo(final WordData o) {
        return word.compareTo(o.getWord());
    }

    public Vector toVector() {
        final Vector result = new Vector(numberOfProperties);

        result.add(Constants.TAG_WORD, wordTag);
        result.add(Constants.WORD, word);
        result.add(Constants.COUNT, number);
        result.add(Constants.WORD_LEMMA, lemmaWord);
        result.add(Constants.TAG_LEMMA, lemmaWordTag);

        return result;
    }

    public static class Builder {

        public static WordData buildWord(final String word, final int number) {
            final String wordTag = Lemmatizer.getTag(word);
            final String originalWord = Lemmatizer.lemmatize(word);
            final String originalWordTag = Lemmatizer.getTag(originalWord);
            return new WordData(wordTag, word, number, originalWord, originalWordTag);
        }

        public static WordData buildEmptyWord(final String word) {
            final String wordTag = Lemmatizer.getTag(word);
            final String originalWord = Lemmatizer.lemmatize(word);
            final String originalWordTag = Lemmatizer.getTag(originalWord);
            return new WordData(wordTag, word, originalWord, originalWordTag);
        }
    }

}
