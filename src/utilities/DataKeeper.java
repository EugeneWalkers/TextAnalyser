package utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class DataKeeper {

    public static List<Pair> readPairsFromFile(final File file) {

        if (file.exists()){
            List<Pair> pairs = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

                StringBuilder builder = new StringBuilder();
                while (!builder.append(reader.readLine()).toString().equals("null")) {
                    String[] strings = builder.toString().split("\t");
                    pairs.add(new Pair(strings[0], Integer.parseInt(strings[1])));
                    builder.delete(0, builder.length());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return pairs;
        }

        return new ArrayList<>();
    }

    public static void writePairsToFile(final List<Pair> pairs, final File file){
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try(PrintWriter writer = new PrintWriter(file)){
            for (int i = 0; i < pairs.size(); i++) {
                final Pair pair = pairs.get(i);
                writer.print(pair.toString());

                if (i != pairs.size() - 1){
                    writer.print(System.lineSeparator());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String readTextFromFile(File file) {
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

    public static void writeTextToFile(final String text, final File file){
        try(PrintWriter writer = new PrintWriter(file)){
            writer.print(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
