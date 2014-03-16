class IfStatementTest {
    public static void main(String[] args) {
        boolean b;

        if (b) {
            b = false;
            if (b)
                b = true;
        }
    }
}
