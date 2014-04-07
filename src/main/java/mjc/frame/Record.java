package mjc.frame;

/**
 * Interface for handling of heap objects. The interface is independent of the
 * target architecture, but it is designed for real processors.
 */
public interface Record {
    /**
     * Allocates an Access object corresponding to a member of the record.
     * Record members are allocated consecutively, starting at offset zero.
     *
     * @return An Access object that describes how the corresponding member is
     *         accessed.
     */
    public Access allocField();

    /**
     * @return The size of the record in bytes.
     */
    public int size();
};
