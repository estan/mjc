class VariableDeclarationTest {
    public static void main(String[] args) {
    }
}
class A {
    public int f(int a) {
        int a; // DUPLICATE_VARIABLE
        return 42;
    }
    public int g() {
        int a;
        int a; // DUPLICATE_VARIABLE
        return 42;
    }
    public int h() {
        int a;
        {
            int b;
            {
                int a; // DUPLICATE_VARIABLE
            }
        }
        return 42;
    }
    public int i() {
        B b; // UNDECLARED_CLASS
        return 42;
    }
}
