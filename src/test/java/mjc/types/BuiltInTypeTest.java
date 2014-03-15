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
        assertThat(BuiltInType.Long.isBuiltIn(), is(true));
        assertThat(BuiltInType.IntArray.isBuiltIn(), is(true));
        assertThat(BuiltInType.LongArray.isBuiltIn(), is(true));
    }

    @Test
    public void testIsInt() {
        assertThat(BuiltInType.Boolean.isInt(), is(false));
        assertThat(BuiltInType.Int.isInt(), is(true));
        assertThat(BuiltInType.Long.isInt(), is(false));
        assertThat(BuiltInType.IntArray.isInt(), is(false));
        assertThat(BuiltInType.LongArray.isInt(), is(false));
    }

    @Test
    public void testIsLong() {
        assertThat(BuiltInType.Boolean.isLong(), is(false));
        assertThat(BuiltInType.Int.isLong(), is(false));
        assertThat(BuiltInType.Long.isLong(), is(true));
        assertThat(BuiltInType.IntArray.isLong(), is(false));
        assertThat(BuiltInType.LongArray.isLong(), is(false));
    }

    @Test
    public void testIsInteger() {
        assertThat(BuiltInType.Boolean.isInteger(), is(false));
        assertThat(BuiltInType.Int.isInteger(), is(true));
        assertThat(BuiltInType.Long.isInteger(), is(true));
        assertThat(BuiltInType.IntArray.isInteger(), is(false));
        assertThat(BuiltInType.LongArray.isInteger(), is(false));
    }

    @Test
    public void testIsIntArray() {
        assertThat(BuiltInType.Boolean.isIntArray(), is(false));
        assertThat(BuiltInType.Int.isIntArray(), is(false));
        assertThat(BuiltInType.Long.isIntArray(), is(false));
        assertThat(BuiltInType.IntArray.isIntArray(), is(true));
        assertThat(BuiltInType.LongArray.isIntArray(), is(false));
    }

    @Test
    public void testIsLongArray() {
        assertThat(BuiltInType.Boolean.isLongArray(), is(false));
        assertThat(BuiltInType.Int.isLongArray(), is(false));
        assertThat(BuiltInType.Long.isLongArray(), is(false));
        assertThat(BuiltInType.IntArray.isLongArray(), is(false));
        assertThat(BuiltInType.LongArray.isLongArray(), is(true));
    }

    @Test
    public void testIsArray() {
        assertThat(BuiltInType.Boolean.isArray(), is(false));
        assertThat(BuiltInType.Int.isArray(), is(false));
        assertThat(BuiltInType.Long.isArray(), is(false));
        assertThat(BuiltInType.IntArray.isArray(), is(true));
        assertThat(BuiltInType.LongArray.isArray(), is(true));
    }

    @Test
    public void testIsBoolean() {
        assertThat(BuiltInType.Boolean.isBoolean(), is(true));
        assertThat(BuiltInType.Int.isBoolean(), is(false));
        assertThat(BuiltInType.Long.isBoolean(), is(false));
        assertThat(BuiltInType.IntArray.isBoolean(), is(false));
        assertThat(BuiltInType.LongArray.isBoolean(), is(false));
    }

    @Test
    public void testIsClass() {
        assertThat(BuiltInType.Boolean.isClass(), is(false));
        assertThat(BuiltInType.Int.isClass(), is(false));
        assertThat(BuiltInType.Long.isClass(), is(false));
        assertThat(BuiltInType.IntArray.isClass(), is(false));
        assertThat(BuiltInType.LongArray.isClass(), is(false));
    }

    @Test
    public void testIsAssignableTo() {
        // Boolean
        assertThat(BuiltInType.Boolean.isAssignableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isAssignableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isAssignableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Boolean.isAssignableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Boolean.isAssignableTo(BuiltInType.Boolean), is(true));
        assertThat(BuiltInType.Boolean.isAssignableTo(classType), is(false));

        // Int
        assertThat(BuiltInType.Int.isAssignableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isAssignableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isAssignableTo(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Int.isAssignableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Int.isAssignableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isAssignableTo(classType), is(false));

        // IntArray
        assertThat(BuiltInType.IntArray.isAssignableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isAssignableTo(BuiltInType.IntArray), is(true));
        assertThat(BuiltInType.IntArray.isAssignableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.IntArray.isAssignableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.IntArray.isAssignableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isAssignableTo(classType), is(false));

        // Long
        assertThat(BuiltInType.Long.isAssignableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Long.isAssignableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Long.isAssignableTo(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Long.isAssignableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Long.isAssignableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Long.isAssignableTo(classType), is(false));

        // LongArray
        assertThat(BuiltInType.LongArray.isAssignableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.LongArray.isAssignableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.LongArray.isAssignableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.LongArray.isAssignableTo(BuiltInType.LongArray), is(true));
        assertThat(BuiltInType.LongArray.isAssignableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.LongArray.isAssignableTo(classType), is(false));
    }

    @Test
    public void testIsEqualComparableTo() {
        // Boolean
        assertThat(BuiltInType.Boolean.isEqualComparableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isEqualComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isEqualComparableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Boolean.isEqualComparableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Boolean.isEqualComparableTo(BuiltInType.Boolean), is(true));
        assertThat(BuiltInType.Boolean.isEqualComparableTo(classType), is(false));

        // Int
        assertThat(BuiltInType.Int.isEqualComparableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isEqualComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isEqualComparableTo(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Int.isEqualComparableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Int.isEqualComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isEqualComparableTo(classType), is(false));

        // IntArray
        assertThat(BuiltInType.IntArray.isEqualComparableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isEqualComparableTo(BuiltInType.IntArray), is(true));
        assertThat(BuiltInType.IntArray.isEqualComparableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.IntArray.isEqualComparableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.IntArray.isEqualComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isEqualComparableTo(classType), is(false));

        // Long
        assertThat(BuiltInType.Long.isEqualComparableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Long.isEqualComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Long.isEqualComparableTo(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Long.isEqualComparableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Long.isEqualComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Long.isEqualComparableTo(classType), is(false));

        // LongArray
        assertThat(BuiltInType.LongArray.isEqualComparableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.LongArray.isEqualComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.LongArray.isEqualComparableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.LongArray.isEqualComparableTo(BuiltInType.LongArray), is(true));
        assertThat(BuiltInType.LongArray.isEqualComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.LongArray.isEqualComparableTo(classType), is(false));
    }

    @Test
    public void testIsRelationalComparableTo() {
        // Boolean
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Boolean.isRelationalComparableTo(classType), is(false));

        // Int
        assertThat(BuiltInType.Int.isRelationalComparableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isRelationalComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isRelationalComparableTo(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Int.isRelationalComparableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Int.isRelationalComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isRelationalComparableTo(classType), is(false));

        // IntArray
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isRelationalComparableTo(classType), is(false));

        // Long
        assertThat(BuiltInType.Long.isRelationalComparableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Long.isRelationalComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Long.isRelationalComparableTo(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Long.isRelationalComparableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Long.isRelationalComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Long.isRelationalComparableTo(classType), is(false));

        // LongArray
        assertThat(BuiltInType.LongArray.isRelationalComparableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.LongArray.isRelationalComparableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.LongArray.isRelationalComparableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.LongArray.isRelationalComparableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.LongArray.isRelationalComparableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.LongArray.isRelationalComparableTo(classType), is(false));
    }

    @Test
    public void testIsAddableTo() {
        // Boolean
        assertThat(BuiltInType.Boolean.isAddableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isAddableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isAddableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Boolean.isAddableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Boolean.isAddableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Boolean.isAddableTo(classType), is(false));

        // Int
        assertThat(BuiltInType.Int.isAddableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isAddableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isAddableTo(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Int.isAddableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Int.isAddableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isAddableTo(classType), is(false));

        // IntArray
        assertThat(BuiltInType.IntArray.isAddableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isAddableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isAddableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.IntArray.isAddableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.IntArray.isAddableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isAddableTo(classType), is(false));

        // Long
        assertThat(BuiltInType.Long.isAddableTo(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Long.isAddableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Long.isAddableTo(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Long.isAddableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Long.isAddableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Long.isAddableTo(classType), is(false));

        // LongArray
        assertThat(BuiltInType.LongArray.isAddableTo(BuiltInType.Int), is(false));
        assertThat(BuiltInType.LongArray.isAddableTo(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.LongArray.isAddableTo(BuiltInType.Long), is(false));
        assertThat(BuiltInType.LongArray.isAddableTo(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.LongArray.isAddableTo(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.LongArray.isAddableTo(classType), is(false));
    }

    @Test
    public void testIsSubtractableFrom() {
        // Boolean
        assertThat(BuiltInType.Boolean.isSubtractableFrom(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isSubtractableFrom(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isSubtractableFrom(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Boolean.isSubtractableFrom(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Boolean.isSubtractableFrom(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Boolean.isSubtractableFrom(classType), is(false));

        // Int
        assertThat(BuiltInType.Int.isSubtractableFrom(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isSubtractableFrom(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isSubtractableFrom(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Int.isSubtractableFrom(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Int.isSubtractableFrom(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isSubtractableFrom(classType), is(false));

        // IntArray
        assertThat(BuiltInType.IntArray.isSubtractableFrom(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isSubtractableFrom(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isSubtractableFrom(BuiltInType.Long), is(false));
        assertThat(BuiltInType.IntArray.isSubtractableFrom(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.IntArray.isSubtractableFrom(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isSubtractableFrom(classType), is(false));

        // Long
        assertThat(BuiltInType.Long.isSubtractableFrom(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Long.isSubtractableFrom(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Long.isSubtractableFrom(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Long.isSubtractableFrom(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Long.isSubtractableFrom(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Long.isSubtractableFrom(classType), is(false));

        // LongArray
        assertThat(BuiltInType.LongArray.isSubtractableFrom(BuiltInType.Int), is(false));
        assertThat(BuiltInType.LongArray.isSubtractableFrom(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.LongArray.isSubtractableFrom(BuiltInType.Long), is(false));
        assertThat(BuiltInType.LongArray.isSubtractableFrom(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.LongArray.isSubtractableFrom(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.LongArray.isSubtractableFrom(classType), is(false));
    }

    @Test
    public void testIsMultipliableWith() {
        // Boolean
        assertThat(BuiltInType.Boolean.isMultipliableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isMultipliableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isMultipliableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Boolean.isMultipliableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Boolean.isMultipliableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Boolean.isMultipliableWith(classType), is(false));

        // Int
        assertThat(BuiltInType.Int.isMultipliableWith(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Int.isMultipliableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isMultipliableWith(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Int.isMultipliableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Int.isMultipliableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isMultipliableWith(classType), is(false));

        // IntArray
        assertThat(BuiltInType.IntArray.isMultipliableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isMultipliableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isMultipliableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.IntArray.isMultipliableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.IntArray.isMultipliableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isMultipliableWith(classType), is(false));

        // Long
        assertThat(BuiltInType.Long.isMultipliableWith(BuiltInType.Int), is(true));
        assertThat(BuiltInType.Long.isMultipliableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Long.isMultipliableWith(BuiltInType.Long), is(true));
        assertThat(BuiltInType.Long.isMultipliableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Long.isMultipliableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Long.isMultipliableWith(classType), is(false));

        // LongArray
        assertThat(BuiltInType.LongArray.isMultipliableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.LongArray.isMultipliableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.LongArray.isMultipliableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.LongArray.isMultipliableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.LongArray.isMultipliableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.LongArray.isMultipliableWith(classType), is(false));
    }

    @Test
    public void testIsDisjunctableWith() {
        // Boolean
        assertThat(BuiltInType.Boolean.isDisjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isDisjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isDisjunctableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Boolean.isDisjunctableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Boolean.isDisjunctableWith(BuiltInType.Boolean), is(true));
        assertThat(BuiltInType.Boolean.isDisjunctableWith(classType), is(false));

        // Int
        assertThat(BuiltInType.Int.isDisjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Int.isDisjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isDisjunctableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Int.isDisjunctableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Int.isDisjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isDisjunctableWith(classType), is(false));

        // IntArray
        assertThat(BuiltInType.IntArray.isDisjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isDisjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isDisjunctableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.IntArray.isDisjunctableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.IntArray.isDisjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isDisjunctableWith(classType), is(false));

        // Long
        assertThat(BuiltInType.Long.isDisjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Long.isDisjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Long.isDisjunctableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Long.isDisjunctableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Long.isDisjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Long.isDisjunctableWith(classType), is(false));

        // LongArray
        assertThat(BuiltInType.LongArray.isDisjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.LongArray.isDisjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.LongArray.isDisjunctableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.LongArray.isDisjunctableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.LongArray.isDisjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.LongArray.isDisjunctableWith(classType), is(false));
    }

    @Test
    public void testIsConjunctableWith() {
        // Boolean
        assertThat(BuiltInType.Boolean.isConjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Boolean.isConjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Boolean.isConjunctableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Boolean.isConjunctableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Boolean.isConjunctableWith(BuiltInType.Boolean), is(true));
        assertThat(BuiltInType.Boolean.isConjunctableWith(classType), is(false));

        // Int
        assertThat(BuiltInType.Int.isConjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Int.isConjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Int.isConjunctableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Int.isConjunctableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Int.isConjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Int.isConjunctableWith(classType), is(false));

        // IntArray
        assertThat(BuiltInType.IntArray.isConjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.IntArray.isConjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.IntArray.isConjunctableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.IntArray.isConjunctableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.IntArray.isConjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.IntArray.isConjunctableWith(classType), is(false));

        // Long
        assertThat(BuiltInType.Long.isConjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.Long.isConjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.Long.isConjunctableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.Long.isConjunctableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.Long.isConjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.Long.isConjunctableWith(classType), is(false));

        // LongArray
        assertThat(BuiltInType.LongArray.isConjunctableWith(BuiltInType.Int), is(false));
        assertThat(BuiltInType.LongArray.isConjunctableWith(BuiltInType.IntArray), is(false));
        assertThat(BuiltInType.LongArray.isConjunctableWith(BuiltInType.Long), is(false));
        assertThat(BuiltInType.LongArray.isConjunctableWith(BuiltInType.LongArray), is(false));
        assertThat(BuiltInType.LongArray.isConjunctableWith(BuiltInType.Boolean), is(false));
        assertThat(BuiltInType.LongArray.isConjunctableWith(classType), is(false));
    }

    @Test
    public void testIsUndefined() {
        assertThat(BuiltInType.Boolean.isUndefined(), is(false));
        assertThat(BuiltInType.Int.isUndefined(), is(false));
        assertThat(BuiltInType.Long.isUndefined(), is(false));
        assertThat(BuiltInType.IntArray.isUndefined(), is(false));
        assertThat(BuiltInType.LongArray.isUndefined(), is(false));
    }

    @Test
    public void testIsUnsupported() {
        assertThat(BuiltInType.Boolean.isUnsupported(), is(false));
        assertThat(BuiltInType.Int.isUnsupported(), is(false));
        assertThat(BuiltInType.Long.isUnsupported(), is(false));
        assertThat(BuiltInType.IntArray.isUnsupported(), is(false));
        assertThat(BuiltInType.LongArray.isUnsupported(), is(false));
    }
}
