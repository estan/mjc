class MethodInvocationExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        B cB;

        //i = cB.f_i(i); // OK!
        i = cB.f_i(ia); // INVALID_PARAM_TYPE
        i = cB.f_i(b); // INVALID_PARAM_TYPE
        i = cB.f_i(cA); // INVALID_PARAM_TYPE
        i = cB.f_i(cB); // INVALID_PARAM_TYPE

        i = cB.f_ia(i); // INVALID_PARAM_TYPE
        //i = cB.f_ia(ia); // OK!
        i = cB.f_ia(b); // INVALID_PARAM_TYPE
        i = cB.f_ia(cA); // INVALID_PARAM_TYPE
        i = cB.f_ia(cB); // INVALID_PARAM_TYPE

        i = cB.f_b(i); // INVALID_PARAM_TYPE
        i = cB.f_b(ia); // INVALID_PARAM_TYPE
        //i = cB.f_b(b); // OK!
        i = cB.f_b(cA); // INVALID_PARAM_TYPE
        i = cB.f_b(cB); // INVALID_PARAM_TYPE

        i = cB.f_A(i); // INVALID_PARAM_TYPE
        i = cB.f_A(ia); // INVALID_PARAM_TYPE
        i = cB.f_A(b); // INVALID_PARAM_TYPE
        //i = cB.f_A(cA); // OK!
        i = cB.f_A(cB); // INVALID_PARAM_TYPE

        i = cB.f_all(i, ia, b, ia); // INVALID_PARAM_TYPE
        i = cB.f_all(i, ia, b, cA, i); // INVALID_PARAM_COUNT
        i = cB.f_all(i, ia, b); // INVALID_PARAM_COUNT
        i = cB.u(); // UNDECLARED_METHOD
        i = this.main(args); // INVALID_PARAM_TYPE + INVALID_ASSIGNMENT (yes!)
        i = i.f(); // CALL_ON_NON_CLASS
        i = ia.f(); // CALL_ON_NON_CLASS
        i = b.f(); // CALL_ON_NON_CLASS
    }
}
class A {}
class B {
    public int f_i(int a) {
        return 1;
    }
    public int f_ia(int[] a) {
        return 1;
    }
    public int f_b(boolean a) {
        return 1;
    }
    public int f_A(A a) {
        return 1;
    }
    public int f_all(int a, int[] b, boolean e, A f) {
        return 1;
    }
}
