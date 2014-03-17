/**
 * Tests accessing an array out-of-bounds.
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
