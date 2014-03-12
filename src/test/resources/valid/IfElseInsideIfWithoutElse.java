/**
 * Test if with else nested inside if without else, and also
 * precedence of logical OR and AND.
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 */
class IfElseInsideIfWithoutElse {
    public static void main(String[] arg) {
        int a;
        int b;
        int c;
        boolean A;
        boolean B;
        boolean C;
        A = true;
        B = true;
        C = false;

        a = 33;
        b = 5;
        c = 6;
        if (A)
            if (C || A && B)
                a = 1;
            else if (B && A)
                b = 2;
            else if (C)
                b = 3;
            else
                c = 3;

        // Expected is a = 1, b = 5, c = 6.
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }
}
