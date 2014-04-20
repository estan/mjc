/**
 * Check that return in main method is not accepted.
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 */
class ReturnInMainMethod {
    public static void main(String[] args) {
        int foo;
        return foo;
    }
}
