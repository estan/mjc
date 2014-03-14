package mjc.error;

/**
 * MiniJava program error types.
 */
public enum MiniJavaErrorType {

    /*
     * Symbol table construction errors.
     */
    MISSING_MAIN                    (100, "missing main method in `%s`"),
    DUPLICATE_CLASS                 (101, "duplicate class `%s`"),
    DUPLICATE_FIELD                 (102, "duplicate field `%s`"),
    DUPLICATE_METHOD                (103, "duplicate method `%s`"),
    DUPLICATE_PARAMETER             (104, "duplicate parameter `%s`"),
    DUPLICATE_VARIABLE              (105, "duplicate variable `%s`"),
    UNDECLARED_CLASS                (106, "undeclared class `%s`"),

    /*
     * Type errors.
     */
    WRONG_RETURN_TYPE               (200, "method `%s` must return value of type %s"),
    WRONG_IF_CONDITION_TYPE         (201, "if: condition must be of type boolean"),
    WRONG_WHILE_CONDITION_TYPE      (202, "while: condition must be of type boolean"),
    UNPRINTABLE_TYPE                (203, "can't print value of type %s"),
    EXPECTED_VARIABLE_GOT_CLASS     (204, "`%s` is a class name, expected variable name"),
    UNDECLARED_IDENTIFIER           (205, "undeclared identifier `%s`"),
    INVALID_ASSIGNMENT              (206, "can't assign %s to %s"),
    WRONG_INDEX_TYPE                (207, "index of type %s, expected int"),
    NOT_ARRAY_TYPE                  (208, "`%s` is of non-array type %s"),
    INVALID_LEFT_OP_AND             (209, "&&: invalid left-hand type %s"),
    INVALID_RIGHT_OP_AND            (210, "&&: invalid right-hand type %s"),
    INVALID_LEFT_OP_OR              (211, "||: invalid left-hand type %s"),
    INVALID_RIGHT_OP_OR             (212, "||: invalid right-hand type %s"),
    INVALID_LT_COMPARISON           (213, "<: can't compare %s to %s"),
    INVALID_GT_COMPARISON           (214, ">: can't compare %s to %s"),
    INVALID_GE_COMPARISON           (215, ">=: can't compare %s to %s"),
    INVALID_LE_COMPARISON           (216, "<=: can't compare %s to %s"),
    INVALID_EQ_COMPARISON           (217, "==: can't compare %s to %s"),
    INVALID_NE_COMPARISON           (218, "!=: can't compare %s to %s"),
    INVALID_LEFT_OP_PLUS            (219, "+: invalid left-hand type %s"),
    INVALID_RIGHT_OP_PLUS           (220, "+: invalid right-hand type %s"),
    INVALID_LEFT_OP_MINUS           (221, "-: invalid left-hand type %s"),
    INVALID_RIGHT_OP_MINUS          (222, "-: invalid right-hand type %s"),
    INVALID_LEFT_OP_TIMES           (223, "*: invalid left-hand type %s"),
    INVALID_RIGHT_OP_TIMES          (224, "*: invalid right-hand type %s"),
    INVALID_SUBTRACTION             (225, "-: can't subtract %s from %s"),
    NEGATION_EXPECTED_BOOLEAN       (226, "!: expected boolean, but got %s"),
    WRONG_PARAMETER_TYPE            (227, "in call to `%s`: parameter %d of type %s, expected %s"),
    WRONG_PARAMETER_COUNT           (228, "in call to `%s`: %d parameters given, expected %d"),
    UNDECLARED_METHOD               (229, "no method `%s` in class `%s`"),
    METHOD_CALL_ON_NON_CLASS_TYPE   (230, "method call on expression of non-class type %s"),
    LENGTH_ON_NON_ARRAY_TYPE        (231, "length unsupported on non-array type %s"),
    WRONG_SIZE_TYPE                 (232, "size of type %s, expected int"),
    INVALID_INT_LITERAL             (233, "invalid int literal %s"),
    INVALID_LONG_LITERAL            (234, "invalid long literal %s");

    private final int code;
    private final String message;

    private MiniJavaErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Factory method to create an actual error of this type.
     *
     * The slightly weird method name is because it feels kind of natural to do e.g:
     *
     * <code>
     *     System.out.println(WRONG_RETURN_TYPE.on(line, column, type));
     * </code>
     *
     * Also, it's short :)
     *
     * @param line Line of the error.
     * @param column Column of the error.
     * @param args Arguments for the error message.
     * @return The constructed MiniJavaError.
     */
    public MiniJavaError on(int line, int column, Object... args) {
        return new MiniJavaError(line, column, this, args);
    }
}