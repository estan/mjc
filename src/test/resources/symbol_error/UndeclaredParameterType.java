class UndeclaredParameterType {
    public static void main(String[] args) {
    }
}

class UndeclaredParameterTypeTest {
    public int foo(int[] d, Bar b) { // Undeclared parameter type.
        return 2;
    }
}
