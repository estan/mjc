package mjc.analysis;

import org.apache.log4j.Logger;

import mjc.node.AMainClassDeclaration;

public class BasicAnalysisVisitor extends DepthFirstAdapter {
    private static final Logger log = Logger.getLogger(BasicAnalysisVisitor.class);

    private boolean success = true;

    public boolean success() {
        return success;
    }

    @Override
    public void caseAMainClassDeclaration(AMainClassDeclaration node) {
        final String className = node.getName().getText();
        final String methodName = node.getMainMethodName().toString().trim();

        // The single method on the main class must be called "main".
        if (methodName.compareTo("main") != 0) {
            log.error("missing main method in class `" + className + "` (found `" + methodName + "`)");
            success = false;
        }
    }
}
