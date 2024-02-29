package demo.project.persistence;


import demo.project.persistence.account.constant.TelegramChatStatus;
import demo.project.persistence.account.entity.Account;
import demo.project.persistence.account.entity.Telegram;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class Entities {

    public static final Long ACCOUNT_ID = 1000L;

    private Entities() {
    }

    public static Account account() {
        return new Account();
    }

    public static Account accountSaved() {
        return Account.builder()
                .id(ACCOUNT_ID)
                .build();
    }


    public static Telegram telegramSaved(Long platformUserId) {
        return telegram(accountSaved(), platformUserId);
    }

    public static Telegram telegram(Account account, Long platformUserId) {
        assertNotNull(account);
        assertNotNull(account.getId());
        assertNotNull(platformUserId);
        return Telegram.builder()
                .accountId(account.getId())
                .userId(platformUserId)
                .directChatStatus(TelegramChatStatus.ACTIVE.getValue())
                .build();
    }
}
