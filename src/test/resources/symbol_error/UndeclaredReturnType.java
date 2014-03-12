class UndeclaredReturnType {
    public static void main(String[] args) {
    }
}

class UndeclaredReturnTypeTest {
    public Bar foo(int[] d, int b) { // Undeclared return type.
        return 2;
    }
}
