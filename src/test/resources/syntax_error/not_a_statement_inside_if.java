class NotAStatementInsideIf {
    public static void main(String[] args) {
    }
}

class NotAStatementInsideIfTest {
    public int foo(int a, int b) {
        if (true)
            int g; // Not okay without { }. int g; on its own is not a statement.
        return 2;
    }
}
