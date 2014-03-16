class Fibonacci {
    public static void main(String[] args) {
        System.out.println(new F().Start(10));
    }
}

class F {
    public int Start(int n) {
        int a;
        int b;
        int temp;
        a = 1;
        b = 1;
        while (0 < n) {
            System.out.println(b);
            temp = a;
            a = a + b;
            b = temp;
            n = n - 1;
        }
        return n;
    }
}
