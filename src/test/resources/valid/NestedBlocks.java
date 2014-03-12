class NestedBlocks {
    public static void main(String[] args) {
    }
}

class NestedBlocksTest {
    public int foo(int a, int b) {
        // Some nested blocks.
        int c;
        {
            int d;
            {
                int e;
            }
            if (true) {
                int g;
            }
            while (false) {
                long g; // OK: The other g is not in scope.
            }
        }
        return c;
    }
}
