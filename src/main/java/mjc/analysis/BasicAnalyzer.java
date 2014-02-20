package mjc.analysis;

import org.apache.log4j.Logger;

import mjc.node.AMainClassDeclaration;
import mjc.node.Start;

public class BasicAnalyzer extends DepthFirstAdapter {
    private static final Logger log = Logger.getLogger(BasicAnalyzer.class);

    private boolean success = true;

    public boolean analyze(Start tree) {
        success = true;
        tree.apply(this);
        return success;
    }

    @Override
    public void caseAMainClassDeclaration(AMainClassDeclaration node) {
        final String className = node.getName().getText();
        final String methodName = node.getMainMethodName().getText();

        // The single method on the main class must be called "main".
        if (methodName.compareTo("main") != 0) {
            log.error("missing main method in class `" + className + "` (found `" + methodName + "`)");
            success = false;
        }
    }
}
