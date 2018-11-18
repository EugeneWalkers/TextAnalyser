package utilities;

import textHandlers.Lemmatizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static utilities.StringUtilities.listToString;

public class WordData implements Comparable<WordData> {
    private final static int numberOfProperties = 5;
    private String word;
    private List<String> wordTag;
    private List<String>lemmaWord;
    private List<String> lemmaWordTag;
    private int number;

    public WordData(final List<String> wordTag, final String word, final List<String> lemmaWord, final List<String> lemmaWordTag) {
        this.word = word;
        this.wordTag = wordTag;
        this.lemmaWord = lemmaWord;
        this.lemmaWordTag = lemmaWordTag;
        number = 0;
    }

    public WordData(final List<String> wordTag, final String word, int number, final List<String> lemmaWord, final List<String> lemmaWordTag) {
        this(wordTag, word, lemmaWord, lemmaWordTag);
        this.number = number;
    }

    public WordData(final Vector dataInVector){
        this.word = (String) dataInVector.get(Constants.WORD);
        this.wordTag = (List<String>) dataInVector.get(Constants.TAG_WORD);
        this.lemmaWord = (List<String>) dataInVector.get(Constants.WORD_LEMMA);
        this.lemmaWordTag = (List<String>) dataInVector.get(Constants.TAG_LEMMA);
        this.number = (int) dataInVector.get(Constants.COUNT);
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
        return listToString(wordTag) + "\t" + word + "\t" + number + "\t" + listToString(lemmaWord) + "\t" + listToString(lemmaWordTag);
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
            final List<String> wordTag = Lemmatizer.getTag(word);
            final List<String> originalWord = Lemmatizer.lemmatize(word);
            final List<String> originalWordTag = new ArrayList<>();
            for (final String original : originalWord) {
                originalWordTag.addAll(Lemmatizer.getTag(original));
            }
            return new WordData(wordTag, word, number, originalWord, originalWordTag);
        }

        public static WordData buildEmptyWord(final String word) {
            final List<String> wordTag = Lemmatizer.getTag(word);
            final List<String> originalWord = Lemmatizer.lemmatize(word);
            final List<String> originalWordTag = new ArrayList<>();
            for (final String original : originalWord) {
                originalWordTag.addAll(Lemmatizer.getTag(original));
            }
            return new WordData(wordTag, word, originalWord, originalWordTag);
        }
    }

}
