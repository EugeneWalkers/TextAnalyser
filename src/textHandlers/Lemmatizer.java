package textHandlers;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.MultiTokenTag;
import edu.stanford.nlp.ling.Tag;
import edu.stanford.nlp.ling.tokensregex.types.Tags;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Lemmatizer {

    private final StanfordCoreNLP pipeline;
    private final MaxentTagger tagger;

    public Lemmatizer() {
        final Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos");

        pipeline = new StanfordCoreNLP(props);

        tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");

    }
//
//    static {
//        // Create StanfordCoreNLP object properties, with POS tagging
//        // (required for lemmatization), and lemmatization
//        final Properties props = new Properties();
//        props.put("annotators", "tokenize, ssplit, pos, lemma");
//
//        // StanfordCoreNLP loads a lot of models, so you probably
//        // only want to do this once per execution
//        pipeline = new StanfordCoreNLP(props);
//    }

    public static List<String> lemmatize(final String word) {
//        final Annotation tokenAnnotation = new Annotation(word);
//        pipeline.annotate(tokenAnnotation);
//        final List<CoreMap> list = tokenAnnotation.get(CoreAnnotations.SentencesAnnotation.class);
//
//        return list
//                .get(0).get(CoreAnnotations.TokensAnnotation.class)
//                .get(0).get(CoreAnnotations.LemmaAnnotation.class);
        return new Sentence(word).lemmas();

    }

    public static List<String> getTag(final String word) {
        return new Sentence(word).posTags();
    }

    public String paintTextWithPosTags(final String text) {
        final StringBuilder textBuilder = new StringBuilder();
        final String[] textInLines = text.split("\n");

        for (int i = 0; i < textInLines.length; i++) {
            final Annotation tokenAnnotation = new Annotation(textInLines[i]);

            pipeline.annotate(tokenAnnotation);

            final List<CoreMap> sentences = tokenAnnotation.get(CoreAnnotations.SentencesAnnotation.class);

            for (final CoreMap sentence : sentences) {
                System.out.println(sentence.keySet());
                final String tokens = sentence.toShorterString("Tokens");
                final String sent = sentence.toString();
                System.out.println(sent);
                textBuilder.append(tagger.tagString(sentence.toString()));
            }

            textBuilder.append(i == (textInLines.length - 1) ? "":System.lineSeparator());
        }

        return textBuilder.toString();
    }

    private boolean isPaintNeeded(final String word){
        return !word.contains("_");
    }

    private List<String> getWordsByTokens(final String tokens){ // "[Tokens=[Hello-1, ,-2, world-3, !-4]]" -> {Hello, ,, world, !] (as List)
        final StringBuilder builder = new StringBuilder(tokens);
        builder.deleteCharAt(0);
        builder.deleteCharAt(builder.length() - 1);
        int first = builder.indexOf("=");
        builder.delete(0, first);
        builder.deleteCharAt(0);
        builder.deleteCharAt(builder.length() - 1);
        final String someResult = builder.toString();
        return null;
    }

}