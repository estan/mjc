package mjc.symbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mjc.analysis.DepthFirstAdapter;
import mjc.node.ABlockStatement;
import mjc.node.AClassDeclaration;
import mjc.node.AClassType;
import mjc.node.AFieldDeclaration;
import mjc.node.AFormalParameter;
import mjc.node.AMainClassDeclaration;
import mjc.node.AMethodDeclaration;
import mjc.node.AVariableDeclaration;
import mjc.node.PFormalParameter;
import mjc.node.PType;
import mjc.node.Start;
import mjc.node.TIdentifier;
import mjc.types.BuiltInType;
import mjc.types.ClassType;
import mjc.types.Type;
import mjc.types.UndefinedType;
import mjc.types.UnsupportedType;

public class SymbolTableBuilder {

    private SymbolTable symbolTable;
    private final List<String> errors = new ArrayList<>();

    public boolean build(Start tree, SymbolTable symbolTable) {
        this.symbolTable = symbolTable;

        errors.clear();

        // Apply first pass.
        tree.apply(new FirstPass());

        // Apply second pass if first one succeeded.
        if (!hasErrors())
            tree.apply(new SecondPass());

        return !hasErrors();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }

    private void error(String error, int line, int column) {
        errors.add(String.format("[%d,%d] %s", line, column, error));
    }

    /**
     * Visitor for the first pass of symbol table construction.
     *
     * In the first pass, we only add information about declared classes.
     */
    private class FirstPass extends DepthFirstAdapter {

        @Override
        public void inAMainClassDeclaration(AMainClassDeclaration node) {
            final TIdentifier id = node.getName();

            ClassInfo classInfo = new ClassInfo(id.getText());
            ClassType classType = new ClassType(id.getText(), classInfo);
            classInfo.setType(classType);

            final TIdentifier methodId = node.getMainMethodName();

            if (!methodId.getText().equals("main")) {
                final int line = methodId.getLine();
                final int column = methodId.getPos();
                error("Main class method name must be `main`", line, column);
                classInfo = ClassInfo.Undefined;
            }

            symbolTable.addClassInfo(classInfo.getName(), classInfo);
        }

        @Override
        public void inAClassDeclaration(AClassDeclaration node) {
            final TIdentifier id = node.getName();

            if (symbolTable.getClassInfo(id.getText()) != null) {
                final int line = id.getLine();
                final int column = id.getPos();
                error("Redeclaration of class `" + id.getText() + "`", line, column);
                symbolTable.addClassInfo(ClassInfo.Undefined.getName(), ClassInfo.Undefined);
            } else {
                ClassInfo classInfo = new ClassInfo(id.getText());
                ClassType classType = new ClassType(id.getText(), classInfo);
                classInfo.setType(classType);
                symbolTable.addClassInfo(classInfo.getName(), classInfo);
            }
        }
    }

    /**
     * Visitor for the second pass of symbol table construction.
     *
     * In the second pass, we visit everything else.
     */
    private class SecondPass extends DepthFirstAdapter {

        private ClassInfo currentClass;
        private MethodInfo currentMethod;

        @Override
        public void inAMainClassDeclaration(AMainClassDeclaration node) {
            currentClass = symbolTable.getClassInfo(node.getName().getText());

            final TIdentifier methodId = node.getMainMethodName();
            final int methodLine = methodId.getLine();
            final int methodColumn = methodId.getPos();
            currentMethod = new MethodInfo(methodId.getText(), UnsupportedType.Void,
                    methodLine, methodColumn);

            final TIdentifier argId = node.getArguments();
            final int argLine = argId.getLine();
            final int argColumn = argId.getPos();
            currentMethod.addParameter(new VariableInfo(argId.getText(),
                    UnsupportedType.StringArray, argLine, argColumn));

            currentClass.addMethod(currentMethod.getName(), currentMethod);
            currentMethod.enterBlock();
        }

        @Override
        public void outAMainClassDeclaration(AMainClassDeclaration node) {
            currentMethod.leaveBlock();
        }

        @Override
        public void inAClassDeclaration(AClassDeclaration node) {
            currentClass = symbolTable.getClassInfo(node.getName().getText());
        }

        @Override
        public void inAFieldDeclaration(AFieldDeclaration node) {

            final TIdentifier id = node.getName();
            final int line = id.getLine();
            final int column = id.getPos();

            if (currentClass.getField(id.getText()) != null) {
                error("Redeclaration of field `" + id.getText() + "`", line, column);
                currentClass.addField(VariableInfo.Undefined);
            } else {
                currentClass.addField(new VariableInfo(id.getText(),
                        resolve(node.getType()), line, column));
            }
        }

        @Override
        public void inAMethodDeclaration(AMethodDeclaration node) {
            currentMethod = currentClass.getMethod(node.getName().getText());

            final TIdentifier id = node.getName();
            final int line = id.getLine();
            final int column = id.getPos();

            boolean redeclaration = false;

            if (currentMethod != null) {
                /*
                 * Another method with the same name exists. If the number, types and order
                 * of the parameters match, we have a redeclaration!
                 */
                if (node.getFormalParameters().size() == currentMethod.getParameters().size()) {

                    Iterator<PFormalParameter> it1 = node.getFormalParameters().iterator();
                    Iterator<VariableInfo> it2 = currentMethod.getParameters().iterator();

                    redeclaration = true;
                    while (it1.hasNext()) {
                        AFormalParameter p1 = (AFormalParameter) it1.next();
                        VariableInfo p2 = it2.next();
                        if (!resolve(p1.getType()).equals(p2.getType())) {
                            redeclaration = false;
                            break;
                        }
                    }
                }
            }

            if (redeclaration) {
                error("Redeclaration of method `" + id.getText() + "`", line, column);
                currentMethod = MethodInfo.Undefined;
            } else {
                currentMethod = new MethodInfo(id.getText(), resolve(node.getReturnType()),
                        line, column);
            }

            currentClass.addMethod(currentMethod.getName(), currentMethod);
            currentMethod.enterBlock();
        }

        @Override
        public void outAMethodDeclaration(AMethodDeclaration node) {
            currentMethod.leaveBlock();
        }

        @Override
        public void inAFormalParameter(AFormalParameter node) {
            final TIdentifier id = node.getName();
            final int line = id.getLine();
            final int column = id.getPos();

            if (currentMethod.getParameter(id.getText()) != null) {
                error("Redeclaration of parameter `" + id.getText() + "`", line, column);
                currentMethod.addParameter(VariableInfo.Undefined);
            } else {
                currentMethod.addParameter(new VariableInfo(id.getText(),
                        resolve(node.getType()), line, column));
            }
        }

        @Override
        public void inAVariableDeclaration(AVariableDeclaration node) {
            final TIdentifier id = node.getName();
            final int line = id.getLine();
            final int column = id.getPos();

            if (currentMethod.getParameter(id.getText()) != null ||
                    currentMethod.getLocal(id.getText()) != null) {
                error("Redeclaration of local variable `" + id.getText() + "`", line, column);
                currentMethod.addLocal(VariableInfo.Undefined);
            } else {
                currentMethod.addLocal(new VariableInfo(id.getText(),
                        resolve(node.getType()), line, column));
            }
        }

        @Override
        public void inABlockStatement(ABlockStatement node) {
            currentMethod.enterBlock();
        }

        @Override
        public void outABlockStatement(ABlockStatement node) {
            currentMethod.leaveBlock();
        }

        private Type resolve(PType type) {
            if (type instanceof AClassType) {
                final AClassType classType = (AClassType)type;
                final TIdentifier id = classType.getName();
                final ClassInfo classInfo = symbolTable.getClassInfo(id.getText());

                if (classInfo == null) {
                    final int line = id.getLine();
                    final int column = id.getPos();
                    error("Undeclared identifier `" + id.getText() + "`", line, column);
                    return UndefinedType.Instance;
                } else {
                    return classInfo.getType();
                }
            } else {
                return BuiltInType.fromAbstract(type);
            }
        }
    }
}
