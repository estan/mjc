/**
 * Tests the disjoint-set datastructure declared below on the two sample
 * inputs from the Kattis "unionfind" problem at [1].
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 *
 * [1] https://kth.kattis.com/problems/unionfind
 */
class DisjointSets {
    public static void main(String[] args) {
        DisjointSet s1;
        DisjointSet s2;

        int r; // Unused.

        // First test case (unionfind1.in on Kattis).
        s1 = new DisjointSet();
        r = s1.init(10);
        r = s1.same(1, 3);
        r = s1.union(1, 8);
        r = s1.union(3, 8);
        r = s1.same(1, 3);

        // Second test case (unionfind2.in on Kattis).
        s2 = new DisjointSet();
        r = s2.init(4);
        r = s2.same(0, 0);
        r = s2.union(0, 1);
        r = s2.union(1, 2);
        r = s2.union(0, 2);
        r = s2.same(0, 3);
    }
}

/**
 * Disjoint-set data structure for non-negative integer elements.
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 *
 * [1] https://en.wikipedia.org/wiki/Disjoint-set_data_structure
 */
class DisjointSet {
    int[] parent;
    int[] rank;

    // Initialize the sets { {0}, {1}, ..., {N - 1} }.
    public int init(int N) {
        int i;
        parent = new int[N];
        rank = new int[N];
        i = 0;
        while (i < N) {
            parent[i] = i;
            i = i + 1;
        }
        return 0;
    }

    // Unions the subsets containing x and y.
    public int union(int x, int y) {
        int xRoot;
        int yRoot;
        xRoot = this.find(x);
        yRoot = this.find(y);

        if (!(!(xRoot < yRoot) && !(yRoot < xRoot))) { // xRoot != yRoot
            if (rank[xRoot] < rank[yRoot]) {
                parent[xRoot] = yRoot;
            } else {
                if (rank[yRoot] < rank[xRoot]) {
                    parent[yRoot] = xRoot;
                } else {
                    parent[yRoot] = xRoot;
                    rank[xRoot] = rank[xRoot] + 1;
                }
            }
        } else {
            // x and y already in same set.
        }

        return 0;
    }

    // Returns the representative for the subset containing x.
    public int find(int x) {
        int root;
        int next; // Used as temporary in second loop.

        // Find root.
        root = x;
        while (!(!(root < parent[root]) && !(parent[root] < root))) { // root != parent[root]
            root = parent[root];
        }

        // Re-parent all elements on path to root to root.
        while (!(!(x < root) && !(root < x))) { // x != root
            next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    // Prints 1 if x and y is in the same subset, otherwise 0.
    public int same(int x, int y) {
        int xRoot;
        int yRoot;
        xRoot = this.find(x);
        yRoot = this.find(y);

        if (!(xRoot < yRoot) && !(yRoot < xRoot)) { // xRoot == yRoot
            System.out.println(1);
        } else {
            System.out.println(0);
        }
        return 0;
    }
}
