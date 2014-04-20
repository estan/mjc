/**
 * Check that an attempt to call main method is not accepted.
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 */
class CallToMain {
    public static void main(String[] args) {
        CallToMain c;
        int r;
        r = c.main(args);
    }
}
