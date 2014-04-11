package mjc.translate;

import mjc.temp.Label;
import mjc.tree.Exp;
import mjc.tree.Stm;

/**
 * Translation is an interface to the translation of an abstract syntax node.
 *
 * Sometimes the mapping between an AST node and its IR translation is not 1-to-1. For
 * instance, a conditional expression such as "x != 5" should be translated differently
 * depending on how how it is used: If it is the rvalue in an assignment, we need IR
 * code to get its value. If used as the condition in an if or while, we need IR code
 * to conditionally branch based on its value.
 *
 * Since the translation proceeds in a depth-first manner through the AST, we need to
 * defer the decision on what translation to use until we know the context. For this
 * reason, this interface specifies three methods to be implemented:
 *
 * <ul>
 *     <li>{@link #asExp()}, to get the value,</li>
 *     <li>{@link #asStm()}, to get only the side-effects, and</li>
 *     <li>{@link #asCond(Label, Label)}, to get control transfer code.</li>
 * </ul>
 */
interface Translation {

    /** @return code for obtaining the value of this translation. */
    Exp asExp();

    /** @return code evaluating this translation for its side-effects. */
    Stm asStm();

    /** @return code to transfer control to either @a trueLabel or @a falseLabel. */
    Stm asCond(Label trueLabel, Label falseLabel);
}
