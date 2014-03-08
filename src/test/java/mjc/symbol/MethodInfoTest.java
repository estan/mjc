package mjc.symbol;

import mjc.types.BuiltInType;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;

public class MethodInfoTest {

    @Test
    public void testMethodInfo() {
        /*
         * We build the following test case:
         *
         *  1  int f(boolean p1) {   // Enter block 0
         *  2     long l1;
         *  3     {                  // Enter block 1
         *  4         int l2;
         *  5         {              // Enter block 2
         *  6             int[] l3;
         *  7         }              // Leave block 2
         *  8         {              // Enter block 3
         *  9             long[] l4
         * 10         }              // Leave block 3
         * 11      }                 // Leave block 1
         * 12  }                     // Leave block 0
         *
         * And check at each block enter/leave that the correct variables are in scope.
         */

        MethodInfo f = new MethodInfo("f", BuiltInType.Integer, 1, 1);
        assertThat(f.getName(), is("f"));
        assertThat(f.getReturnType(), sameInstance(BuiltInType.Integer));

        f.enterBlock(); // Enter block 0.

        // Add parameter p1.
        f.addParameter(new VariableInfo("p1", BuiltInType.Boolean, 1, 15));
        VariableInfo p1 = f.getParameter("p1");
        assertThat(p1, notNullValue());
        assertThat(p1.getName(), is("p1"));
        assertThat(p1.getType(), sameInstance(BuiltInType.Boolean));
        assertThat(p1.getBlock(), is(0));

        // Add local l1.
        f.addLocal(new VariableInfo("l1", BuiltInType.Long, 2, 4));
        VariableInfo l1 = f.getLocal("l1");
        assertThat(l1, notNullValue());
        assertThat(l1.getName(), is("l1"));
        assertThat(l1.getType(), sameInstance(BuiltInType.Long));
        assertThat(l1.getBlock(), is(0));

        f.enterBlock(); // Enter block 1.

        // p1 and l1 still in scope.
        p1 = f.getParameter("p1");
        assertThat(p1, notNullValue());
        assertThat(p1.getName(), is("p1"));
        assertThat(p1.getType(), sameInstance(BuiltInType.Boolean));
        assertThat(p1.getBlock(), is(0));
        l1 = f.getLocal("l1");
        assertThat(l1, notNullValue());
        assertThat(l1.getName(), is("l1"));
        assertThat(l1.getType(), sameInstance(BuiltInType.Long));
        assertThat(l1.getBlock(), is(0));

        // Add local l2.
        f.addLocal(new VariableInfo("l2", BuiltInType.Integer, 4, 8));
        VariableInfo l2 = f.getLocal("l2");
        assertThat(l2, notNullValue());
        assertThat(l2.getName(), is("l2"));
        assertThat(l2.getType(), sameInstance(BuiltInType.Integer));
        assertThat(l2.getBlock(), is(1));

        // Enter block 2.
        f.enterBlock();

        // p1, l1 and l2 still in scope.
        p1 = f.getParameter("p1");
        assertThat(p1, notNullValue());
        assertThat(p1.getName(), is("p1"));
        assertThat(p1.getType(), sameInstance(BuiltInType.Boolean));
        assertThat(p1.getBlock(), is(0));
        l1 = f.getLocal("l1");
        assertThat(l1, notNullValue());
        assertThat(l1.getName(), is("l1"));
        assertThat(l1.getType(), sameInstance(BuiltInType.Long));
        assertThat(l1.getBlock(), is(0));
        l2 = f.getLocal("l2");
        assertThat(l2, notNullValue());
        assertThat(l2.getName(), is("l2"));
        assertThat(l2.getType(), sameInstance(BuiltInType.Integer));
        assertThat(l2.getBlock(), is(1));

        // Add local l3.
        f.addLocal(new VariableInfo("l3", BuiltInType.IntegerArray, 6, 12));
        VariableInfo l3 = f.getLocal("l3");
        assertThat(l3, notNullValue());
        assertThat(l3.getName(), is("l3"));
        assertThat(l3.getType(), sameInstance(BuiltInType.IntegerArray));
        assertThat(l3.getBlock(), is(2));

        f.leaveBlock(); // Leave block 2.

        // p1, l1 and l2 still in scope, but not l3.
        p1 = f.getParameter("p1");
        assertThat(p1, notNullValue());
        assertThat(p1.getName(), is("p1"));
        assertThat(p1.getType(), sameInstance(BuiltInType.Boolean));
        assertThat(p1.getBlock(), is(0));

        l1 = f.getLocal("l1");
        assertThat(l1, notNullValue());
        assertThat(l1.getName(), is("l1"));
        assertThat(l1.getType(), sameInstance(BuiltInType.Long));
        assertThat(l1.getBlock(), is(0));

        l2 = f.getLocal("l2");
        assertThat(l2, notNullValue());
        assertThat(l2.getName(), is("l2"));
        assertThat(l2.getType(), sameInstance(BuiltInType.Integer));
        assertThat(l2.getBlock(), is(1));

        assertThat(f.getLocal("l3"), nullValue());

        f.enterBlock(); // Enter block 3.

        // p1, l1 and l2 still in scope, but not l3.
        p1 = f.getParameter("p1");
        assertThat(p1, notNullValue());
        assertThat(p1.getName(), is("p1"));
        assertThat(p1.getType(), sameInstance(BuiltInType.Boolean));
        assertThat(p1.getBlock(), is(0));

        l1 = f.getLocal("l1");
        assertThat(l1, notNullValue());
        assertThat(l1.getName(), is("l1"));
        assertThat(l1.getType(), sameInstance(BuiltInType.Long));
        assertThat(l1.getBlock(), is(0));

        l2 = f.getLocal("l2");
        assertThat(l2, notNullValue());
        assertThat(l2.getName(), is("l2"));
        assertThat(l2.getType(), sameInstance(BuiltInType.Integer));
        assertThat(l2.getBlock(), is(1));

        assertThat(f.getLocal("l3"), nullValue());

        // Add local l4.
        f.addLocal(new VariableInfo("l4", BuiltInType.LongArray, 9, 12));
        VariableInfo l4 = f.getLocal("l4");
        assertThat(l4, notNullValue());
        assertThat(l4.getName(), is("l4"));
        assertThat(l4.getType(), sameInstance(BuiltInType.LongArray));
        assertThat(l4.getBlock(), is(3));

        f.leaveBlock(); // Leave block 3.

        // p1, l1 and l2 still in scope, but neither l3 nor l4 are in scope.
        p1 = f.getParameter("p1");
        assertThat(p1, notNullValue());
        assertThat(p1.getName(), is("p1"));
        assertThat(p1.getType(), sameInstance(BuiltInType.Boolean));
        assertThat(p1.getBlock(), is(0));

        l1 = f.getLocal("l1");
        assertThat(l1, notNullValue());
        assertThat(l1.getName(), is("l1"));
        assertThat(l1.getType(), sameInstance(BuiltInType.Long));
        assertThat(l1.getBlock(), is(0));

        l2 = f.getLocal("l2");
        assertThat(l2, notNullValue());
        assertThat(l2.getName(), is("l2"));
        assertThat(l2.getType(), sameInstance(BuiltInType.Integer));
        assertThat(l2.getBlock(), is(1));

        assertThat(f.getLocal("l3"), nullValue());
        assertThat(f.getLocal("l4"), nullValue());

        f.leaveBlock(); // Leave block 1.

        // p1 and l1 still in scope, but neither l2, l3 nor l4 are in scope.
        p1 = f.getParameter("p1");
        assertThat(p1, notNullValue());
        assertThat(p1.getName(), is("p1"));
        assertThat(p1.getType(), sameInstance(BuiltInType.Boolean));
        assertThat(p1.getBlock(), is(0));

        l1 = f.getLocal("l1");
        assertThat(l1, notNullValue());
        assertThat(l1.getName(), is("l1"));
        assertThat(l1.getType(), sameInstance(BuiltInType.Long));
        assertThat(l1.getBlock(), is(0));

        assertThat(f.getLocal("l2"), nullValue());
        assertThat(f.getLocal("l3"), nullValue());
        assertThat(f.getLocal("l4"), nullValue());

        f.leaveBlock(); // Leave block 0.

        // Everything is out of scope.
        assertThat(f.getParameter("p1"), nullValue());
        assertThat(f.getLocal("l1"), nullValue());
        assertThat(f.getLocal("l2"), nullValue());
        assertThat(f.getLocal("l3"), nullValue());
        assertThat(f.getLocal("l4"), nullValue());
    }
}
