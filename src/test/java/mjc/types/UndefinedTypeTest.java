package mjc.types;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class UndefinedTypeTest {

    final Type[] supportedTypes = {
        BuiltInType.Boolean,
        BuiltInType.Int,
        BuiltInType.IntArray,
        BuiltInType.Long,
        BuiltInType.LongArray,
        UndefinedType.Instance
    };

    @Test
    public void testIsUndefined() {
        assertThat(UndefinedType.Instance.isUndefined(), is(true));
    }

    @Test
    public void testIsAssignableTo() {
        for (Type otherType : supportedTypes) {
            assertThat(UndefinedType.Instance.isAssignableTo(otherType), is(true));
        }
        assertThat(UndefinedType.Instance.isAssignableTo(UnsupportedType.Void), is(false));
        assertThat(UndefinedType.Instance.isAssignableTo(UnsupportedType.StringArray), is(false));
    }

    @Test
    public void testIsEqualComparableTo() {
        for (Type otherType : supportedTypes) {
            assertThat(UndefinedType.Instance.isEqualComparableTo(otherType), is(true));
        }
        assertThat(UndefinedType.Instance.isEqualComparableTo(UnsupportedType.Void), is(false));
        assertThat(UndefinedType.Instance.isEqualComparableTo(UnsupportedType.StringArray), is(false));
    }

    @Test
    public void testIsRelationalComparableTo() {
        for (Type otherType : supportedTypes) {
            assertThat(UndefinedType.Instance.isRelationalComparableTo(otherType), is(true));
        }
        assertThat(UndefinedType.Instance.isRelationalComparableTo(UnsupportedType.Void), is(false));
        assertThat(UndefinedType.Instance.isRelationalComparableTo(UnsupportedType.StringArray), is(false));
    }

    @Test
    public void testIsAddableTo() {
        for (Type otherType : supportedTypes) {
            assertThat(UndefinedType.Instance.isAddableTo(otherType), is(true));
        }
        assertThat(UndefinedType.Instance.isAddableTo(UnsupportedType.Void), is(false));
        assertThat(UndefinedType.Instance.isAddableTo(UnsupportedType.StringArray), is(false));
    }

    @Test
    public void testIsSubtractableFrom() {
        for (Type otherType : supportedTypes) {
            assertThat(UndefinedType.Instance.isSubtractableFrom(otherType), is(true));
        }
        assertThat(UndefinedType.Instance.isAddableTo(UnsupportedType.Void), is(false));
        assertThat(UndefinedType.Instance.isAddableTo(UnsupportedType.StringArray), is(false));
    }

    @Test
    public void testIsMultipliableWith() {
        for (Type otherType : supportedTypes) {
            assertThat(UndefinedType.Instance.isMultipliableWith(otherType), is(true));
        }
        assertThat(UndefinedType.Instance.isMultipliableWith(UnsupportedType.Void), is(false));
        assertThat(UndefinedType.Instance.isMultipliableWith(UnsupportedType.StringArray), is(false));
    }

    @Test
    public void testIsDisjunctableWith() {
        for (Type otherType : supportedTypes) {
            assertThat(UndefinedType.Instance.isDisjunctableWith(otherType), is(true));
        }
        assertThat(UndefinedType.Instance.isDisjunctableWith(UnsupportedType.Void), is(false));
        assertThat(UndefinedType.Instance.isDisjunctableWith(UnsupportedType.StringArray), is(false));
    }

    @Test
    public void testIsConjunctableWith() {
        for (Type otherType : supportedTypes) {
            assertThat(UndefinedType.Instance.isConjunctableWith(otherType), is(true));
        }
        assertThat(UndefinedType.Instance.isConjunctableWith(UnsupportedType.Void), is(false));
        assertThat(UndefinedType.Instance.isConjunctableWith(UnsupportedType.StringArray), is(false));
    }

}
