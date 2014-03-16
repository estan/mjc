class FieldDeclarationTest {
    public static void main(String[] args) {
    }
}
class A {
    int a;
    long a; // DUPLICATE_FIELD
    B b; // UNDECLARED_CLASS
}
