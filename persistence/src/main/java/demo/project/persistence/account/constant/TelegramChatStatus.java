package demo.project.persistence.account.constant;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum TelegramChatStatus {
    // length limit 8
    ACTIVE("active"),
    REACTIVATED("r_active"),
    KICKED("kicked");

    private final String value;

    TelegramChatStatus(String value) {
        this.value = value;
    }

    public static Optional<TelegramChatStatus> of(String status) {
        return Stream.of(TelegramChatStatus.values())
                .filter(c -> c.getValue().equals(status))
                .findFirst();
    }
}
