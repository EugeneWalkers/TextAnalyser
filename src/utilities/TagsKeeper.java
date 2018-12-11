package utilities;

import java.util.HashMap;
import java.util.Map;

public final class TagsKeeper {

    private final static Map<String, TagData> tags;

    static {
        tags = new HashMap<>();
        tags.put("CC", new TagData("Coordinating conjunction", ColorUtilities.getRandomColor()));
        tags.put("CD", new TagData("Cardinal number", ColorUtilities.getRandomColor()));
        tags.put("DT", new TagData("Determiner", ColorUtilities.getRandomColor()));
        tags.put("EX", new TagData("Existential there", ColorUtilities.getRandomColor()));
        tags.put("FW", new TagData("Foreign word", ColorUtilities.getRandomColor()));
        tags.put("IN", new TagData("Preposition or subordinating conjunction", ColorUtilities.getRandomColor()));
        tags.put("JJ", new TagData("Adjective", ColorUtilities.getRandomColor()));
        tags.put("JJR", new TagData("Adjective, comparative", ColorUtilities.getRandomColor()));
        tags.put("JJS", new TagData("Adjective, superlative", ColorUtilities.getRandomColor()));
        tags.put("LS", new TagData("List item marker", ColorUtilities.getRandomColor()));
        tags.put("MD", new TagData("Modal", ColorUtilities.getRandomColor()));
        tags.put("NN", new TagData("Noun, singular or mass", ColorUtilities.getRandomColor()));
        tags.put("NNS", new TagData("Noun, plural", ColorUtilities.getRandomColor()));
        tags.put("NNP", new TagData("Proper noun, singular", ColorUtilities.getRandomColor()));
        tags.put("NNPS", new TagData("Proper noun, plural", ColorUtilities.getRandomColor()));
        tags.put("PDT", new TagData("Predeterminer", ColorUtilities.getRandomColor()));
        tags.put("POS", new TagData("Possessive ending", ColorUtilities.getRandomColor()));
        tags.put("PRP", new TagData("Personal pronoun", ColorUtilities.getRandomColor()));
        tags.put("PRP$", new TagData("Possessive pronoun", ColorUtilities.getRandomColor()));
        tags.put("RB", new TagData("Adverb", ColorUtilities.getRandomColor()));
        tags.put("RBR", new TagData("Adverb, comparative", ColorUtilities.getRandomColor()));
        tags.put("RBS", new TagData("Adverb, superlative", ColorUtilities.getRandomColor()));
        tags.put("RP", new TagData("Particle", ColorUtilities.getRandomColor()));
        tags.put("SYM", new TagData("Symbol", ColorUtilities.getRandomColor()));
        tags.put("TO", new TagData("to", ColorUtilities.getRandomColor()));
        tags.put("UH", new TagData("Interjection", ColorUtilities.getRandomColor()));
        tags.put("VB", new TagData("Verb, base form", ColorUtilities.getRandomColor()));
        tags.put("VBD", new TagData("Verb, past tense", ColorUtilities.getRandomColor()));
        tags.put("VBG", new TagData("Verb, gerund or present participle", ColorUtilities.getRandomColor()));
        tags.put("VBN", new TagData("Verb, past participle", ColorUtilities.getRandomColor()));
        tags.put("VBP", new TagData("Verb, non-3rd person singular present", ColorUtilities.getRandomColor()));
        tags.put("VBZ", new TagData("Verb, 3rd person singular present", ColorUtilities.getRandomColor()));
        tags.put("WDT", new TagData("Wh-determiner", ColorUtilities.getRandomColor()));
        tags.put("WP", new TagData("Wh-pronoun", ColorUtilities.getRandomColor()));
        tags.put("WP$", new TagData("Possessive wh-pronoun", ColorUtilities.getRandomColor()));
        tags.put("WRB", new TagData("Wh-adverb", ColorUtilities.getRandomColor()));
    }

    public static TagData getTagData(final String tag) {
        return tags.getOrDefault(tag.toUpperCase(), new TagData("?", ColorUtilities.getRandomColor()));
    }

    public static Map<String, TagData> getAllTags(){
        return tags;
    }
}
