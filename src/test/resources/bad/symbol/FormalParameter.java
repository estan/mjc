class FormalParameterTest {
    public static void main(String[] args) {
    }
}

class A {
    public int f(int b, long b /* DUPLICATE_PARAMETER */, long[] c, boolean d) {
        return 42;
    }
    public int g(B b) { // UNDECLARED_CLASS
        return 42;
    }
}
