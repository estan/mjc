package mjc.types;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class ClassTypeTest {

    final ClassType classA1 = new ClassType("ClassA");
    final ClassType classA2 = new ClassType("ClassA");
    final ClassType classB = new ClassType("ClassB");

    @Test
    public void testIsClass() {
        assertThat(classA1.isClass(), is(true));
    }

    @Test
    public void testIsAssignableTo() {
        assertThat(classA1.isAssignableTo(BuiltInType.Boolean), is(false));
        assertThat(classA1.isAssignableTo(BuiltInType.Int), is(false));
        assertThat(classA1.isAssignableTo(BuiltInType.IntArray), is(false));
        assertThat(classA1.isAssignableTo(BuiltInType.Long), is(false));
        assertThat(classA1.isAssignableTo(BuiltInType.LongArray), is(false));
        assertThat(classA1.isAssignableTo(UndefinedType.Instance), is(true));
        assertThat(classA1.isAssignableTo(classA2), is(true)); // Same class
        assertThat(classA1.isAssignableTo(classB), is(false)); // Different class
    }

    @Test
    public void testIsEqualComparableTo() {
        assertThat(classA1.isEqualComparableTo(BuiltInType.Boolean), is(false));
        assertThat(classA1.isEqualComparableTo(BuiltInType.Int), is(false));
        assertThat(classA1.isEqualComparableTo(BuiltInType.IntArray), is(false));
        assertThat(classA1.isEqualComparableTo(BuiltInType.Long), is(false));
        assertThat(classA1.isEqualComparableTo(BuiltInType.LongArray), is(false));
        assertThat(classA1.isEqualComparableTo(UndefinedType.Instance), is(true));
        assertThat(classA1.isEqualComparableTo(classA2), is(true)); // Same class
        assertThat(classA1.isEqualComparableTo(classB), is(false)); // Different class
    }

}
