class MethodInvocationExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;

        i = cA.f_i(i); // OK!
        i = cA.f_ia(ia); // OK!
        i = cA.f_b(b); // OK!
        i = cA.f_A(cA); // OK!
        i = cA.f_all(i, ia, b, cA); // OK!
    }
}
class A {
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
    public int f_all(int a, int[] b, boolean c, A d) {
        return 1;
    }
}
