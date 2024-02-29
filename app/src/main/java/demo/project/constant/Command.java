package demo.project.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Command {

    START("/".concat(Value.START), false),
    HELP("/".concat(Value.HELP), false);

    private final String command;
    private final boolean dynamic;

    public static Optional<Command> of(String command) {
        if (StringUtils.isBlank(command)) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(cm -> cm.dynamic ? checkDynamic(cm, command) : cm.equal(command))
                .findFirst();
    }

    private static boolean checkDynamic(Command cm, String command) {
        return command.startsWith(cm.command) && command.length() > cm.command.length();
    }

    public boolean equal(String command) {
        return this.command.equals(command);
    }

    public boolean startsWith(String command) {
        return command.startsWith(this.command);
    }

    public static class Value {
        public static final String START = "start";
        public static final String HELP = "help";
    }
}
