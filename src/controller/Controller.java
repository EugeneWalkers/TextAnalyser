package controller;

import textHandler.MapReduce;
import utilities.DataKeeper;
import utilities.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import static textHandler.MapReduce.MAP;
import static textHandler.MapReduce.REDUCE;

public class Controller {


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

    public Controller() {
        mapReduce = new MapReduce();
    }

    public Vector<Vector> getData() {
        final List<Pair> data = DataKeeper.readPairsFromFile(dictionary);
        Vector<Vector> dataInStringMatrix = new Vector<>();
        for (int i = 0; i < data.size(); i++) {
            Vector temp = new Vector();
            temp.add(data.get(i).getWord());
            temp.add(data.get(i).getNumber());
            dataInStringMatrix.add(temp);
        }
        return dataInStringMatrix;
    }

    public File createDefaultFile() {
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

    public File pullFile() {
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
        final List<Pair> pairs = DataKeeper.readPairsFromFile(outMap);
        Collections.sort(pairs);
        DataKeeper.writePairsToFile(pairs, outSort);
        mapReduce.runCommand(REDUCE, outSort, temporaryOutput);
        mergeFiles();
        sortPairs();
        cleanFiles();
    }


    private void mergeFiles() {
        if (!dictionary.exists()) {
            try {
                dictionary.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<Pair> newPairs = DataKeeper.readPairsFromFile(temporaryOutput);
        List<Pair> oldPairs = DataKeeper.readPairsFromFile(dictionary);

        boolean isExist = false;

        while (!newPairs.isEmpty()) {
            final Pair pair = newPairs.get(0);

            for (int j = 0; j < oldPairs.size(); j++) {
                if (oldPairs.get(j).getWord().equals(pair.getWord())) {
                    isExist = true;
                    oldPairs.get(j).setNumber(oldPairs.get(j).getNumber() + pair.getNumber());
                    newPairs.remove(pair);
                }
            }

            if (!isExist) {
                oldPairs.add(pair);
                newPairs.remove(pair);
            }

            isExist = false;
        }

        DataKeeper.writePairsToFile(oldPairs, dictionary);
    }

    public void sortPairs() {
        final List<Pair> pairs = DataKeeper.readPairsFromFile(dictionary);

        Collections.sort(pairs);

        DataKeeper.writePairsToFile(pairs, dictionary);
    }

    public void notifyTableChanged(Vector<Vector> newData) {
        final List<Pair> oldData = DataKeeper.readPairsFromFile(dictionary);

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

    private String[] changeElementInDictionaryAndConcat(final List<Pair> oldData, final Vector<Vector> newData) {
        int index = -1;

        for (int i = 0; i < oldData.size(); i++) {
            if (!oldData.get(i).getWord().equals(newData.get(i).get(0))) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return null;
        }

        final String[] result = new String[2];
        result[0] = oldData.get(index).getWord();
        result[1] = (String) newData.get(index).get(0);

        final Pair changedPair = new Pair((String) newData.get(index).get(0), (int) newData.get(index).get(1));

        boolean isNew = true;

        for (int i = 0; i < newData.size(); i++) {
            if (changedPair.getWord().equals(oldData.get(i).getWord()) && i != index) {
                isNew = false;
                final Pair pairInWhatAdd = oldData.get(i);
                pairInWhatAdd.setNumber(oldData.get(i).getNumber() + changedPair.getNumber());

                break;
            }
        }

        if (isNew) {
            oldData.get(index).setWord((String) newData.get(index).get(0));
        } else {
            oldData.remove(index);
        }

        DataKeeper.writePairsToFile(oldData, dictionary);

        return result;
    }

    public void cleanFiles() {
        outMap.delete();
        outSort.delete();
        temporaryOutput.delete();
    }
}
