class RedeclaredParameter {
    public static void main(String[] args) {
    }
}

class RedeclaredParameterTest {
    public int foo(int b, long b /* Redeclared */, long[] c, boolean d) {
        int a;
        return 2;
    }
}
