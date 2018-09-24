public class Pair {
    private String word;
    private int number;

    public Pair(String word) {
        this.word = word;
        number = 1;
    }

    public Pair(String word, int number) {
        this.word = word;
        this.number = number;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return word + "\t\t" + number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Pair pair = (Pair) obj;

        return pair.number == this.number && pair.word.equals(this.getWord());
    }
}
