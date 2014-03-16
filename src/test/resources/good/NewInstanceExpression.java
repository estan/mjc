class NewInstanceExpressionTest {
    public static void main(String[] args) {
        A cA;
        NewInstanceExpression foo;
        cA = new A(); // OK!
        foo = new NewInstanceExpressionTest(); // OK!
    }
}
class A {}
