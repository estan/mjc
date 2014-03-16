/**
 * Tests some evaluation order / side effects issues with && and ||.
 */

//EXT:IWE
//EXT:BDJ

class SideEffectsAndEvalOrder {
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
        if (a(3, false) && a(5, true)                        /* r = 2 + 3 */
            || (m(7, false) || s(11, true) && m(13, false))  /* r = ((2 + 3) * 7 - 11) * 13 */
            || a(17, true) || m(19, true))                   /* r = ((2 + 3) * 7 - 11) * 13 + 17 = 329 */
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
