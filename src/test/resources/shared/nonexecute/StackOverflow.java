/**
 * This test simply overflows the stack through recursion.
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 */
class StackOverflow {
    public static void main(String[] args) {
        Overflower o;
        int r;
        o = new Overflower();
        r = o.overflow();
    }
}

class Overflower {
    public int overflow() {
        int a;
        int b;
        int c;
        return this.overflow();
    }
}
