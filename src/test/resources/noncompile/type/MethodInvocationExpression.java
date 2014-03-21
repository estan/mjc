class MethodInvocationExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        //i = cB.f_i(i); // OK!
        i = cB.f_i(ia); // INVALID_PARAM_TYPE
        i = cB.f_i(l); // INVALID_PARAM_TYPE
        i = cB.f_i(la); // INVALID_PARAM_TYPE
        i = cB.f_i(b); // INVALID_PARAM_TYPE
        i = cB.f_i(cA); // INVALID_PARAM_TYPE
        i = cB.f_i(cB); // INVALID_PARAM_TYPE

        i = cB.f_ia(i); // INVALID_PARAM_TYPE
        //i = cB.f_ia(ia); // OK!
        i = cB.f_ia(l); // INVALID_PARAM_TYPE
        i = cB.f_ia(la); // INVALID_PARAM_TYPE
        i = cB.f_ia(b); // INVALID_PARAM_TYPE
        i = cB.f_ia(cA); // INVALID_PARAM_TYPE
        i = cB.f_ia(cB); // INVALID_PARAM_TYPE

        //i = cB.f_l(i); // OK!
        i = cB.f_l(ia); // INVALID_PARAM_TYPE
        //i = cB.f_l(l); // OK!
        i = cB.f_l(la); // INVALID_PARAM_TYPE
        i = cB.f_l(b); // INVALID_PARAM_TYPE
        i = cB.f_l(cA); // INVALID_PARAM_TYPE
        i = cB.f_l(cB); // INVALID_PARAM_TYPE

        i = cB.f_la(i); // INVALID_PARAM_TYPE
        i = cB.f_la(ia); // INVALID_PARAM_TYPE
        i = cB.f_la(l); // INVALID_PARAM_TYPE
        //i = cB.f_la(la); // OK!
        i = cB.f_la(b); // INVALID_PARAM_TYPE
        i = cB.f_la(cA); // INVALID_PARAM_TYPE
        i = cB.f_la(cB); // INVALID_PARAM_TYPE

        i = cB.f_b(i); // INVALID_PARAM_TYPE
        i = cB.f_b(ia); // INVALID_PARAM_TYPE
        i = cB.f_b(l); // INVALID_PARAM_TYPE
        i = cB.f_b(la); // INVALID_PARAM_TYPE
        //i = cB.f_b(b); // OK!
        i = cB.f_b(cA); // INVALID_PARAM_TYPE
        i = cB.f_b(cB); // INVALID_PARAM_TYPE

        i = cB.f_A(i); // INVALID_PARAM_TYPE
        i = cB.f_A(ia); // INVALID_PARAM_TYPE
        i = cB.f_A(l); // INVALID_PARAM_TYPE
        i = cB.f_A(la); // INVALID_PARAM_TYPE
        i = cB.f_A(b); // INVALID_PARAM_TYPE
        //i = cB.f_A(cA); // OK!
        i = cB.f_A(cB); // INVALID_PARAM_TYPE

        i = cB.f_all(i, ia, ia, la, b, cA); // INVALID_PARAM_TYPE
        i = cB.f_all(i, ia, l, la, b, cA, i); // INVALID_PARAM_COUNT
        i = cB.f_all(i, ia, l, la, b); // INVALID_PARAM_COUNT
        i = cB.u(); // UNDECLARED_METHOD
        i = this.main(args); // INVALID_PARAM_TYPE + INVALID_ASSIGNMENT (yes!)
        i = i.f(); // CALL_ON_NON_CLASS
        i = ia.f(); // CALL_ON_NON_CLASS
        i = l.f(); // CALL_ON_NON_CLASS
        i = la.f(); // CALL_ON_NON_CLASS
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
    public int f_l(long a) {
        return 1;
    }
    public int f_la(long[] a) {
        return 1;
    }
    public int f_b(boolean a) {
        return 1;
    }
    public int f_A(A a) {
        return 1;
    }
    public int f_all(int a, int[] b, long c, long[] d, boolean e, A f) {
        return 1;
    }
}
