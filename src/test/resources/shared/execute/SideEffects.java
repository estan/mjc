/**
 * Tests some evaluation order / side effects with && and ||.
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 */

//EXT:BDJ
//EXT:IWE

class SideEffects {
    public static void main(String[] args) {
        IncredibleMachine m;
        m = new IncredibleMachine();
        System.out.println(m.run()); // Should print 329.
    }
}

class IncredibleMachine {
    int result;
    public int run() {
        result = 2;
        if (this.a(3, false) && this.a(5, true)                             /* r = 2 + 3 */
            || (this.m(7, false) || this.s(11, true) && this.m(13, false))  /* r = ((2 + 3) * 7 - 11) * 13 */
            || this.a(17, true) || this.m(19, true))                        /* r = ((2 + 3) * 7 - 11) * 13 + 17 = 329 */
            result = result;
        return result;
    }
    public boolean m(int value, boolean ret) {
        result = result * value;
        return ret;
    }
    public boolean a(int value, boolean ret) {
        result = result + value;
        return ret;
    }
    public boolean s(int value, boolean ret) {
        result = result - value;
        return ret;
    }
}
