class RedeclaredMethod {
    public static void main(String[] args) {
    }
}

class RedeclaredMethodTest {
    public int foo(int a, long[] b, boolean c) {
        return 2;
    }
    // Redeclaring method.
    public long foo(int d, long[] e, boolean f) {
        return 0;
    }
}
