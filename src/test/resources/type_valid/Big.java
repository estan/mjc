/**
   Test large numbers.
   @author Lars Engebretsen
*/
class Big
{
    public static void main(String[] a){
	System.out.println(new Test().go(4711));
    }
}

class Test {

    public int go(int n) {
	return n - 4711;
    }

}
