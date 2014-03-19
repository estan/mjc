/**
 * Check that we reject "int foo; foo = new int[3][3]".
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 */
class ArrayAccessOnNewArray {
    public static void main(String[] args) {
        /*
         * In Java this is syntactically OK, but a type error.
         *
         * In MiniJava we should reject it as well, but for another reason:
         * The RHS has no semantic meaning (no multidimensional arrays).
         *
         * This tests that the grammar is set up such that this is not
         * accidently interpreted as an array access on a newly created
         * int[].
         */
        int foo; foo = new int[3][2];
    }
}
