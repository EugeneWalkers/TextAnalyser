import java.io.*;
import java.util.LinkedList;

class MapReduce {

    private final static String defaultNameOfTheFile = "input.txt";
    private final static String outMapString = "output/outMap.txt";
    private final static String outSortString = "output/outSort.txt";
    private final static String outReduceString = "output/output.txt";
    private final String regex;
    private final MapReduceComparator comparator;
    private final FrequencyComparator frequencyComparator;
    private final LinkedList<Pair> pairs;
    private final File outMap;
    private final File outSort;
    private final File outReduce;
    private StringBuilder text;
    private File file;
    private boolean isFileDefault;

    {
        text = new StringBuilder();
        regex = "\\W";
        pairs = new LinkedList<>();
        comparator = new MapReduceComparator();
        frequencyComparator = new FrequencyComparator();
        outMap = new File(outMapString);
        outSort = new File(outSortString);
        outReduce = new File(outReduceString);
        isFileDefault = false;
    }

    MapReduce() {

    }

    //unused

    MapReduce(File file) {
        this.file = file;
    }

    public StringBuilder getText() {
        return text;
    }

    void setText(String text) {
        this.text.setLength(0);
        this.text.append(text);
    }

    /**
     * Used for first version of Map to get text in StringBuilder
     *
     * @param file readed file
     * @author Eugene_Walkers
     */
    private void readTextFromFile(File file) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while (!builder.append(reader.readLine()).toString().equals("null")) {
                text.append(builder.toString());
                builder.delete(0, builder.length());
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
    }

    private void clearPairs() {
        while (pairs.size() > 0) {
            pairs.remove();
        }
    }

    private void setPairsFromFile(File file) {
        clearPairs();
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
    }

    void setFile(File file) {
        this.file = file;
    }

    private void map() {
//        if (file != null) {
//            readTextFromFile(file);
//        }
//        String[] words = text.toString().split(regex);
//        PrintWriter writer = null;
//        try {
//            writer =
//                    new PrintWriter(outMap);
//            for (int i = 0; i < words.length; i++) {
//                writer.println(new Pair(words[i].toLowerCase()).toString());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (writer != null) {
//                writer.close();
//            }
//        }

        ProcessBuilder bd = new ProcessBuilder("map.exe");
        bd.redirectInput(file);
        bd.redirectOutput(outMap);
        Process process = null;
        try {
            process = bd.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (process != null) {
                process.waitFor();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (process != null) {
            process.destroy();
        }
    }

    private void initFile() {
        file = new File(defaultNameOfTheFile);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(text.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writePairsToFile(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (int i = 0; i < pairs.size(); i++) {
                writer.print(pairs.get(i).toString());
                if (i != pairs.size() - 1) {
                    writer.print(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sort(boolean isUp) {
        comparator.setUp(isUp);
        setPairsFromFile(outMap);
        pairs.sort(comparator);
        writePairsToFile(outSort);
    }

    private void sortByFrequency(boolean isUp) {
        frequencyComparator.setUp(isUp);
        setPairsFromFile(outReduce);
        pairs.sort(frequencyComparator);
        writePairsToFile(outReduce);
    }

    private void reduce() {

        ProcessBuilder bd = new ProcessBuilder("reduce.exe");
        bd.redirectInput(outSort);
        bd.redirectOutput(outReduce);
        Process process = null;
        try {
            process = bd.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (process != null) {
                process.waitFor();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (process != null) {
            process.destroy();
        }

//        setPairsFromFile(outSort);
//        for (int i = 0; i < pairs.size() - 1; i++) {
//            if (pairs.get(i).getWord().equals(pairs.get(i + 1).getWord())) {
//                pairs.get(i).setNumber(pairs.get(i).getNumber() + 1);
//                pairs.remove(i + 1);
//                i--;
//            }
//        }
//        writePairsToFile(outReduce);
    }

    public void handle(boolean isByName, boolean isUp) {
        if (file == null) {
            isFileDefault = true;
            initFile();
        }

        map();
        sort(isUp);
        reduce();

        if (!isByName) {
            sortByFrequency(isUp);
        }

        cleanFiles();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void cleanFiles() {
        outMap.delete();
        outSort.delete();
        if (isFileDefault){
            file.delete();
        }
    }

    public LinkedList<Pair> getPairs() {
        return pairs;
    }

}
