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
         * It has no semantic meaning (no multidimensional arrays).
         */
        int foo; foo = new int[3][3];
    }
}
