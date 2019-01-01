package utilities;

import java.util.HashMap;
import java.util.Map;

public final class TagsKeeper {

    private final static Map<String, TagColorData> tags;

    static {
        tags = new HashMap<>();
        tags.put("CC", new TagColorData("Coordinating conjunction", ColorUtilities.getRandomColor()));
        tags.put("CD", new TagColorData("Cardinal number", ColorUtilities.getRandomColor()));
        tags.put("DT", new TagColorData("Determiner", ColorUtilities.getRandomColor()));
        tags.put("EX", new TagColorData("Existential there", ColorUtilities.getRandomColor()));
        tags.put("FW", new TagColorData("Foreign word", ColorUtilities.getRandomColor()));
        tags.put("IN", new TagColorData("Preposition or subordinating conjunction", ColorUtilities.getRandomColor()));
        tags.put("JJ", new TagColorData("Adjective", ColorUtilities.getRandomColor()));
        tags.put("JJR", new TagColorData("Adjective, comparative", ColorUtilities.getRandomColor()));
        tags.put("JJS", new TagColorData("Adjective, superlative", ColorUtilities.getRandomColor()));
        tags.put("LS", new TagColorData("List item marker", ColorUtilities.getRandomColor()));
        tags.put("MD", new TagColorData("Modal", ColorUtilities.getRandomColor()));
        tags.put("NN", new TagColorData("Noun, singular or mass", ColorUtilities.getRandomColor()));
        tags.put("NNS", new TagColorData("Noun, plural", ColorUtilities.getRandomColor()));
        tags.put("NNP", new TagColorData("Proper noun, singular", ColorUtilities.getRandomColor()));
        tags.put("NNPS", new TagColorData("Proper noun, plural", ColorUtilities.getRandomColor()));
        tags.put("PDT", new TagColorData("Predeterminer", ColorUtilities.getRandomColor()));
        tags.put("POS", new TagColorData("Possessive ending", ColorUtilities.getRandomColor()));
        tags.put("PRP", new TagColorData("Personal pronoun", ColorUtilities.getRandomColor()));
        tags.put("PRP$", new TagColorData("Possessive pronoun", ColorUtilities.getRandomColor()));
        tags.put("RB", new TagColorData("Adverb", ColorUtilities.getRandomColor()));
        tags.put("RBR", new TagColorData("Adverb, comparative", ColorUtilities.getRandomColor()));
        tags.put("RBS", new TagColorData("Adverb, superlative", ColorUtilities.getRandomColor()));
        tags.put("RP", new TagColorData("Particle", ColorUtilities.getRandomColor()));
        tags.put("SYM", new TagColorData("Symbol", ColorUtilities.getRandomColor()));
        tags.put("TO", new TagColorData("to", ColorUtilities.getRandomColor()));
        tags.put("UH", new TagColorData("Interjection", ColorUtilities.getRandomColor()));
        tags.put("VB", new TagColorData("Verb, base form", ColorUtilities.getRandomColor()));
        tags.put("VBD", new TagColorData("Verb, past tense", ColorUtilities.getRandomColor()));
        tags.put("VBG", new TagColorData("Verb, gerund or present participle", ColorUtilities.getRandomColor()));
        tags.put("VBN", new TagColorData("Verb, past participle", ColorUtilities.getRandomColor()));
        tags.put("VBP", new TagColorData("Verb, non-3rd person singular present", ColorUtilities.getRandomColor()));
        tags.put("VBZ", new TagColorData("Verb, 3rd person singular present", ColorUtilities.getRandomColor()));
        tags.put("WDT", new TagColorData("Wh-determiner", ColorUtilities.getRandomColor()));
        tags.put("WP", new TagColorData("Wh-pronoun", ColorUtilities.getRandomColor()));
        tags.put("WP$", new TagColorData("Possessive wh-pronoun", ColorUtilities.getRandomColor()));
        tags.put("WRB", new TagColorData("Wh-adverb", ColorUtilities.getRandomColor()));
    }

    public static TagColorData getTagData(final String tag) {
        return tags.getOrDefault(tag.toUpperCase(), new TagColorData("?", ColorUtilities.getRandomColor()));
    }

    public static Map<String, TagColorData> getAllTags(){
        return tags;
    }
}
