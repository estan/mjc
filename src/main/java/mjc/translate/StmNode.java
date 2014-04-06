package mjc.translate;

import mjc.tree.Stm;

/**
 * Abstract base class for statement nodes.
 */
abstract class StmNode extends TreeNode {
    final Stm statement;

    StmNode(Stm statement) {
        this.statement = statement;
    }

    @Override
    Stm asStm() {
        return statement;
    }
}
