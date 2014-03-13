class RedeclaredMethod {
    public static void main(String[] args) {
    }
}

class RedeclaredMethodTest {
    public int foo(int a, long[] b, boolean c) {
        return 2;
    }
    // Redeclaring method.
    //
    // It's a redeclaration even if the parameters differ since MiniJava does
    // not support method overloading.
    public long foo(int d, long[] e, int f) {
        return 0;
    }
}
