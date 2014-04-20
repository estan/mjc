class MethodDeclarationTest {
    public static void main(String[] args) {
    }
}
class A {
    public int f(int a, boolean b) {
        return 42;
    }
    public int[] f(boolean a, int b) { // DUPLICATE_METHOD
        int[] ia;
        return ia;
    }
    public B g() { // UNDECLARED_CLASS
        return 42;
    }
}
