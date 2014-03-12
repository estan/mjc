class RedeclaredField {
    public static void main(String[] args) {
    }
}

class RedeclaredFieldTest {
    int a;
    long a; // Redeclared field.
    public int foo(int b, long[] c, boolean d) {
        return 2;
    }
}
