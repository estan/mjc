package mjc.analysis;

import org.apache.log4j.Logger;

import mjc.node.EOF;
import mjc.node.Node;
import mjc.node.Start;
import mjc.node.Token;

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
        logger.info(String.format("%" + i + "s%s", "", node.getClass().getSimpleName()));
        i += 2;
    }

    @Override
    public void defaultCase(Node node) {
        if (node instanceof EOF)
            return;

        // For tokens, we print both their name and value.
        Token token = (Token) node;
        logger.info(String.format("%" + i + "s%s(%s)", "",
                token.getClass().getSimpleName(), token.getText()));
    }

    @Override
    public void defaultOut(final Node node) {
        i -= 2;
    }
}
