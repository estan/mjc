class IdentifierExpressionTest {
    public static void main(String[] args) {
        int a;
        a = args; // UNDECLARED_IDENTIFIER (yes!)
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
                int e1;
                int e2;
                //r = a; // OK!
                //r = b; // OK!
                //r = c; // OK!
                //r = d; // OK!
                //r = e1; // OK!
                //r = e2; // OK!
            }
            //r = a; // OK!
            //r = b; // OK!
            //r = c; // OK!
            //r = d; // OK!
            r = e1; // UNDECLARED_IDENTIFIER
        }
        //r = a; // OK!
        //r = b; // OK!
        //r = c; // OK!
        r = d; // UNDECLARED_IDENTIFIER
        r = e2; // UNDECLARED_IDENTIFIER
        r = U;  // UNDECLARED_IDENTIFIER
        r = A;  // EXPECTED_VARIABLE_GOT_CLASS
        return r;
    }
}
