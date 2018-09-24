import java.util.Comparator;

public class FrequencyComparator implements Comparator<Pair> {

    private boolean isUp;

    public void setUp(boolean up) {
        isUp = up;
    }

    @Override
    public int compare(Pair o1, Pair o2) {
        return isUp ? o1.getNumber() - o2.getNumber() : o2.getNumber() - o1.getNumber();
    }
}
