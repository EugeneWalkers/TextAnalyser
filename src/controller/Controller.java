package controller;

import javafx.util.Pair;
import textHandlers.MapReduce;
import utilities.Constants;
import utilities.DataKeeper;
import utilities.StringUtilities;
import utilities.WordData;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static textHandlers.MapReduce.MAP;
import static textHandlers.MapReduce.REDUCE;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Controller {

    private static final Controller INSTANCE = new Controller();

    private final static String OUT_MAP_TXT = "output/outMap.txt";
    private final static String OUT_SORT_TXT = "output/outSort.txt";
    private final File outMap;
    private final File outSort;
    private final File dictionary;
    private final File temporaryOutput;
    private List<File> files;
    private MapReduce mapReduce;
    private int defaultFileCounter;

    {
        outMap = new File(OUT_MAP_TXT);
        outSort = new File(OUT_SORT_TXT);
        defaultFileCounter = 0;
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

    private List<WordData> getData() {
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

    public Vector<Vector> getDataByWord(final String word) {
        final List<WordData> data = getData();
        final Vector<Vector> newData = new Vector<>();

        for (final WordData wordData : data) {
            if (wordData.getWord().equals(word)) {
                newData.add(0, wordData.toVector());
                continue;
            }
            if (StringUtilities.isSubEquals(wordData.getWord(), word)) {
                newData.add(wordData.toVector());
            }
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

    public void addTextAndRewriteFile(final String text) {
        rewriteFileWithNewText(text, pullFile());
    }

    private void rewriteFileWithNewText(final String text, final File file) {
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

    private void rewritePairsToWordData() {
        final List<Pair<String, Integer>> pairs = DataKeeper.readPairsFromFile(temporaryOutput);
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

    public void notifyTableChanged(Vector<Vector> newData) {
        final List<WordData> oldData = DataKeeper.readWordDatasFromFile(dictionary);

        if (oldData.size() == newData.size()) {
            final String[] replaces = changeElementInDictionaryAndConcat(oldData, newData);

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
        int index = -1;

        for (int i = 0; i < oldData.size(); i++) {
            if (!oldData.get(i).getWord().equals(newData.get(i).get(Constants.WORD))) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return null;
        }

        final String[] result = new String[2];
        result[0] = oldData.get(index).getWord();
        result[1] = (String) newData.get(index).get(Constants.WORD);

        final WordData changedPair = WordData.Builder.buildWord((String) newData.get(index).get(Constants.WORD), (int) newData.get(index).get(Constants.COUNT));

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
            oldData.get(index).setWord((String) newData.get(index).get(Constants.WORD));
        } else {
            oldData.remove(index);
        }

        DataKeeper.writeWordDatasToFile(oldData, dictionary);

        return result;
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
