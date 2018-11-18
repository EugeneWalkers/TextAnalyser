package utilities;

import textHandlers.Lemmatizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static utilities.Constants.AMOUNT;
import static utilities.Constants.SEPARATOR_FOR_FILE;
import static utilities.StringUtilities.listToString;

public class WordData implements Comparable<WordData> {
    private String word;
    private List<String> wordTag;
    //private List<String> tagTranscripts;
    private List<String> lemmaWord;
    private List<String> lemmaWordTag;
    //private List<String> lemmaTagTranscripts;
    private int number;

    public WordData(
            final String word,
            final List<String> wordTag,
            //final List<String> tagTranscripts,
            final List<String> lemmaWord,
            final List<String> lemmaWordTag
            //final List<String> lemmaTagTranscripts
    ) {
        this.word = word;
        this.wordTag = wordTag;
        //this.tagTranscripts = tagTranscripts;
        this.lemmaWord = lemmaWord;
        this.lemmaWordTag = lemmaWordTag;
        //this.lemmaTagTranscripts = lemmaTagTranscripts;
        number = 0;
    }

    public WordData(
            final String word,
            final List<String> wordTag,
            //final List<String> tagTranscripts,
            final int number,
            final List<String> lemmaWord,
            final List<String> lemmaWordTag
            //final List<String> lemmaTagTranscripts
    ) {
        this(word, wordTag, /*tagTranscripts,*/ lemmaWord, lemmaWordTag/*, lemmaTagTranscripts*/);
        this.number = number;
    }

    public WordData(final Vector dataInVector) {
        this.word = (String) dataInVector.get(Constants.WORD);
        this.wordTag = (List<String>) dataInVector.get(Constants.TAG_WORD);
        this.lemmaWord = (List<String>) dataInVector.get(Constants.WORD_LEMMA);
        this.lemmaWordTag = (List<String>) dataInVector.get(Constants.TAG_LEMMA);
        this.number = (int) dataInVector.get(Constants.COUNT);
    }

    public List<String> getWordTag() {
        return wordTag;
    }

    public void setWordTag(List<String> wordTag) {
        this.wordTag = wordTag;
    }

//    public List<String> getTagTranscripts() {
//        return tagTranscripts;
//    }

//    public void setTagTranscripts(List<String> tagTranscripts) {
//        this.tagTranscripts = tagTranscripts;
//    }

    public List<String> getLemmaWord() {
        return lemmaWord;
    }

    public void setLemmaWord(List<String> lemmaWord) {
        this.lemmaWord = lemmaWord;
    }

    public List<String> getLemmaWordTag() {
        return lemmaWordTag;
    }

    public void setLemmaWordTag(List<String> lemmaWordTag) {
        this.lemmaWordTag = lemmaWordTag;
    }

//    public List<String> getLemmaTagTranscripts() {
//        return lemmaTagTranscripts;
//    }

//    public void setLemmaTagTranscripts(List<String> lemmaTagTranscripts) {
//        this.lemmaTagTranscripts = lemmaTagTranscripts;
//    }

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
                        listToString(wordTag) + SEPARATOR_FOR_FILE +
//                        listToString(tagTranscripts) + SEPARATOR_FOR_FILE +
                        number + SEPARATOR_FOR_FILE +
                        listToString(lemmaWord) + SEPARATOR_FOR_FILE +
                        listToString(lemmaWordTag) + SEPARATOR_FOR_FILE
//                        listToString(lemmaTagTranscripts)
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
        result.add(Constants.TAG_WORD, listToString(wordTag));
//        result.add(Constants.DESCRIPTION_TAG_WORD, listToString(tagTranscripts));
        result.add(Constants.COUNT, number);
        result.add(Constants.WORD_LEMMA, listToString(lemmaWord));
        result.add(Constants.TAG_LEMMA, listToString(lemmaWordTag));
//        result.add(Constants.DESCRIPTION_TAG_LEMMA, listToString(lemmaTagTranscripts));

        return result;
    }

    public static class Builder {

        public static WordData buildWord(final String word, final int number) {
            final List<String> wordTag = Lemmatizer.getTag(word);
            final List<String> lemmaWord = Lemmatizer.lemmatize(word);
            final List<String> lemmaWordTag = new ArrayList<>();
//            final List<String> tagTranscripts = new ArrayList<>();
//            final List<String> lemmaTagTranscripts = new ArrayList<>();

            for (final String original : lemmaWord) {
                lemmaWordTag.addAll(Lemmatizer.getTag(original));
            }

//            for (final String tag : wordTag) {
//                tagTranscripts.add(TagsKeeper.getDescription(tag));
//            }
//
//            for (final String lemmaTag : lemmaWordTag) {
//                lemmaTagTranscripts.add(TagsKeeper.getDescription(lemmaTag));
//            }

            return new WordData(word, wordTag, /*tagTranscripts,*/ number, lemmaWord, lemmaWordTag/*, lemmaTagTranscripts*/);
        }

        public static WordData buildEmptyWord(final String word) {
            final List<String> wordTag = Lemmatizer.getTag(word);
            final List<String> lemmaWord = Lemmatizer.lemmatize(word);
            final List<String> lemmaWordTag = new ArrayList<>();
            final List<String> tagTranscripts = new ArrayList<>();
            final List<String> lemmaTagTranscripts = new ArrayList<>();

            for (final String original : lemmaWord) {
                lemmaWordTag.addAll(Lemmatizer.getTag(original));
            }

            for (final String tag : wordTag) {
                tagTranscripts.add(TagsKeeper.getDescription(tag));
            }

            for (final String lemmaTag : lemmaWordTag) {
                lemmaTagTranscripts.add(TagsKeeper.getDescription(lemmaTag));
            }

            return new WordData(word, wordTag, /*tagTranscripts,*/ lemmaWord, lemmaWordTag/*, lemmaTagTranscripts*/);
        }
    }

}
