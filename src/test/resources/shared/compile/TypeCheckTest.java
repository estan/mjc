/**
 * This is an amalgamation of some of our type checker / parser tests.
 * It should cover most type-valid constructs of our extended language.
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 */

//EXT:IWE
//EXT:NBD
//EXT:CLE
//EXT:CGT
//EXT:CGE
//EXT:CEQ
//EXT:CNE
//EXT:BDJ

class TypeCheckTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        A cA1;
        A cA2;
        B cB;
        C cC;

        // Assignment + new instance/array.
        i = 42;
        ia = new int[42];
        b = true;
        b = false;
        b = b;
        cA = new A();
        cA1 = new A();
        cA2 = new A();
        cB = new B();
        cC = new C();

        // Operator +, - and *
        i = i + i;
        i = i - i;
        i = i * i;

        // Operator ==
        if (i == i) {}
        if (ia == ia) {}
        if (b == b) {}
        if (cA1 == cA2) {}

        // Operator !=
        if (i != i) {}
        if (ia != ia) {}
        if (b != b) {}
        if (cA != cA) {}

        // Operator <, <=, > and >=
        if (i < i) {}
        if (i <= i) {}
        if (i > i) {}
        if (i >= i) {}

        // Operator &&, || and !
        if (b && b) {}
        if (b || b) {}
        if (!b) {}
        if (!!b) {}

        // Array access, assignment and length.
        i = ia[i];
        ia[i] = i;
        i = ia.length;

        // if, if/else and while
        if (b) {
            b = false;
            if (b)
                b = true;
        }
        if (b) {
            b = false;
            b = true;
        } else {
            b = true;
            b = false;
        }
        if (b)
            b = false;
        else
            if (b)
                b = true;
            else
                b = false;
        while (b) {}

        // Integer literals
        i = 0;
        i = 42;
        i = 2147483647;

        // Method invocation
        i = cC.f_i(i);
        i = cC.f_ia(ia);
        i = cC.f_b(b);
        i = cC.f_A(cA);
        i = cC.f_all(i, ia, b, cA);

        // println
        System.out.println(i);
    }
}

class A {
    int r;
    int a;
    public int f(int b) {
        // Variable visibility
        int c;
        c = 0;
        {
            int d;
            d = 0;
            {
                int e;
                e = 0;
                r = a;
                r = b;
                r = c;
                r = d;
                r = e;
            }
            r = a;
            r = b;
            r = c;
            r = d;
        }
        r = a;
        r = b;
        r = c;
        return r;
    }
}

class B {
    // Check return type compatibility.
    public int f_i() { return 42; }
    public int[] f_ia() { return new int[42]; }
    public boolean f_b() { return true; }
    public A f_A() { return new A(); }
}

class C {
    // Methods invoked from TypeCheckTest.
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
