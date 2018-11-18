package textHandlers;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.Tag;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Lemmatizer {

//    private static final StanfordCoreNLP pipeline;
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

    public static String lemmatize(final String word) {
//        final Annotation tokenAnnotation = new Annotation(word);
//        pipeline.annotate(tokenAnnotation);
//        final List<CoreMap> list = tokenAnnotation.get(CoreAnnotations.SentencesAnnotation.class);
//
//        return list
//                .get(0).get(CoreAnnotations.TokensAnnotation.class)
//                .get(0).get(CoreAnnotations.LemmaAnnotation.class);

        return new Sentence(word).lemma(0);

    }

    public static String getTag(final String word){
        return new Sentence(word).posTags().get(0);
    }

}