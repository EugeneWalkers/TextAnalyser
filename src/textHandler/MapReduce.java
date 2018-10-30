package textHandler;

import java.io.File;
import java.io.IOException;

public class MapReduce {


    public final static String MAP = "map.exe";
    public final static String REDUCE = "reduce.exe";
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final String regex;

    {
        regex = "\\W";
    }

    public void runCommand(final String CommandId, final File input, final File output) {

        ProcessBuilder bd = null;

        switch (CommandId) {
            default:
            case MAP:
                bd = new ProcessBuilder(CommandId);
                break;
            case REDUCE:
                bd = new ProcessBuilder(CommandId);
                break;
        }


        bd.redirectInput(input);
        bd.redirectOutput(output);

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

}
