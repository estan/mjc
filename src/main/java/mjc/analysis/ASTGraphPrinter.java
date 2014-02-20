package mjc.analysis;

import mjc.node.EOF;
import mjc.node.Node;
import mjc.node.Start;
import mjc.node.Token;

/**
 * Simple visitor to print AST in GraphViz format on standard output.
 */
public class ASTGraphPrinter extends DepthFirstAdapter {

    public void print(Start tree) {
        tree.apply(this);
    }

    @Override
    public void inStart(final Start node) {
        System.out.println("digraph G {");
        System.out.println("Start" + node.hashCode() + "[label=Start];");
    }

    @Override
    public void outStart(final Start node) {
        System.out.println("}");
    }

    @Override
    public void defaultIn(final Node node) {
        printNode(node, node.getClass().getSimpleName());
    }

    @Override
    public void defaultCase(Node node) {
        if (!(node instanceof EOF))
            printNode(node, ((Token) node).getText());
    }

    private void printNode(Node node, String label) {
        System.out.println(uniqueName(node) + "[label=" + label + "];");
        if (node.parent() != null)
            printEdge(node.parent(), node);
    }

    private void printEdge(Node from, Node to) {
        System.out.println(uniqueName(from) + " -> " + uniqueName(to) + ";");
    }

    private String uniqueName(Node node) {
        return node.getClass().getSimpleName() + node.hashCode();
    }
}
