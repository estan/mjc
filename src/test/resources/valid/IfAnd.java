/**
   Tests semantics of &&.
   @author Lars Engebretsen
*/
class IfAnd
{
    public static void main(String[] a){
	System.out.println(new Test().go());
    }
}

class Test {
    int a;

    public int go() {
	a = 17;
	if(a < 13 && this.change()) {
	}
	else {
	}
	return a;
    }

    public boolean change() {
	a = 4711;
	return true;
    }
}
