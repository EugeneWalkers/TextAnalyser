import java.util.Comparator;

public class MapReduceComparator implements Comparator<Pair> {

    private boolean isUp;

    MapReduceComparator() {

    }

    public void setUp(boolean up) {
        isUp = up;
    }

    @Override
    public int compare(Pair o1, Pair o2) {
        return isUp ? o1.getWord().compareTo(o2.getWord()) : o2.getWord().compareTo(o1.getWord());
    }


}
