package mjc.types;

/**
 * UnsupportedType represents an unsupported type.
 *
 * There are only two static instances of this type, one for void and one for
 * String[]. These types appear as the return type and parameter type of the main
 * method.
 *
 * Unsupported types are incompatible with all other types, including itself,
 * effectively preventing the programmer from calling the main method or
 * referencing its parameter without getting a type error.
 */
public class UnsupportedType extends Type {
    public final static Type Void = new UnsupportedType("void");
    public final static Type StringArray = new UnsupportedType("String[]");

    private final String name;

    private UnsupportedType(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name + " (unsupported)";
    }
}
