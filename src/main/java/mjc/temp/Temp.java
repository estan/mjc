package mjc.temp;

public class Temp {
    private static int count;
    private final int num;

    public Temp() {
        num = count++;
    }

    @Override
    public String toString() {
        return "t" + num;
    }
}
