package utilities;

import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static utilities.Constants.*;
import static utilities.StringUtilities.stringToList;
import static utilities.StringUtilities.tagStringToList;

public final class DataKeeper {

    private DataKeeper() {

    }

    public static List<Pair<String, Integer>> readPairsFromFile(final File file) {
        if (file.exists()) {
            final List<Pair<String, Integer>> wordDatas = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                final StringBuilder builder = new StringBuilder();

                while (!builder.append(reader.readLine()).toString().equals("null")) {
                    String[] strings = builder.toString().split("\t");
                    wordDatas.add(new Pair<>(strings[0], Integer.parseInt(strings[1])));
                    builder.delete(0, builder.length());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return wordDatas;
        }

        return new ArrayList<>();
    }

    public static void writePairsToFile(final List<Pair<String, Integer>> pairs, final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {

            for (int i = 0; i < pairs.size(); i++) {
                final Pair<String, Integer> pair = pairs.get(i);
                writer.print(pair.getKey() + "\t" + pair.getValue());

                if (i != pairs.size() - 1) {
                    writer.print(System.lineSeparator());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<WordData> readWordDatasFromFile(final File file) {

        if (file.exists()) {
            final List<WordData> wordDatas = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                final StringBuilder builder = new StringBuilder();

                while (!builder.append(reader.readLine()).toString().equals("null")) {
                    final String[] strings = builder.toString().split(SEPARATOR_FOR_FILE);
                    wordDatas.add(new WordData(

                            strings[WORD],
                            tagStringToList(strings[TAG_WORD]),
                            Integer.parseInt(strings[COUNT]),
                            stringToList(strings[WORD_LEMMA]),
                            stringToList(strings[TAG_LEMMA])
                    ));
                    builder.delete(0, builder.length());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return wordDatas;
        }

        return new ArrayList<>();
    }

    public static void writeWordDatasToFile(final List<WordData> wordDatas, final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {

            for (int i = 0; i < wordDatas.size(); i++) {
                final WordData wordData = wordDatas.get(i);
                writer.print(wordData.toString());

                if (i != wordDatas.size() - 1) {
                    writer.print(System.lineSeparator());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String readTextFromFile(final File file) {
        StringBuilder temporaryString = new StringBuilder();
        StringBuilder text = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            while (!temporaryString.append(reader.readLine()).toString().equals("null")) {
                text.append(temporaryString.toString());
                temporaryString.delete(0, temporaryString.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return text.toString();
    }

    public static void writeTextToFile(final String text, final File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
