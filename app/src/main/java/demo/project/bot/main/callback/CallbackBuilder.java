package demo.project.bot.main.callback;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public final class CallbackBuilder {
    public static final String SPLITTER = "#";

    public static Optional<String> extractPrefix(String query) {
        return StringUtils.isBlank(query) ? Optional.empty() : Optional.of(query.split(SPLITTER)[0]);
    }

    public static String extractPrimaryId(String query) {
        return query.split(SPLITTER)[1];
    }
}
