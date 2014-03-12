class UndeclaredLocalVariableType {
    public static void main(String[] args) {
    }
}

class UndeclaredLocalVariableTypeTest {
    public int foo(int[] d, int b) {
        Bar f; // Undeclared local variable type.
        return 2;
    }
}
