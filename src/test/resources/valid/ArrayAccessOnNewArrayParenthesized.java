/**
 * Check that we accept "int foo; foo = (new int[3])[3]".
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 */
class ArrayAccessOnNewArrayParenthesized {
    public static void main(String[] args) {
        // It is okay to do an array access on a newly created array
        // if the creation expression is parenthesized.
        int foo; foo = (new int[3])[3];
    }
}
