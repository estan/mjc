/**
 * Testing nested block declarations.
 *
 * The program should print the numbers from the TV series Lost:
 *
 * 4
 * 8
 * 15
 * 16
 * 23
 * 42
 *
 * @author Elvis Stansvik
 */

//EXT:NBD

class NestedBlocks {
    public static void main(String[] args) {
        int r;
        Lost lost;
        lost = new Lost();
        r = lost.printNumbers();
    }
}

class Lost {
    public int printNumbers() {
        int l;
        int k;
        {
            int n;
            n = 1;
        }
        {
            int n;
            n = 8;
            {
                int m;
                m = 4;
                {
                    System.out.println(m);
                }
            }
            System.out.println(n);
            {
                int m;
                m = 15;
                {
                    System.out.println(m);
                }
            }
        }
        k = 16;
        {
            int n;
            n = 23;
            {
                System.out.println(k);
            }
            System.out.println(n);
        }
        {
            l = 42;
        }
        System.out.println(l);
        return 0;
    }
}
