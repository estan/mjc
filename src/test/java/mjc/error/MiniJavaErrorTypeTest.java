package mjc.error;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

public class MiniJavaErrorTypeTest {

    @Test
    public void testAllCodesUnique() {
        // Check that all error type codes are unique.
        Set<Integer> codes = new HashSet<>();
        for (MiniJavaErrorType type : MiniJavaErrorType.values()) {
            assertThat("non-unique error type code " + type.getCode(), codes.contains(type.getCode()), is(false));
            codes.add(type.getCode());
        }
    }

}
