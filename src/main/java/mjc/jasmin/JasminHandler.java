package mjc.jasmin;

/**
 * The JasminHandler interface specifies a handler of Jasmin code.
 */
public interface JasminHandler {
    /**
     * Handle the result of Jasmin generation.
     *
     * @param className Name of the class from which the code was generated.
     * @param code Generated Jasmin code.
     */
    void handle(String className, StringBuilder code);
}
