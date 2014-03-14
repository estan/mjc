package mjc.analysis;

import mjc.node.EOF;
import mjc.node.Node;
import mjc.node.Start;
import mjc.node.Token;

/**
 * Simple visitor to print AST in GraphViz format on standard output.
 */
public class ASTGraphPrinter extends DepthFirstAdapter {
    private StringBuilder builder;

    public void print(Start tree) {
        tree.apply(this);
    }

    @Override
    public void inStart(final Start node) {
        builder = new StringBuilder();
        builder.append("digraph G {\n");
        printNode(node, node.getClass().getSimpleName());
    }

    @Override
    public void outStart(final Start node) {
        builder.append("}\n");
        System.out.print(builder.toString());
    }

    @Override
    public void defaultIn(final Node node) {
        printNode(node, node.getClass().getSimpleName());
    }

    @Override
    public void defaultCase(Node node) {
        if (node instanceof EOF)
            return;

        final Token token = (Token) node;
        switch (token.getText()) {
            case "=":
            case "!=":
            case "&&":
            case "||":
            case "<":
            case "<=":
            case ">":
            case ">=":
            case "length":
            case "new":
            case "if":
            case "else":
            case "[":
            case "*":
            case "+":
            case "-":
            case "int":
            case "long":
            case "int[]":
            case "long[]":
            case "boolean":
                break; // Some noise in our AST that we can skip.
            default:
                printNode(node, ((Token) node).getText());
        }
    }

    private void printNode(Node node, String label) {
        builder.append(String.format("%s[label=\"%s\"];\n", uniqueName(node), label));
        if (node.parent() != null)
            printEdge(node.parent(), node);
    }

    private void printEdge(Node from, Node to) {
        builder.append(uniqueName(from) + " -> " + uniqueName(to) + ";\n");
    }

    private String uniqueName(Node node) {
        return node.getClass().getSimpleName() + node.hashCode();
    }
}
