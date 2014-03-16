/**
 * Check duplicates in two sorted arrays.
 *
 * This is the "cd" problem on Kattis [1]
 *
 * @author Elvis Stansvik <elvstone@gmail.com>
 *
 * [1] https://kth.kattis.scrool.se/problems/cd
 */
class Duplicates {
    public static void main(String[] args) {
        int[] l1;
        int[] l2;
        Checker checker;

        l1 = new int[10];
        l2 = new int[10];

        l1[0] = 1;
        l1[1] = 2;
        l1[2] = 5;
        l1[3] = 7;
        l1[4] = 9;
        l1[5] = 20;
        l1[6] = 21;
        l1[7] = 55;
        l1[8] = 91;
        l1[9] = 204;

        l2[0] = 6;
        l2[1] = 7;
        l2[2] = 9;
        l2[3] = 42;
        l2[4] = 49;
        l2[5] = 91;
        l2[6] = 103;
        l2[7] = 304;
        l2[8] = 444;
        l2[9] = 552;

        checker = new Checker();

        System.out.println(checker.duplicates(l1, l2));
    }
}

/**
 * Checker is a duplicate checker.
 *
 * The duplicates method will return the number of elements that exists
 * in both of the given arrays.
 */
class Checker {
    public int duplicates(int[] l1, int[] l2) {
        int N;
        int M;

        int i;
        int j;
        int dupes;

        N = l1.length;
        M = l2.length;
        i = 0;
        j = 0;
        dupes = 0;
        while (i < N && j < M) {
            if (l1[i] < l2[j]) {
                i = i + 1;
            } else {
                if (l2[j] < l1[i]) {
                    j = j + 1;
                } else {
                    dupes = dupes + 1;
                    i = i + 1;
                    j = j + 1;
                }
            }
        }
        return dupes;
    }
}
