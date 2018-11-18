package utilities;

import java.awt.*;
import java.util.Random;

public final class ColorUtilities {

    private static final Random random = new Random();
    private static final int bound = 256;

    private ColorUtilities(){

    }

    public static Color getRandomColor(){
        final int red = random.nextInt(bound);
        final int green = random.nextInt(bound);
        final int blue = random.nextInt(bound);

        return new Color(red, green, blue);
    }
}
