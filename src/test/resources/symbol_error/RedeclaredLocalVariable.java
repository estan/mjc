class RedeclaredLocalVariable {
    public static void main(String[] args) {
    }
}

class RedeclaredLocalVariableTest {
    public int foo(int b, long[] c, boolean d) {
        int a;
        int b; // Redeclared local variable.
        return 2;
    }
}
