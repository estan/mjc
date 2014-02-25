package mjc.lexer;

import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import mjc.node.*;

/**
 * Some basic tests for the lexer.
 */
public class LexerTest {
    // To save some typing in the tests below.
    private final static Class<?> w = TWhitespace.class;

    /**
     * Test tokenization of comments.
     */
    @Test
    public void testComments() throws Exception {
        assertTokens("foo/* Hi + there,{]\\}\\]}[ öööl // * */bar",
                TIdentifier.class, TComment.class, TIdentifier.class);
        assertTokens("aa/*  \n  A ,, cool multi-\nline\ncomment*/12",
                TIdentifier.class, TComment.class, TInteger.class);
        assertTokens("bar//yep\n",
                TIdentifier.class, TComment.class);
        assertTokens("foo // yea man, the end of line is optional",
                TIdentifier.class, w, TComment.class);
        assertTokens("foo// but \nthis_is_not_a_comment",
                TIdentifier.class, TComment.class, TIdentifier.class);
        assertTokens("/* this comment /* // /** ends here: */",
                TComment.class);
    }

    /**
     * Test tokenization of nested (invalid) comment.
     */
    @Test(expected = AssertionError.class)
    public void testNestedComment() throws Exception {
        // We expect the assertion to fail with trailing input: "*/"
        assertTokens("/*/**/*/", TComment.class);
    }

    /**
     * Test tokenization of keywords.
     */
    @Test
    public void testKeywords() throws Exception {
        assertTokens("class public static void String return int " +
                     "boolean if else while System.out.println length " +
                     "true false this new",
                TClassKeyword.class, w, TPublicKeyword.class, w, TStaticKeyword.class, w,
                TVoidKeyword.class, w, TStringKeyword.class, w, TReturnKeyword.class, w,
                TIntKeyword.class, w, TBooleanKeyword.class, w, TIfKeyword.class, w,
                TElseKeyword.class, w, TWhileKeyword.class, w, TPrintlnKeyword.class, w,
                TLengthKeyword.class, w, TTrueKeyword.class, w, TFalseKeyword.class, w,
                TThisKeyword.class, w, TNewKeyword.class);
    }

    /**
     * Test tokenization of identifiers.
     */
    @Test
    public void testIdentifiers() throws Exception {
        assertTokens("this_is0_anIdentifier andthis but not this",
                TIdentifier.class, w, TIdentifier.class, w,
                TIdentifier.class, w, TIdentifier.class, w,
                TThisKeyword.class);
        assertTokens("03foo", TInteger.class, TIdentifier.class);
    }

    /**
     * Test tokenization of invalid identifier (leading underscore).
     */
    @Test(expected = LexerException.class)
    public void testIdentifierLeadingUnderscore() throws Exception {
        assertTokens("_invalid_identifier", TIdentifier.class);
    }

    /**
     * Test tokenization of integer literals.
     */
    @Test
    public void testInteger() throws Exception {
        assertTokens("022 263575432",
                TInteger.class, w, TInteger.class);
    }

    /**
     * Test tokenization of various short tokens.
     */
    @Test
    public void testShortTokens() throws Exception {
        assertTokens("asddf{ds[*+-}]&&bar! ,,(<).=;",
                TIdentifier.class, TLbrace.class, TIdentifier.class,
                TLbrack.class, TStar.class, TPlus.class, TMinus.class,
                TRbrace.class, TRbrack.class, TAnd.class, TIdentifier.class,
                TNot.class, w, TComma.class, TComma.class, TLparen.class,
                TLessThan.class, TRparen.class, TPeriod.class, TAssign.class,
                TSemicolon.class);
    }

    /**
     * Assert correct tokenization.
     *
     * This helper method asserts that the lexer will tokenize @a input into the
     * token stream specified by @a tokens.
     *
     * @param input
     *            An input string.
     * @param tokens
     *            Token stream to which @a input should be tokenized.
     * @throws Exception
     *             If the lexer failed or an I/O error occurred.
     */
    private void assertTokens(String input, Class<?>... tokens) throws Exception {
        Lexer lexer = new Lexer(new PushbackReader(new StringReader(input)));
        for (Class<?> token : tokens) {
            assertEquals(token, lexer.next().getClass());
        }
        assertEquals(EOF.class, lexer.next().getClass());
    }
}
