package mjc;

/**
 * Class for input program errors.
 *
 * An Error represents an error in the input program. Each error has a line, column,
 * error code and an (optionally formatted) error message.
 *
 * Templates for errors are provided as public static instances, and from these, context
 * specific messages can be constructed using the {@link #on(int, int, Object...)} on}
 * factory method. E.g.
 *
 * <code>
 * System.out.println(Error.DUPLICATE_FIELD.on(line, column, theField));
 * </code>
 */
public class Error {
    // Symbol table construction errors.
    public final static Error MISSING_MAIN = new Error(100, "missing main method in `%s`");
    public final static Error DUPLICATE_CLASS = new Error(101, "duplicate class `%s`");
    public final static Error DUPLICATE_FIELD = new Error(102, "duplicate field `%s`");
    public final static Error DUPLICATE_METHOD = new Error(103, "duplicate method `%s`");
    public final static Error DUPLICATE_PARAMETER = new Error(104, "duplicate parameter `%s`");
    public final static Error DUPLICATE_VARIABLE = new Error(105, "duplicate variable `%s`");
    public final static Error UNDECLARED_CLASS = new Error(106, "undeclared class `%s`");

    // Type errors.
    public final static Error WRONG_RETURN_TYPE = new Error(200, "method `%s` must return value of type %s");
    public final static Error WRONG_IF_CONDITION_TYPE = new Error(201, "if: condition must be of type boolean");
    public final static Error WRONG_WHILE_CONDITION_TYPE = new Error(202, "while: condition must be of type boolean");
    public final static Error UNPRINTABLE_TYPE = new Error(203, "can't print value of type %s");
    public final static Error EXPECTED_VARIABLE_GOT_CLASS = new Error(204, "`%s` is a class name, expected variable name");
    public final static Error UNDECLARED_IDENTIFIER = new Error(205, "undeclared identifier `%s`");
    public final static Error INVALID_ASSIGNMENT = new Error(206, "can't assign %s to %s");
    public final static Error WRONG_INDEX_TYPE = new Error(207, "index of type %s, expected int");
    public final static Error NOT_ARRAY_TYPE = new Error(208, "`%s` is of non-array type %s");
    public final static Error INVALID_LEFT_OP_AND = new Error(209, "&&: invalid left-hand type %s");
    public final static Error INVALID_RIGHT_OP_AND = new Error(210, "&&: invalid right-hand type %s");
    public final static Error INVALID_LEFT_OP_OR = new Error(211, "||: invalid left-hand type %s");
    public final static Error INVALID_RIGHT_OP_OR = new Error(212, "||: invalid right-hand type %s");
    public final static Error INVALID_LT_COMPARISON = new Error(213, "<: can't compare %s to %s");
    public final static Error INVALID_GT_COMPARISON = new Error(214, ">: can't compare %s to %s");
    public final static Error INVALID_GE_COMPARISON = new Error(215, ">=: can't compare %s to %s");
    public final static Error INVALID_LE_COMPARISON = new Error(216, "<=: can't compare %s to %s");
    public final static Error INVALID_EQ_COMPARISON = new Error(217, "==: can't compare %s to %s");
    public final static Error INVALID_NE_COMPARISON = new Error(218, "!=: can't compare %s to %s");
    public final static Error INVALID_LEFT_OP_PLUS = new Error(219, "+: invalid left-hand type %s");
    public final static Error INVALID_RIGHT_OP_PLUS = new Error(220, "+: invalid right-hand type %s");
    public final static Error INVALID_LEFT_OP_MINUS = new Error(221, "-: invalid left-hand type %s");
    public final static Error INVALID_RIGHT_OP_MINUS = new Error(222, "-: invalid right-hand type %s");
    public final static Error INVALID_LEFT_OP_TIMES = new Error(223, "*: invalid left-hand type %s");
    public final static Error INVALID_RIGHT_OP_TIMES = new Error(224, "*: invalid right-hand type %s");
    public final static Error INVALID_SUBTRACTION = new Error(225, "-: can't subtract %s from %s");
    public final static Error NEGATION_EXPECTED_BOOLEAN = new Error(226, "!: expected boolean, but got %s");
    public final static Error WRONG_PARAMETER_TYPE = new Error(227, "in call to `%s`: parameter %d of type %s, expected %s");
    public final static Error WRONG_PARAMETER_COUNT = new Error(228, "in call to `%s`: %d parameters given, expected %d");
    public final static Error UNDECLARED_METHOD = new Error(229, "no method `%s` in class `%s`");
    public final static Error METHOD_CALL_ON_NON_CLASS_TYPE = new Error(230, "method call on expression of non-class type %s");
    public final static Error LENGTH_ON_NON_ARRAY_TYPE = new Error(231, "length unsupported on non-array type %s");
    public final static Error WRONG_SIZE_TYPE = new Error(232, "size of type %s, expected int");
    public final static Error INVALID_INT_LITERAL = new Error(233, "invalid int literal %s");
    public final static Error INVALID_LONG_LITERAL = new Error(234, "invalid long literal %s");

    private int line;
    private int column;
    private final int code;
    private final String message;
    private Object[] args;

    /**
     * Constructs an error template instance.
     *
     * @param code Error code.
     * @param format Error message (optionally including format specifiers).
     */
    private Error(int code, final String message) {
        this(code, message, new Object[0], 0, 0);
    }

    /**
     * Constructs a full error.
     *
     * @param code Error code.
     * @param message Error message including format specifiers.
     * @param args Arguments referred to in the message.
     * @param line Line of the error.
     * @param column Column of the error.
     */
    private Error(int code, final String message, Object[] args, int line, int column) {
        this.code = code;
        this.message = message;
        this.args = args;
        this.line = line;
        this.column = column;
    }

    /**
     * @return Line of the error.
     */
    public int getLine() {
        return line;
    }

    /**
     * @return Column of the error.
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return Formatted error message.
     */
    public String getMessage() {
        return args.length == 0 ? message : String.format(message, args);
    }

    /**
     * @return Error code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Constructs a full message from this error template.
     *
     * @param line Line of the error.
     * @param column Column of the error.
     * @param args Arguments for the error message.
     * @return The constructed error message.
     */
    public Error on(int line, int column, Object... args) {
        return new Error(code, message, args, line, column);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other instanceof Error)
            return code == ((Error) other).code;
        return false;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "[" + line + "," + column + "] error: " + getMessage();
    }
}
