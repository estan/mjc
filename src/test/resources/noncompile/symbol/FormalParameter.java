class FormalParameterTest {
    public static void main(String[] args) {
    }
}

class A {
    public int f(int b, int b /* DUPLICATE_PARAMETER */, int[] c, boolean d) {
        return 42;
    }
    public int g(B b) { // UNDECLARED_CLASS
        return 42;
    }
}
