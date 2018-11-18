import textHandlers.Lemmatizer;
import view.DictionaryFrame;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String args[]) {
//        DictionaryFrame dictionaryFrame = DictionaryFrame.getInstance();
//        dictionaryFrame.setVisible(true);


        // Не работает для b

//        ArrayList<Integer> a1 = new ArrayList<>();
//        ArrayList<Integer> a2 = new ArrayList<>();
//
//        a1.add(1);
//        a1.add(2);
//        a1.add(3);
//        a1.add(4);
//
//        a2.add(1);
//        a2.add(2);
//        a2.add(5);
//
//        //a1.addAll(a2);
//
//        a1.removeAll(a2);
//        a1.addAll(a2);
//
//        System.out.println(a1);

        System.out.println(Lemmatizer.getTag("Did you book a room?"));
    }
}
