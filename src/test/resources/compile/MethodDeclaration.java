class MethodDeclarationTest {
    public static void main(String[] args) {
    }
}

class A {}

class B {
    int i;
    int[] ia;
    boolean b;
    A cA;

    public int f_i() { return i; } // OK!
    public int[] f_ia() { return ia; } // OK!
    public boolean f_b() { return b; } // OK!
    public A f_A() { return cA; } // OK!
}
