package mjc.error;

/**
 * MiniJava program error types.
 */
public enum MiniJavaErrorType {

    /*
     * Lexer / parser errors.
     */
    LEXER_ERROR                   (50, "invalid token: %s"),
    PARSER_ERROR                  (51, "parsing failed: %s"),

    /*
     * Symbol table construction errors.
     */
    MISSING_MAIN                  (100, "missing main method in `%s`"),
    DUPLICATE_CLASS               (101, "duplicate class `%s`"),
    DUPLICATE_FIELD               (102, "duplicate field `%s`"),
    DUPLICATE_METHOD              (103, "duplicate method `%s`"),
    DUPLICATE_PARAMETER           (104, "duplicate parameter `%s`"),
    DUPLICATE_VARIABLE            (105, "duplicate variable `%s`"),
    UNDECLARED_CLASS              (106, "undeclared class `%s`"),

    /*
     * Type errors.
     */
    UNDECLARED_IDENTIFIER         (200, "undeclared identifier `%s`"),
    UNDECLARED_METHOD             (201, "no method `%s` in class `%s`"),
    INVALID_RETURN_TYPE           (202, "method `%s` must return value of type %s"),
    INVALID_CONDITION_TYPE        (203, "condition must be of type boolean, not %s"),
    INVALID_PRINTLN_TYPE          (204, "can't print value of type %s"),
    INVALID_ASSIGNMENT            (205, "can't assign %s to %s"),
    INVALID_INDEX_TYPE            (206, "index of type %s, expected int"),
    INVALID_BINARY_OP             (207, "%s: invalid operand types %s and %s"),
    INVALID_UNARY_OP              (208, "%s: invalid operand type %s"),
    INVALID_PARAM_TYPE            (209, "in call to `%s`: parameter %d of type %s, expected %s"),
    INVALID_PARAM_COUNT           (210, "in call to `%s`: %d parameters given, expected %d"),
    INVALID_SIZE_TYPE             (211, "size of type %s, expected int"),
    INVALID_INT_LITERAL           (212, "invalid int literal %s"),
    INVALID_LONG_LITERAL          (213, "invalid long literal %s"),
    EXPECTED_VARIABLE_GOT_CLASS   (214, "`%s` is a class name, expected variable name"),
    NOT_ARRAY_TYPE                (215, "%s is not an array type"),
    CALL_ON_NON_CLASS             (216, "method call on expression of non-class type %s"),
    LENGTH_ON_NON_ARRAY           (217, "length unsupported on non-array type %s"),

    INTERNAL_ERROR                (1000, "Internal compiler error: %s");

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