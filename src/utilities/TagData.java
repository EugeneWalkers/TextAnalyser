package utilities;

import java.awt.*;

public class TagData {
    private final String name;
    private final Color color;

    public TagData(final String name, final Color color) {
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
