class SideEffectsAndEvalOrder {
    public static void main(String[] args) {
        IncredibleMachine m;
        m = new IncredibleMachine();
        System.out.println(m.run());
    }
}

class IncredibleMachine {
    int result;
    public int run() {
        result = 2;
        if (a(3, false) && a(5, true)                        /* r = 2 * 3 */
            || (a(7, false) || a(11, true) && a(13, false))  /* r = 2 * 3 * 7 * 11 * 13 */
            || a(17, true) || a(19, true))                   /* r = 2 * 3 * 7 * 11 * 13 * 17 = 102102 */
            result = result;
        else {}
        return result;
    }
    public boolean a(int value, boolean ret) {
        result = result * value;
        return ret;
    }
}
