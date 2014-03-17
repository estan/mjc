class MethodInvocationExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;

        i = cA.f_i(i); // OK!
        i = cA.f_ia(ia); // OK!
        i = cA.f_l(i); // OK!
        i = cA.f_l(l); // OK!
        i = cA.f_la(la); // OK!
        i = cA.f_b(b); // OK!
        i = cA.f_A(cA); // OK!
        i = cA.f_all(i, ia, l, la, b, cA); // OK!
    }
}
class A {
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
