/**
 * This is an implementation of the Bron-Kerbosch algorithm for
 * finding maximal cliques in a graph. It makes use of our fully
 * extended language.
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 */

//EXT:IWE
//EXT:NBD
//EXT:CLE
//EXT:CGT
//EXT:CGE
//EXT:CEQ
//EXT:CNE
//EXT:BDJ

class FullLanguageBronKerbosch {
    public static void main(String[] args) {
        /*
         * Test the algorithm on:
         *
         *     1
         *    / \
         *   0---2
         *       |
         *   4---3
         *
         * which has maximal cliques {0,1,2}, {2,3} and {3,4}.
         */
        Graph g;
        boolean r; // Unused
        g = new Graph();
        r = g.init(5);
        r = g.addEdge(0, 1);
        r = g.addEdge(0, 2);
        r = g.addEdge(2, 3);
        r = g.addEdge(1, 2);
        r = g.addEdge(3, 4);
        r = g.printMaxCliques();
    }
}

// Very basic graph class.
class Graph {
    int[] edges;
    int size;

    Set set; // Set utility methods.

    // Initialize the graph with N vertices.
    public boolean init(int N) {
        int i;
        size = N;
        set = new Set();

        edges = new int[size * size];
        i = 0;
        while (i <= N * N - 1) { // CLE.
            edges[i] = 0;
            i = i + 1;
        }

        return true;
    }

    // Add an edge between u and v.
    public boolean addEdge(int u, int v) {
        edges[u * size + v] = 1;
        edges[v * size + u] = 1;
        return true;
    }

    // Prints each maximal clique followed by 666.
    public boolean printMaxCliques() {
        int[] R;
        int[] P;
        int[] X;
        int i;
        boolean r;
        R = new int[size];
        P = new int[size];
        X = new int[size];
        i = 0;
        while (R.length > i) { // CGT
            R[i] = 0;
            P[i] = 1;
            X[i] = 0;
            i = i + 1;
        }
        r = this.bronKerbosch(R, P, X);
        return true;
    }

    /*
     * The Bron-Kerbosch algorithm.
     *
     * Prints each maximal clique followed by 666.
     *
     * R is the current clique, P are candidates vertices for inclusion into R,
     * X are the forbidden (already evaluated vertices).
     */
    public boolean bronKerbosch(int[] R, int[] P, int[] X) {
        int v;

        if (set.isEmpty(P) && set.isEmpty(X)) { // IWE
            // R is a maximal clique.
            boolean r; // NBD
            r = set.print(R);
            System.out.println(666);
        }

        // For each vertex v in P.
        v = 0;
        while (v < P.length) {
            if (P[v] == 1) { // CEQ
                boolean r;
                r = this.bronKerbosch(
                        set.add(R, v),
                        set.intersect(P, edges, v * size),
                        set.intersect(X, edges, v * size));
                P = set.del(P, v);
                X = set.add(X, v);
            }
            v = v + 1;
        }

        return true;
    }

}

/*
 * Some (slow) methods for treating arrays as sets.
 *
 * E.g. a[i] = 1 ==> i is in set a, a[i] = 0 ==> i is not in set a.
 */
class Set {
    // Return a copy of s with e added.
    public int[] add(int[] s, int e) {
        int[] result;
        int i;
        result = new int[s.length];
        i = 0;
        while (result.length - 1 >= i) { // CGE
            if (s[i] == 1)
                result[i] = 1;
            i = i + 1;
        }
        result[e] = 1;
        return result;
    }

    // Return a copy of s with e removed.
    public int[] del(int[] s, int e) {
        int[] result;
        int i;
        result = new int[s.length];
        i = 0;
        while (i < result.length) {
            if (s[i] == 1)
                result[i] = 1;
            i = i + 1;
        }
        result[e] = 0;
        return result;
    }

    // Return the union of a and b[bOffset..bOffset + a.length - 1]
    public int[] union(int[] a, int[] b, int bOffset) {
        int[] result;
        int i;
        result = new int[a.length];
        i = 0;
        while (i < result.length) {
            if (a[i] == 1 || b[i + bOffset] == 1) // BDJ
                result[i] = 1;
            else
                result[i] = 0;
            i = i + 1;
        }
        return result;
    }

    // Return the intersection of a and b[bOffset..bOffset + a.length - 1]
    public int[] intersect(int[] a, int[] b, int bOffset) {
        int[] result;
        int i;
        result = new int[a.length];
        i = 0;
        while (i < result.length) {
            if (a[i] == 1 && b[i + bOffset] == 1)
                result[i] = 1;
            else
                result[i] = 0;
            i = i + 1;
        }
        return result;
    }

    // Returns true if a is an empty set.
    public boolean isEmpty(int[] a) {
        int i;
        boolean result;
        result = true;
        i = 0;
        while (i < a.length) {
            if (a[i] == 1)
                result = false;
            i = i + 1;
        }
        return result;
    }

    // Prints the elements of s.
    public boolean print(int[] s) {
        int i;
        i = 0;
        while (i < s.length) {
            if (s[i] != 0) // CNE
                System.out.println(i);
            i = i + 1;
        }
        return true;
    }
}

