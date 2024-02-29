package demo.project.constant;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandTest {

    public static Stream<Arguments> validValues() {
        return Stream.of(
                Arguments.of(Command.START, "/start"),
                Arguments.of(Command.HELP, "/help")
        );
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"/dummy_command", "/service_info_"})
    void of_empty(String command) {
        assertTrue(Command.of(command).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("validValues")
    void of_success(Command expected, String command) {
        assertEquals(expected, Command.of(command).get());
    }
}