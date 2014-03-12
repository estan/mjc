/**
   Test large numbers.
   @author Lars Engebretsen
*/
class Negative
{
    public static void main(String[] a){
	System.out.println(new Test().go() + 711127);
    }
}

class Test {

    public int go() {
	System.out.println(4711);
	System.out.println(0-4711);
	return 1;
    }

}
