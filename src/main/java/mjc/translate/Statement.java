package mjc.translate;

import mjc.temp.Label;
import mjc.tree.Exp;
import mjc.tree.Stm;

/**
 * Statement represents the translation of a statement.
 *
 * Statements have no values and cannot be used as conditionals. It it thus an
 * error to call {@link #asExp()} or {@link #asCond(Label, Label)} on instances
 * of this class.
 */
class Statement implements Translation {
    final Stm statement;

    /**
     * Constructs a new Statement.
     *
     * @param statement The IR statement.
     */
    Statement(Stm statement) {
        this.statement = statement;
    }

    @Override
    public Exp asExp() {
        throw new Error("Statement has no expression representation");
    }

    @Override
    public Stm asStm() {
        return statement;
    }

    @Override
    public Stm asCond(Label trueLabel, Label falseLabel) {
        throw new Error("Statement has no conditional representation");
    }
}
