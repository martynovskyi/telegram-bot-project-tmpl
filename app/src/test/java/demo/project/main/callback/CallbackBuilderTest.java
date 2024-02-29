package demo.project.main.callback;

import demo.project.bot.main.callback.CallbackBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CallbackBuilderTest {
    private static final Integer ID = 255;
    private static final String QUERY = "ctg#255";


    @ParameterizedTest
    @NullSource
    @EmptySource
    void extractPrefix_empty(String query) {
        assertTrue(CallbackBuilder.extractPrefix(query).isEmpty());
    }

    @Test
    void extractPrimaryId() {
        assertEquals(ID.toString(), CallbackBuilder.extractPrimaryId(QUERY));
    }
}