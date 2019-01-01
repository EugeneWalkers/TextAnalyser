package utilities;

import textHandlers.Lemmatizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static utilities.Constants.AMOUNT;
import static utilities.Constants.SEPARATOR_FOR_FILE;
import static utilities.StringUtilities.listToString;
import static utilities.StringUtilities.tagListToString;
import static utilities.StringUtilities.tagListToStringWithoutNumb;

public class WordData implements Comparable<WordData> {
    private String word;
    private List<TagCountData> wordTag;
    private List<String> lemmaWord;
    private List<String> lemmaWordTag;
    private int number;

    public WordData(
            final String word,
            final List<TagCountData> wordTag,
            final List<String> lemmaWord,
            final List<String> lemmaWordTag
    ) {
        this.word = word;
        this.wordTag = wordTag;
        this.lemmaWord = lemmaWord;
        this.lemmaWordTag = lemmaWordTag;
        number = 0;
    }

    public WordData(
            final String word,
            final List<TagCountData> wordTag,
            final int number,
            final List<String> lemmaWord,
            final List<String> lemmaWordTag
    ) {
        this(word, wordTag, lemmaWord, lemmaWordTag);
        this.number = number;
    }

    public WordData(final Vector dataInVector) {
        this.word = (String) dataInVector.get(Constants.WORD);
        this.wordTag = (List<TagCountData>) dataInVector.get(Constants.TAG_WORD);
        this.lemmaWord = (List<String>) dataInVector.get(Constants.WORD_LEMMA);
        this.lemmaWordTag = (List<String>) dataInVector.get(Constants.TAG_LEMMA);
        this.number = (int) dataInVector.get(Constants.COUNT);
    }

    public List<TagCountData> getWordTag() {
        return wordTag;
    }

    public void setWordTag(List<TagCountData> wordTag) {
        this.wordTag = wordTag;
    }

    public List<String> getLemmaWordTag() {
        return lemmaWordTag;
    }

    public void setLemmaWordTag(List<String> lemmaWordTag) {
        this.lemmaWordTag = lemmaWordTag;
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
        return
                word + SEPARATOR_FOR_FILE +
                        tagListToString(wordTag) + SEPARATOR_FOR_FILE +
                        number + SEPARATOR_FOR_FILE +
                        listToString(lemmaWord) + SEPARATOR_FOR_FILE +
                        listToString(lemmaWordTag) + SEPARATOR_FOR_FILE
                ;
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
        final Vector result = new Vector(AMOUNT);

        result.add(Constants.WORD, word);
        result.add(Constants.TAG_WORD, tagListToString(wordTag));
        result.add(Constants.COUNT, number);
        result.add(Constants.WORD_LEMMA, listToString(lemmaWord));
        result.add(Constants.TAG_LEMMA, listToString(lemmaWordTag));

        return result;
    }

    public Vector toVectorWithoutTagNumb() {
        final Vector result = new Vector(AMOUNT);

        result.add(Constants.WORD, word);
        result.add(Constants.TAG_WORD, tagListToStringWithoutNumb(wordTag));
        result.add(Constants.COUNT, number);
        result.add(Constants.WORD_LEMMA, listToString(lemmaWord));
        result.add(Constants.TAG_LEMMA, listToString(lemmaWordTag));

        return result;
    }

    public static class Builder {

        public static WordData buildWord(final String word, final int number) {
            final List<String> wordTagString = Lemmatizer.getTag(word);

            final List<TagCountData> wordTag = new ArrayList<>();
            final List<String> lemmaWord = Lemmatizer.lemmatize(word);
            final List<String> lemmaWordTag = new ArrayList<>();

            for (final String original : lemmaWord) {
                lemmaWordTag.addAll(Lemmatizer.getTag(original));
            }

            for (final String tag: wordTagString){
                wordTag.add(new TagCountData(tag, number/wordTagString.size()));
            }

            return new WordData(word, wordTag, number, lemmaWord, lemmaWordTag);
        }

        public static WordData buildEmptyWord(final String word) {
            final List<String> wordTagString = Lemmatizer.getTag(word);

            final List<TagCountData> wordTag = new ArrayList<>();
            final List<String> lemmaWord = Lemmatizer.lemmatize(word);
            final List<String> lemmaWordTag = new ArrayList<>();

            for (final String original : lemmaWord) {
                lemmaWordTag.addAll(Lemmatizer.getTag(original));
            }

            for (final String tag: wordTagString){
                wordTag.add(new TagCountData(tag));
            }


            return new WordData(word, wordTag, lemmaWord, lemmaWordTag);
        }
    }

}
