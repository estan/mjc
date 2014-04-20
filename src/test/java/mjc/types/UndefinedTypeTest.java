package mjc.types;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class UndefinedTypeTest {
    final ClassType classType = new ClassType("ClassType");

    final Type[] allTypes = {
        BuiltInType.Int,
        BuiltInType.IntArray,
        BuiltInType.Boolean,
        UndefinedType.Instance,
        classType
    };

    @Test
    public void testIsUndefined() {
        assertThat(UndefinedType.Instance.isUndefined(), is(true));
    }

    @Test
    public void testIsAssignableTo() {
        for (Type otherType : allTypes) {
            assertThat(UndefinedType.Instance.isAssignableTo(otherType), is(true));
        }
    }

    @Test
    public void testIsEqualComparableTo() {
        for (Type otherType : allTypes) {
            assertThat(UndefinedType.Instance.isEqualComparableTo(otherType), is(true));
        }
    }

    @Test
    public void testIsRelationalComparableTo() {
        for (Type otherType : allTypes) {
            assertThat(UndefinedType.Instance.isRelationalComparableTo(otherType), is(true));
        }
    }

    @Test
    public void testIsAddableTo() {
        for (Type otherType : allTypes) {
            assertThat(UndefinedType.Instance.isAddableTo(otherType), is(true));
        }
    }

    @Test
    public void testIsSubtractableFrom() {
        for (Type otherType : allTypes) {
            assertThat(UndefinedType.Instance.isSubtractableFrom(otherType), is(true));
        }
    }

    @Test
    public void testIsMultipliableWith() {
        for (Type otherType : allTypes) {
            assertThat(UndefinedType.Instance.isMultipliableWith(otherType), is(true));
        }
    }

    @Test
    public void testIsDisjunctableWith() {
        for (Type otherType : allTypes) {
            assertThat(UndefinedType.Instance.isDisjunctableWith(otherType), is(true));
        }
    }

    @Test
    public void testIsConjunctableWith() {
        for (Type otherType : allTypes) {
            assertThat(UndefinedType.Instance.isConjunctableWith(otherType), is(true));
        }
    }
}
