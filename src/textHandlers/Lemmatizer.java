package textHandlers;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Tag;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import utilities.StringUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Lemmatizer {

    private final StanfordCoreNLP pipeline;

    public Lemmatizer(){
        final Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos");

        pipeline = new StanfordCoreNLP(props);
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

    public static List<String> getTag(final String word){
        return new Sentence(word).posTags();
    }

    public String paintTextWithPosTags(final String text){
        final Annotation tokenAnnotation = new Annotation(text);
        pipeline.annotate(tokenAnnotation);
        final List<CoreMap> sentences = tokenAnnotation.get(CoreAnnotations.SentencesAnnotation.class);

        final StringBuilder textBuilder = new StringBuilder();


        for(final CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (final CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the list of lemmas
                final String[] words = sentence.toString().split("");
                final String[] tags = token.tag().split("");
                for (int i=0; i<sentences.size(); i++){
                    textBuilder.append(words[i]).append("(").append(tags[i]).append(") ");
                }
                textBuilder.append(sentence.toString());
                textBuilder.append(token.get(CoreAnnotations.LemmaAnnotation.class));
                System.out.println(token.tag());
            }
        }

        System.out.println(textBuilder.toString());

        return textBuilder.toString();
    }

}