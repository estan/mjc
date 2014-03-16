class PrintlnStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        //System.out.println(i); // OK!
        System.out.println(ia); // INVALID_PRINTLN_TYPE
        //System.out.println(l); // OK!
        System.out.println(la); // INVALID_PRINTLN_TYPE
        System.out.println(b); // INVALID_PRINTLN_TYPE
        System.out.println(cA); // INVALID_PRINTLN_TYPE
        System.out.println(cB); // INVALID_PRINTLN_TYPE
    }
}
class A {}
class B {}
