class AlmostRedeclaredMethod {
    public static void main(String[] args) {
    }
}

class AlmostRedeclaredMethodTest {
    public int foo(int[] d, long[] b, boolean c) {
        return 2;
    }
    // Almost redeclaring, only one type differs.
    public int foo(int d, long[] e, boolean f) {
        return 0;
    }
}
