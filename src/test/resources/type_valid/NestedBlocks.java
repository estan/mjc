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
        }
        return c;
    }
}
