class IfElseStatementTest {
    public static void main(String[] args) {
        boolean b;

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
    }
}
