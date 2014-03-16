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
    B cB;

    //public int f1() { return i; } // OK!
    public int f2() { return ia; } // INVALID_RETURN_TYPE
    public int f3() { return l; } // INVALID_RETURN_TYPE
    public int f4() { return la; } // INVALID_RETURN_TYPE
    public int f5() { return b; } // INVALID_RETURN_TYPE
    public int f6() { return cA; } // INVALID_RETURN_TYPE
    public int f7() { return cB; } // INVALID_RETURN_TYPE

    public int[] f8() { return i; } // INVALID_RETURN_TYPE
    //public int[] f9() { return ia; } // OK!
    public int[] f10() { return l; } // INVALID_RETURN_TYPE
    public int[] f11() { return la; } // INVALID_RETURN_TYPE
    public int[] f12() { return b; } // INVALID_RETURN_TYPE
    public int[] f13() { return cA; } // INVALID_RETURN_TYPE
    public int[] f14() { return cB; } // INVALID_RETURN_TYPE

    //public long f15() { return i; } // OK!
    public long f16() { return ia; } // INVALID_RETURN_TYPE
    //public long f17() { return l; } // OK!
    public long f18() { return la; } // INVALID_RETURN_TYPE
    public long f19() { return b; } // INVALID_RETURN_TYPE
    public long f20() { return cA; } // INVALID_RETURN_TYPE
    public long f21() { return cB; } // INVALID_RETURN_TYPE

    public long[] f22() { return i; } // INVALID_RETURN_TYPE
    public long[] f23() { return ia; } // INVALID_RETURN_TYPE
    public long[] f24() { return l; } // INVALID_RETURN_TYPE
    //public long[] f25() { return la; } // OK!
    public long[] f26() { return b; } // INVALID_RETURN_TYPE
    public long[] f27() { return cA; } // INVALID_RETURN_TYPE
    public long[] f28() { return cB; } // INVALID_RETURN_TYPE

    public boolean f29() { return i; } // INVALID_RETURN_TYPE
    public boolean f30() { return ia; } // INVALID_RETURN_TYPE
    public boolean f31() { return l; } // INVALID_RETURN_TYPE
    public boolean f32() { return la; } // INVALID_RETURN_TYPE
    //public boolean f33() { return b; } // OK!
    public boolean f34() { return cA; } // INVALID_RETURN_TYPE
    public boolean f35() { return cB; } // INVALID_RETURN_TYPE

    public A f36() { return i; } // INVALID_RETURN_TYPE
    public A f37() { return ia; } // INVALID_RETURN_TYPE
    public A f38() { return l; } // INVALID_RETURN_TYPE
    public A f39() { return la; } // INVALID_RETURN_TYPE
    public A f40() { return b; } // INVALID_RETURN_TYPE
    //public A f41() { return cA; } // OK!
    public A f42() { return cB; } // INVALID_RETURN_TYPE
}
