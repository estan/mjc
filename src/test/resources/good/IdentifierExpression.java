class IdentifierExpressionTest {
    public static void main(String[] args) {
    }
}

class A {
    int r;
    int a;
    public int f(int b) {
        int c;
        {
            int d;
            {
                int e;
                r = a; // OK!
                r = b; // OK!
                r = c; // OK!
                r = d; // OK!
                r = e; // OK!
            }
            r = a; // OK!
            r = b; // OK!
            r = c; // OK!
            r = d; // OK!
        }
        r = a; // OK!
        r = b; // OK!
        r = c; // OK!
        return r;
    }
}
