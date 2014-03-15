package mjc.types;

/**
 * UnsupportedType represents an unsupported type.
 *
 * This is currently just void and String[], which it is convenient to consider as
 * a type during compilation, since they appear as return value and parameter type
 * for main(). Operations with unsupported types are never allowed.
 *
 * There are only two static instances of this class.
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
        return name;
    }

    @Override
    public boolean isUnsupported() {
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
