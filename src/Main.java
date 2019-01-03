import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import textHandlers.Lemmatizer;
import view.DictionaryFrame;

import javax.swing.*;

public class Main {

    public static void main(String args[]) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        DictionaryFrame.getInstance().setVisible(true);
    }
}
