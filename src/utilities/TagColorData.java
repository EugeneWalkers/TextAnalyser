package utilities;

import java.awt.*;

public class TagColorData {
    private final String name;
    private final Color color;

    public TagColorData(final String name, final Color color) {
        this.name = name;
        this.color = color;
    }

    public String getDescription() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
