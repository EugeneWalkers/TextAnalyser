package controller;

import javafx.util.Pair;
import textHandlers.Lemmatizer;
import textHandlers.MapReduce;
import utilities.DataKeeper;
import utilities.StringUtilities;
import utilities.WordData;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static textHandlers.MapReduce.MAP;
import static textHandlers.MapReduce.REDUCE;
import static utilities.CollectionUtilities.areCollectionsEqual;
import static utilities.Constants.*;
import static utilities.StringUtilities.stringToList;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Controller {

    private static final Controller INSTANCE = new Controller();

    private final static String OUT_MAP_TXT = "output/outMap.txt";
    private final static String OUT_SORT_TXT = "output/outSort.txt";

    private final File outMap;
    private final File outSort;
    private final File dictionary;
    private final File temporaryOutput;
    private final Lemmatizer lemmatizer;

    private List<File> files;
    private MapReduce mapReduce;
    private int defaultFileCounter;

    {
        outMap = new File(OUT_MAP_TXT);
        outSort = new File(OUT_SORT_TXT);
        defaultFileCounter = 0;
        lemmatizer = new Lemmatizer();
        files = new ArrayList<>();
        dictionary = new File("output/output.txt");
        temporaryOutput = new File("output/temp.txt");
    }

    private Controller() {
        mapReduce = new MapReduce();
    }

    public static Controller getInstance() {
        return INSTANCE;
    }

    public List<WordData> getData() {

        return DataKeeper.readWordDatasFromFile(dictionary);
    }

    public Vector<Vector> getDataInVector() {
        final List<WordData> data = getData();
        final Vector<Vector> dataInVector = new Vector<>();

        for (final WordData aData : data) {
            dataInVector.add(aData.toVector());
        }

        return dataInVector;
    }

    public List<WordData> getDataByWord(final String word) {
        final List<WordData> data = getData();
        final List<WordData> newData = new Vector<>();

        for (final WordData wordData : data) {
            if (wordData.getWord().equals(word)) {
                newData.add(0, wordData);
                continue;
            }
            if (StringUtilities.isSubEquals(wordData.getWord(), word)) {
                newData.add(wordData);
            }
        }

        return newData;
    }

    public Vector<Vector> getDataByWordForTable(final String word) {
        final Vector<Vector> newData = new Vector<>();
        final List<WordData> dataInList = getDataByWord(word);

        for (final WordData data : dataInList) {
            newData.add(data.toVector());
        }

        return newData;
    }

    private File createDefaultFile() {
        final File newFile = new File("default " + defaultFileCounter++ + ".txt");

        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFile;
    }

    public void pushFile(final File file) {
        if (file == null) {
            files.add(createDefaultFile());
        } else {
            files.add(new File(file.getAbsolutePath()));
        }
    }

    private File pullFile() {
        return files.get(files.size() - 1);
    }

    public void rewriteLastFile(final String text) {
        rewriteFileWithNewText(text, pullFile());
    }

    public void rewriteFileWithNewText(final String text, final File file) {
        file.delete();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataKeeper.writeTextToFile(text, file);
    }

    public void handle() {
        mapReduce.runCommand(MAP, files.get(files.size() - 1), outMap);
        sort();
        mapReduce.runCommand(REDUCE, outSort, temporaryOutput);
        rewritePairsToWordData();
        mergeFiles();
        sortPairs();
        cleanFiles();
    }

    public void addWord(final String word) {
        DataKeeper.writeTextToFile(WordData.Builder.buildEmptyWord(word).toString(), temporaryOutput);
        mergeFiles();
        sortPairs();
        temporaryOutput.delete();
    }

    public void paintText(final String text) {
        DataKeeper.writeTextToFile(lemmatizer.paintTextWithPosTags(text), new File("painted.txt"/*pullFile().getName().split("\\.")[0] + " painted.txt")*/));
    }

    public void repaintText(final String text) {

    }
    private void rewritePairsToWordData() {
        final List<Pair<String, Integer>> pairs = DataKeeper.readPairsFromFile(temporaryOutput);
        temporaryOutput.delete();
        final List<WordData> wordDatas = new ArrayList<>();
        for (final Pair<String, Integer> pair : pairs) {
            wordDatas.add(WordData.Builder.buildWord(pair.getKey(), pair.getValue()));
        }
        DataKeeper.writeWordDatasToFile(wordDatas, temporaryOutput);
    }

    private void sort() {
        final List<Pair<String, Integer>> pairs = DataKeeper.readPairsFromFile(outMap);
        pairs.sort(Comparator.comparing(Pair::getKey));
        DataKeeper.writePairsToFile(pairs, outSort);
    }

    private void mergeFiles() {
        if (!dictionary.exists()) {
            try {
                dictionary.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final List<WordData> newPairs = DataKeeper.readWordDatasFromFile(temporaryOutput);
        final List<WordData> oldPairs = DataKeeper.readWordDatasFromFile(dictionary);

        boolean isExist = false;

        while (!newPairs.isEmpty()) {
            final WordData pair = newPairs.get(0);

            for (final WordData oldPair : oldPairs) {
                if (oldPair.getWord().equals(pair.getWord())) {
                    isExist = true;
                    oldPair.setNumber(oldPair.getNumber() + pair.getNumber());
                    newPairs.remove(pair);
                }
            }

            if (!isExist) {
                oldPairs.add(pair);
                newPairs.remove(pair);
            }

            isExist = false;
        }

        DataKeeper.writeWordDatasToFile(oldPairs, dictionary);
    }

    private void sortPairs() {
        final List<WordData> wordDatas = DataKeeper.readWordDatasFromFile(dictionary);
        Collections.sort(wordDatas);
        DataKeeper.writeWordDatasToFile(wordDatas, dictionary);
    }

    public void notifyTableChanged(final Vector<Vector> newData) {
        final List<WordData> oldData = DataKeeper.readWordDatasFromFile(dictionary);

        if (oldData.size() == newData.size()) {
            final String[] replaces = changeElementInDictionaryAndConcat(oldData, newData);
          
            if (replaces != null) {
                replaceStringInAllFiles(replaces[0], replaces[1]);
            }
        }
    }

    public void notifyTableChanged(final Vector<Vector> newData, final String word) {
        final List<WordData> oldData = getDataByWord(word);

        int i = -1;

        for (i = 0; i < oldData.size(); i++) {
            if (!newData.get(i).get(WORD).equals(oldData.get(i).getWord())) {
                break;
            }
        }

        final WordData oldWordData = oldData.get(i);
        final Vector newWordData = newData.get(i);

        final List<WordData> fullCachedOld = getData();
        final Vector<Vector> fullCachedToChange = getDataInVector();

        for (int j = 0; j < fullCachedToChange.size(); j++) {
            if (fullCachedToChange.get(i).get(WORD).equals(oldWordData.getWord())) {

                final WordData data = new WordData(newWordData);
                fullCachedToChange.set(i, data.toVector());
                break;
            }
        }

        if (oldData.size() == newData.size()) {
            final String[] replaces = changeElementInDictionaryAndConcat(fullCachedOld, fullCachedToChange);

            if (replaces != null) {
                replaceStringInAllFiles(replaces[0], replaces[1]);
            }
        }
    }

    private void replaceStringInAllFiles(final String from, final String to) {
        for (final File file : files) {
            final String text = DataKeeper.readTextFromFile(file);
            DataKeeper.writeTextToFile(text.replaceAll(from, to), file);
        }
    }

    private String[] changeElementInDictionaryAndConcat(final List<WordData> oldData, final Vector<Vector> newData) {
        final Pair<Integer, Integer> changed = findIndexOfChangedElement(oldData, newData);
        final int index = changed.getKey();
        final int type = changed.getValue();
        String[] result = null;

        switch (type) {
            case WORD:
                result = changeWord(oldData, newData, index);
                break;

            case TAG_WORD:
                oldData.get(index).setWordTag(stringToList((String) newData.get(index).get(TAG_WORD)));
                break;

            case TAG_LEMMA:
                oldData.get(index).setLemmaWordTag(stringToList((String) newData.get(index).get(TAG_LEMMA)));
                break;

            default:
                return null;
        }

        DataKeeper.writeWordDatasToFile(oldData, dictionary);

        return result;
    }


    private String[] changeWord(final List<WordData> oldData, final Vector<Vector> newData, final int index) {
        newData.get(index).set(WORD, ((String) newData.get(index).get(WORD)).toLowerCase());

        final String[] result = new String[2];
        result[0] = oldData.get(index).getWord();
        result[1] = ((String) newData.get(index).get(WORD));

        final WordData changedPair = WordData.Builder.buildWord(
                ((String) newData.get(index).get(WORD)),
                (int) newData.get(index).get(COUNT)
        );

        boolean isNew = true;

        for (int i = 0; i < newData.size(); i++) {
            if (changedPair.getWord().equals(oldData.get(i).getWord()) && i != index) {
                isNew = false;
                final WordData wordDataInWhatAdd = oldData.get(i);
                wordDataInWhatAdd.setNumber(oldData.get(i).getNumber() + changedPair.getNumber());

                break;
            }
        }

        if (isNew) {
            final WordData data = WordData.Builder.buildWord((String) newData.get(index).get(WORD), oldData.get(index).getNumber());
            oldData.set(index, data);
        } else {
            oldData.remove(index);
        }


        return result;
    }

    private Pair<Integer, Integer> findIndexOfChangedElement(final List<WordData> oldData, final Vector<Vector> newData) {
        int index = -1;
        int type = -1;

        for (int i = 0; i < oldData.size(); i++) {
            if (!oldData.get(i).getWord().equals(newData.get(i).get(WORD))) {
                index = i;
                type = WORD;
                break;
            }

            if (!areCollectionsEqual(
                    oldData.get(i).getWordTag(),
                    stringToList(
                            (String) newData.get(i).get(TAG_WORD)
                    )
            )
            ) {
                index = i;
                type = TAG_WORD;
                break;
            }

            if (!areCollectionsEqual(
                    oldData.get(i).getLemmaWordTag(),
                    stringToList(
                            (String) newData.get(i).get(TAG_LEMMA)
                    )
            )) {
                index = i;
                type = TAG_LEMMA;
                break;
            }
        }

        return new Pair<>(index, type);
    }

    private void cleanFiles() {
        outMap.delete();
        outSort.delete();
        temporaryOutput.delete();
    }

    public void deleteAll() {
        files.clear();
        dictionary.delete();
    }

}
