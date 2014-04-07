package mjc.frame;

import mjc.tree.Exp;

/**
 * Interface that models an "access" to a variable. The variable can be either
 * on the heap or on the stack (or in a register), but it is typically described
 * by an offset from a base pointer.
 *
 * For variables in a record on the heap, the base pointer is normally a pointer
 * to the first element in the record.
 *
 * For variables on the stack, the base pointer is normally the frame pointer,
 * but outgoing parameters may be accessed relative to the stack pointer. This
 * interface is independent of the target architecture, but it is designed for
 * real processors.
 */
public interface Access {
    /**
     * A variable access is typically modeled by a relative offset from a base
     * pointer. This function creates a IR node that accesses the variable from
     * a IR node that accesses the base pointer.
     *
     * @param basePointer An IR node that evaluates to the base pointer.
     * @return An IR node that can be used to load or store the variable.
     */
    public Exp exp(Exp basePointer);
}
