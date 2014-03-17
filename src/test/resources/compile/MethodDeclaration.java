class MethodDeclarationTest {
    public static void main(String[] args) {
    }
}

class A {}

class B {
    int i;
    int[] ia;
    long l;
    long[] la;
    boolean b;
    A cA;

    public int f1() { return i; } // OK!
    public int[] f2() { return ia; } // OK!
    public long f3() { return i; } // OK!
    public long f4() { return l; } // OK!
    public long[] f5() { return la; } // OK!
    public boolean f6() { return b; } // OK!
    public A f7() { return cA; } // OK!
}
