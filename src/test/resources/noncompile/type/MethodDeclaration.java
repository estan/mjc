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
    B cB;

    //public int f1() { return i; } // OK!
    public int f2() { return ia; } // INVALID_RETURN_TYPE
    public int f3() { return b; } // INVALID_RETURN_TYPE
    public int f4() { return cA; } // INVALID_RETURN_TYPE
    public int f5() { return cB; } // INVALID_RETURN_TYPE

    public int[] f6() { return i; } // INVALID_RETURN_TYPE
    //public int[] f7() { return ia; } // OK!
    public int[] f8() { return b; } // INVALID_RETURN_TYPE
    public int[] f9() { return cA; } // INVALID_RETURN_TYPE
    public int[] f10() { return cB; } // INVALID_RETURN_TYPE

    public boolean f11() { return i; } // INVALID_RETURN_TYPE
    public boolean f12() { return ia; } // INVALID_RETURN_TYPE
    //public boolean f13() { return b; } // OK!
    public boolean f14() { return cA; } // INVALID_RETURN_TYPE
    public boolean f15() { return cB; } // INVALID_RETURN_TYPE

    public A f16() { return i; } // INVALID_RETURN_TYPE
    public A f17() { return ia; } // INVALID_RETURN_TYPE
    public A f18() { return b; } // INVALID_RETURN_TYPE
    //public A f19() { return cA; } // OK!
    public A f20() { return cB; } // INVALID_RETURN_TYPE
}
