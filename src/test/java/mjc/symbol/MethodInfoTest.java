package mjc.symbol;

import mjc.types.BuiltInType;
import mjc.types.ClassType;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.is;

public class MethodInfoTest {

    @Test
    public void testEnterLeaveBlock() {
        /*
         * We build the following test case:
         *
         *  1  int f(boolean p1) {   // Enter block 0
         *  2     int l1;
         *  3     {                  // Enter block 1
         *  4         int l2;
         *  5         {              // Enter block 2
         *  6             int[] l3;
         *  7         }              // Leave block 2
         *  8         {              // Enter block 3
         *  9             int[] l4
         * 10         }              // Leave block 3
         * 11      }                 // Leave block 1
         * 12  }                     // Leave block 0
         *
         * And check at each block enter/leave that the correct variables are in scope.
         */
        MethodInfo f;
        VariableInfo p1 = null, l1 = null, l2 = null, l3 = null, l4 = null;

        f = new MethodInfo("f", BuiltInType.Int, 1, 1);

        for (int run = 0; run < 2; ++run) {
            f.enterBlock(); // Enter block 0.

            if (run == 0) { // In the first iteration we add variables.
                p1 = f.addParameter("p1", BuiltInType.Boolean, 1, 15);
                l1  =f.addLocal("l1", BuiltInType.Int, 2, 4);
            } // Else: In the second iteration we just check.

            assertThat(f.getParameter("p1"), sameInstance(p1)); // Just became visible.
            assertThat(f.getLocal("l1"), sameInstance(l1));     // Just became visible.

            f.enterBlock(); // Enter block 1.

            if (run == 0) { // In the first iteration we add variables.
                l2 = f.addLocal("l2", BuiltInType.Int, 4, 8);
            } // Else: In the second iteration we just check.

            assertThat(f.getLocal("l2"), sameInstance(l2));     // Just became visible.
            assertThat(f.getParameter("p1"), sameInstance(p1)); // Still visible.
            assertThat(f.getLocal("l1"), sameInstance(l1));     // Still visible.

            f.enterBlock(); // Enter block 2.

            if (run == 0) { // In the first iteration we add variables.
                l3 = f.addLocal("l3", BuiltInType.IntArray, 6, 12);
            } // Else: In the second iteration we just check.

            assertThat(f.getLocal("l3"), sameInstance(l3));     // Just became visible.
            assertThat(f.getParameter("p1"), sameInstance(p1)); // Still visible.
            assertThat(f.getLocal("l1"), sameInstance(l1));     // Still visible.
            assertThat(f.getLocal("l2"), sameInstance(l2));     // Still visible.

            f.leaveBlock(); // Leave block 2.

            assertThat(f.getParameter("p1"), sameInstance(p1)); // Still visible.
            assertThat(f.getLocal("l1"), sameInstance(l1));     // Still visible.
            assertThat(f.getLocal("l2"), sameInstance(l2));     // Still visible.
            assertThat(f.getLocal("l3"), nullValue());          // No longer visible.

            f.enterBlock(); // Enter block 3.

            if (run == 0) { // In the first iteration we add variables.
                l4 = f.addLocal("l4", BuiltInType.Int, 9, 12);
            } // Else: In the second iteration we just check.

            assertThat(f.getLocal("l4"), sameInstance(l4));     // Just became visible.
            assertThat(f.getParameter("p1"), sameInstance(p1)); // Still visible.
            assertThat(f.getLocal("l1"), sameInstance(l1));     // Still visible.
            assertThat(f.getLocal("l2"), sameInstance(l2));     // Still visible.
            assertThat(f.getLocal("l3"), nullValue());          // No longer visible.

            f.leaveBlock(); // Leave block 3.

            assertThat(f.getParameter("p1"), sameInstance(p1)); // Still visible.
            assertThat(f.getLocal("l1"), sameInstance(l1));     // Still visible.
            assertThat(f.getLocal("l2"), sameInstance(l2));     // Still visible.
            assertThat(f.getLocal("l3"), nullValue());          // No longer visible.
            assertThat(f.getLocal("l4"), nullValue());          // No longer visible.

            f.leaveBlock(); // Leave block 1.

            assertThat(f.getParameter("p1"), sameInstance(p1)); // Still visible.
            assertThat(f.getLocal("l1"), sameInstance(l1));     // Still visible.
            assertThat(f.getLocal("l2"), nullValue());          // No longer visible.
            assertThat(f.getLocal("l3"), nullValue());          // No longer visible.
            assertThat(f.getLocal("l4"), nullValue());          // No longer visible.

            f.leaveBlock(); // Leave block 0.

            assertThat(f.getParameter("p1"), sameInstance(p1)); // Still visible.
            assertThat(f.getLocal("l1"), nullValue());          // No longer visible.
            assertThat(f.getLocal("l2"), nullValue());          // No longer visible.
            assertThat(f.getLocal("l3"), nullValue());          // No longer visible.
            assertThat(f.getLocal("l4"), nullValue());          // No longer visible.
        }
    }

    @Test
    public void testDescriptor() {
        ClassType classA = new ClassType("ClassA");

        MethodInfo f = new MethodInfo("f", BuiltInType.Int, 42, 42);

        f.enterBlock();
        f.addParameter("p1", BuiltInType.IntArray, 42, 42);
        f.addParameter("p2", BuiltInType.Boolean, 42, 42);
        f.addParameter("p3", BuiltInType.Int, 42, 42);
        f.addParameter("p4", classA, 42, 42);
        f.leaveBlock();

        assertThat(f.descriptor(), is("([IZILClassA;)I"));
    }

    @Test
    public void testGetNumVariables() {
        MethodInfo f = new MethodInfo("f", BuiltInType.Int, 42, 42);

        f.enterBlock();
        f.addParameter("p1", BuiltInType.IntArray, 42, 42);
        f.addParameter("p2", BuiltInType.Boolean, 42, 42);
        f.addLocal("l1", BuiltInType.Int, 9, 12);
        f.addLocal("l2", BuiltInType.Int, 9, 12);
        f.addLocal("l3", BuiltInType.Int, 9, 12);
        f.leaveBlock();

        assertThat(f.getNumVariables(), is(5));
    }
}
