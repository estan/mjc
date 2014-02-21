/**
   Computes the sum of all integers from 1 up to 100.
   @author Lars Engebretsen
*/
class Accumulator
{
    public static void main(String[] a){
	System.out.println(new Sum().sum1to(100));
    }
}

class Sum {
    Acc a;

    public int sum1to(int n) {
	int i;
	int t;
	i = 1;
	a = new Acc();
	t = a.init();
	while(i < n + 1) {
	    t = a.add(i);
	    i = i + 1;
	}
	return a.sum();
    }
}

class Acc {
    int sum;

    public int init() {
	sum = 0;
	return 0;
    }

    public int add(int n) {
	sum = sum + n;
	return 0;
    }

    public int sum() {
	return sum;
    }
}
