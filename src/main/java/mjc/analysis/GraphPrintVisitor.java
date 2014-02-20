package mjc.analysis;

import mjc.node.Node;
import mjc.node.Start;
import mjc.node.AFalseExpression;
import mjc.node.AIntegerExpression;
import mjc.node.ATrueExpression;

/**
 * Simple visitor to print AST in GraphViz format on standard output.
 */
public class GraphPrintVisitor extends DepthFirstAdapter {
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
        String label = null;
        if (node instanceof AIntegerExpression ||
            node instanceof ATrueExpression ||
            node instanceof AFalseExpression)
            label = node.toString();
        else
            label = node.getClass().getSimpleName();
        String name = node.getClass().getSimpleName() + node.hashCode();
        System.out.println(name + "[label=" + label + "];");

        if (node.parent() != null) {
            String parentLabel = node.parent().getClass().getSimpleName();
            String parentName = parentLabel + node.parent().hashCode();
            System.out.println(parentName + " -> " + name + ";");
        }
    }
}
