/**
 * Tests accessing an array out-of-bounds.
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 */

//EXT:ABC

class ArrayOutOfBounds {
    public static void main(String[] args) {
        int i;
        int[] a;
        a = new int[4];
        i = a[4];
    }
}
