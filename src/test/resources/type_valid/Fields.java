/**
   @author Henrik Österdahl
*/

class Fields {
    public static void main(String[] a){
	System.out.println(new FF().doTests());
    }
}

class FF {

    boolean fbool;

    public int doTests(){
	boolean lbool;

	fbool = true;
	lbool = false;

	lbool = fbool;

	if (fbool) {
	    System.out.println(1);
	} else {
	    System.out.println(0);
	}

	return 17;
    }

}
