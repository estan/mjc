class TypeCheckTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        A cA1;
        A cA2;
        B cB;
        C cC;

        // Assignment + new instance/array.
        i = 42;
        ia = new int[42];
        l = 42;
        l = 42L;
        la = new long[42];
        b = true;
        cA = new A();
        cA1 = new A();
        cA2 = new A();
        cB = new B();
        cC = new C();

        // Operator +
        i = i + i;
        l = i + l;
        l = l + i;
        l = l + l;

        // Operator -
        i = i - i;
        l = l - i;
        l = l - l;

        // Operator *
        i = i * i;
        l = i * l;
        l = l * i;
        l = l * l;

        // Operator ==
        if (i == i) {}
        if (i == l) {}
        if (ia == ia) {}
        if (l == i) {}
        if (l == l) {}
        if (la == la) {}
        if (b == b) {}
        if (cA1 == cA2) {}

        // Operator !=
        if (i != i) {}
        if (i != l) {}
        if (ia != ia) {}
        if (l != i) {}
        if (l != l) {}
        if (la != la) {}
        if (b != b) {}
        if (cA != cA) {}

        // Operator <
        if (i < i) {}
        if (i < l) {}
        if (l < i) {}
        if (l < l) {}

        // Operator <=
        if (i <= i) {}
        if (i <= l) {}
        if (l <= i) {}
        if (l <= l) {}

        // Operator >=
        if (i >= i) {}
        if (i >= l) {}
        if (l >= i) {}
        if (l >= l) {}

        // Operator >
        if (i > i) {}
        if (i > l) {}
        if (l > i) {}
        if (l > l) {}

        // Operator &&, || and !
        if (b && b) {}
        if (b || b) {}
        if (!b) {}

        // Array access
        i = ia[i];
        l = ia[i];
        l = la[i];

        // Array assignment
        ia[i] = i;
        la[i] = l;
        la[i] = i;

        // Array length
        i = ia.length;
        i = la.length;

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
        l = 0;
        l = 42;
        l = 9223372036854775807l;
        l = 9223372036854775807L;

        // Method invocation
        i = cC.f_i(i);
        i = cC.f_ia(ia);
        i = cC.f_l(i);
        i = cC.f_l(l);
        i = cC.f_la(la);
        i = cC.f_b(b);
        i = cC.f_A(cA);
        i = cC.f_all(i, ia, l, la, b, cA);

        // println
        System.out.println(i);
        System.out.println(l);
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
    public int f1() { return 42; }
    public int[] f2() { return new int[42]; }
    public long f3() { return 42; }
    public long f4() { return 42L; }
    public long[] f5() { return new long[42]; }
    public boolean f6() { return true; }
    public A f7() { return new A(); }
}

class C {
    // Methods invoked from TypeCheckTest.
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
