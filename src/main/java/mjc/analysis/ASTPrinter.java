package mjc.analysis;

import mjc.node.EOF;
import mjc.node.Node;
import mjc.node.Start;
import mjc.node.Token;

/**
 * Simple visitor to print AST to standard output.
 */
public class ASTPrinter extends DepthFirstAdapter {
    private int indent;            // Indentation level.
    private StringBuilder builder; // Builder for the output.

    public void print(Node ast) {
        ast.apply(this);
    }

    @Override
    public void inStart(Start node) {
        indent = 0;
        builder = new StringBuilder();
        builder.append("[Abstract Syntax Tree]\n");
    }

    @Override
    public void outStart(Start node) {
        System.out.print(builder.toString());
    }

    @Override
    public void defaultIn(final Node node) {
        indent();
        builder.append(node.getClass().getSimpleName() + '\n');
        indent += 2;
    }

    @Override
    public void defaultOut(final Node node) {
        indent -= 2;
    }

    @Override
    public void defaultCase(Node node) {
        if (node instanceof EOF)
            return;

        indent();

        Token token = (Token) node;
        String name = token.getClass().getSimpleName();
        String text = token.getText();
        builder.append(String.format("%s(%s)\n", name, text));
    }

    private void indent() {
        for (int i = 0; i < indent; ++i)
            builder.append(' ');
    }
}
