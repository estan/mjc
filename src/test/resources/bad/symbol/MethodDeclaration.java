class MethodDeclarationTest {
    public static void main(String[] args) {
    }
}
class A {
    public int f(int a, boolean b) {
        return 42;
    }
    public long f(boolean a, long b) { // DUPLICATE_METHOD
        return 42;
    }
    public B g() { // UNDECLARED_CLASS
        return 42;
    }
}
