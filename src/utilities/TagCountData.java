package utilities;

import static utilities.Constants.SEPARATOR_FOR_TAGS;

public class TagCountData implements Comparable<TagCountData> {

    private String name;
    private int count;

    public TagCountData(final String name) {
        this.name = name;
        this.count = 0;
    }

    public TagCountData(final String name, final int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void inc() {
        count++;
    }

    public void dec() {
        count--;
    }

    @Override
    public int compareTo(TagCountData o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name + SEPARATOR_FOR_TAGS + count;
    }
}
