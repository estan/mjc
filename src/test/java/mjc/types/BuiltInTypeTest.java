package mjc.types;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class BuiltInTypeTest {
    final ClassType classType = new ClassType("TestClass");

    @Test
    public void testIsBuiltIn() {
        assertThat(BuiltInType.Boolean.isBuiltIn(), is(true));
        assertThat(BuiltInType.Int.isBuiltIn(), is(true));
        assertThat(BuiltInType.IntArray.isBuiltIn(), is(true));
    }

    @Test
    public void testIsInt() {
        assertThat(BuiltInType.Boolean.isInt(), is(false));
        assertThat(BuiltInType.Int.isInt(), is(true));
        assertThat(BuiltInType.IntArray.isInt(), is(false));
    }

    @Test
    public void testIsIntArray() {
        assertThat(BuiltInType.Boolean.isIntArray(), is(false));
        assertThat(BuiltInType.Int.isIntArray(), is(false));
        assertThat(BuiltInType.IntArray.isIntArray(), is(true));
    }

    @Test
    public void testIsBoolean() {
        assertThat(BuiltInType.Boolean.isBoolean(), is(true));
        assertThat(BuiltInType.Int.isBoolean(), is(false));
        assertThat(BuiltInType.IntArray.isBoolean(), is(false));
    }

    @Test
    public void testIsClass() {
        assertThat(BuiltInType.Boolean.isClass(), is(false));
        assertThat(BuiltInType.Int.isClass(), is(false));
        assertThat(BuiltInType.IntArray.isClass(), is(false));
    }

    @Test
    public void testIsReference() {
        assertThat(BuiltInType.Boolean.isReference(), is(false));
        assertThat(BuiltInType.Int.isReference(), is(false));
        assertThat(BuiltInType.IntArray.isReference(), is(true));
    }

    @Test
    public void testIsAssignableTo() {
        // Boolean
        assertThat(BuiltInType.Boolean.isAssignableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isAssignableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isAssignableTo(BuiltInType.Boolean), is(true));
        assertThat(BuiltInType.Boolean.isAssignableTo(classType), is(false));
        assertThat(BuiltInType.Boolean.isAssignableTo(UndefinedType.Instance), is(true));

        // Int
        assertThat(BuiltInType.Int.isAssignableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isAssignableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isAssignableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isAssignableTo(classType), is(false));
        assertThat(BuiltInType.Int.isAssignableTo(UndefinedType.Instance), is(true));

        // IntArray
        assertThat(BuiltInType.IntArray.isAssignableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isAssignableTo(BuiltInType.IntArray), is(true));
        assertThat(BuiltInType.IntArray.isAssignableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isAssignableTo(classType), is(false));
        assertThat(BuiltInType.IntArray.isAssignableTo(UndefinedType.Instance), is(true));
    }

    @Test
    public void testIsEqualComparableTo() {
        // Boolean
        assertThat(BuiltInType.Boolean.isEqualComparableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isEqualComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isEqualComparableTo(BuiltInType.Boolean), is(true));
        assertThat(BuiltInType.Boolean.isEqualComparableTo(classType), is(false));
        assertThat(BuiltInType.Boolean.isEqualComparableTo(UndefinedType.Instance), is(true));

        // Int
        assertThat(BuiltInType.Int.isEqualComparableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isEqualComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isEqualComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isEqualComparableTo(classType), is(false));
        assertThat(BuiltInType.Int.isEqualComparableTo(UndefinedType.Instance), is(true));

        // IntArray
        assertThat(BuiltInType.IntArray.isEqualComparableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isEqualComparableTo(BuiltInType.IntArray), is(true));
        assertThat(BuiltInType.IntArray.isEqualComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isEqualComparableTo(classType), is(false));
        assertThat(BuiltInType.IntArray.isEqualComparableTo(UndefinedType.Instance), is(true));
    }

    @Test
    public void testIsRelationalComparableTo() {
        // Boolean
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(classType), is(false));
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(UndefinedType.Instance), is(true));

        // Int
        assertThat(BuiltInType.Int.isRelationalComparableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isRelationalComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isRelationalComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isRelationalComparableTo(classType), is(false));
        assertThat(BuiltInType.Int.isRelationalComparableTo(UndefinedType.Instance), is(true));

        // IntArray
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(classType), is(false));
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(UndefinedType.Instance), is(true));
    }

    @Test
    public void testIsAddableTo() {
        // Boolean
        assertThat(BuiltInType.Boolean.isAddableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isAddableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isAddableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Boolean.isAddableTo(classType), is(false));
        assertThat(BuiltInType.Boolean.isAddableTo(UndefinedType.Instance), is(true));

        // Int
        assertThat(BuiltInType.Int.isAddableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isAddableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isAddableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isAddableTo(classType), is(false));
        assertThat(BuiltInType.Int.isAddableTo(UndefinedType.Instance), is(true));

        // IntArray
        assertThat(BuiltInType.IntArray.isAddableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isAddableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isAddableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isAddableTo(classType), is(false));
        assertThat(BuiltInType.IntArray.isAddableTo(UndefinedType.Instance), is(true));
    }

    @Test
    public void testIsSubtractableFrom() {
        // Boolean
        assertThat(BuiltInType.Boolean.isSubtractableFrom(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isSubtractableFrom(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isSubtractableFrom(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Boolean.isSubtractableFrom(classType), is(false));
        assertThat(BuiltInType.Boolean.isSubtractableFrom(UndefinedType.Instance), is(true));

        // Int
        assertThat(BuiltInType.Int.isSubtractableFrom(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isSubtractableFrom(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isSubtractableFrom(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isSubtractableFrom(classType), is(false));
        assertThat(BuiltInType.Int.isSubtractableFrom(UndefinedType.Instance), is(true));

        // IntArray
        assertThat(BuiltInType.IntArray.isSubtractableFrom(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isSubtractableFrom(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isSubtractableFrom(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isSubtractableFrom(classType), is(false));
        assertThat(BuiltInType.IntArray.isSubtractableFrom(UndefinedType.Instance), is(true));
    }

    @Test
    public void testIsMultipliableWith() {
        // Boolean
        assertThat(BuiltInType.Boolean.isMultipliableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isMultipliableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isMultipliableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Boolean.isMultipliableWith(classType), is(false));
        assertThat(BuiltInType.Boolean.isMultipliableWith(UndefinedType.Instance), is(true));

        // Int
        assertThat(BuiltInType.Int.isMultipliableWith(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isMultipliableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isMultipliableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isMultipliableWith(classType), is(false));
        assertThat(BuiltInType.Int.isMultipliableWith(UndefinedType.Instance), is(true));


        // IntArray
        assertThat(BuiltInType.IntArray.isMultipliableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isMultipliableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isMultipliableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isMultipliableWith(classType), is(false));
        assertThat(BuiltInType.IntArray.isMultipliableWith(UndefinedType.Instance), is(true));
    }

    @Test
    public void testIsDisjunctableWith() {
        // Boolean
        assertThat(BuiltInType.Boolean.isDisjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isDisjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isDisjunctableWith(BuiltInType.Boolean), is(true));
        assertThat(BuiltInType.Boolean.isDisjunctableWith(classType), is(false));
        assertThat(BuiltInType.Boolean.isDisjunctableWith(UndefinedType.Instance), is(true));

        // Int
        assertThat(BuiltInType.Int.isDisjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Int.isDisjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isDisjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isDisjunctableWith(classType), is(false));
        assertThat(BuiltInType.Int.isDisjunctableWith(UndefinedType.Instance), is(true));

        // IntArray
        assertThat(BuiltInType.IntArray.isDisjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isDisjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isDisjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isDisjunctableWith(classType), is(false));
        assertThat(BuiltInType.IntArray.isDisjunctableWith(UndefinedType.Instance), is(true));
    }

    @Test
    public void testIsConjunctableWith() {
        // Boolean
        assertThat(BuiltInType.Boolean.isConjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isConjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isConjunctableWith(BuiltInType.Boolean), is(true));
        assertThat(BuiltInType.Boolean.isConjunctableWith(classType), is(false));
        assertThat(BuiltInType.Boolean.isConjunctableWith(UndefinedType.Instance), is(true));

        // Int
        assertThat(BuiltInType.Int.isConjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Int.isConjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isConjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isConjunctableWith(classType), is(false));
        assertThat(BuiltInType.Int.isConjunctableWith(UndefinedType.Instance), is(true));

        // IntArray
        assertThat(BuiltInType.IntArray.isConjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isConjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isConjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isConjunctableWith(classType), is(false));
        assertThat(BuiltInType.IntArray.isConjunctableWith(UndefinedType.Instance), is(true));
    }

    @Test
    public void testIsUndefined() {
        assertThat(BuiltInType.Boolean.isUndefined(), is(false));
        assertThat(BuiltInType.Int.isUndefined(), is(false));
        assertThat(BuiltInType.IntArray.isUndefined(), is(false));
    }
}
