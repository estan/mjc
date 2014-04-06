package mjc.translate;

import mjc.temp.Label;
import mjc.tree.Exp;
import mjc.tree.Stm;

/**
 * TreeNode represents the IR translation of an AST node.
 *
 * A TreeNode may be evaluated into the final IR in three different ways:
 *
 * <ul>
 *     <li>For its value (using {@link #asExp()})</li>
 *     <li>For its side-effects (using {@link #asStm()})</li>
 *     <li>For flow control (using {@link #asCond(Label, Label)})</li>
 * </ul>
 *
 * Subclasses must implement these methods to return the appropriate IR tree.
 *
 * During translation, an AST node is translated into an appropriate TreeNode subclass.
 * Depending on the context, the translation code will then call either {@link #asExp()},
 * {@link #asStm()} or {@link #asCond(Label, Label)} on the TreeNode to get the final IR
 * tree translation required.
 *
 * This distinction is mostly important for boolean expressions: A boolean expression
 * may be used for its value (zero or one), or it may be used to control the program
 * flow. The most efficient IR translations for these two cases are completely different.
 */
abstract class TreeNode {

    /**
     * @return an IR expression evaluating this node for its value.
     */
    abstract Exp asExp();

    /**
     * @return an IR statement evaluating this node for its side-effects.
     */
    abstract Stm asStm();

    /**
     * Returns an IR statement evaluating this node for flow control.
     *
     * @param trueLabel Label to jump to if this node evaluates to true.
     * @param falseLabel Label to jump to if this node evaluates to false.
     * @return IR statement that performs the jump.
     */
    abstract Stm asCond(Label trueLabel, Label falseLabel);
}
