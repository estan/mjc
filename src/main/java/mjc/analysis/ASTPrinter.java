package mjc.analysis;

import org.apache.log4j.Logger;

import mjc.node.Node;
import mjc.node.AFalseExpression;
import mjc.node.AIntegerExpression;
import mjc.node.ATrueExpression;
import mjc.node.Start;

/**
 * Simple visitor to print AST to log.
 */
public class ASTPrinter extends DepthFirstAdapter {
    private static final Logger logger = Logger.getLogger(ASTPrinter.class);

    private int i = 1; // Indentation level.

    public void print(Start tree) {
        i = 1;
        tree.apply(this);
    }

    @Override
    public void defaultIn(final Node node) {
        String line = String.format("%" + i + "s%s", "", node.getClass().getSimpleName());

        // For integer/boolean literals, we also print their value.
        if (node instanceof AIntegerExpression ||
            node instanceof ATrueExpression ||
            node instanceof AFalseExpression)
            line += " (" + node.toString().trim() + ")";

        logger.info(line);

        i += 2;
    }

    @Override
    public void defaultOut(final Node node) {
        i -= 2;
    }
}
