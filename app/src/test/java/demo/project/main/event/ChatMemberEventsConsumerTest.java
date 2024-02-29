package demo.project.main.event;

import demo.project.TestObjects;
import demo.project.component.PrettyJsonLogger;
import demo.project.component.TgChannelLogger;
import demo.project.bot.main.event.ChatMemberEventsConsumer;
import demo.project.persistence.account.constant.TelegramChatStatus;
import demo.project.persistence.account.entity.Telegram;
import demo.project.persistence.account.service.AccountService;
import com.motokyi.tg.bot_api.api.type.chat_member.ChatMember;
import com.motokyi.tg.bot_api.api.type.chat_member.ChatMemberUpdated;
import com.motokyi.tg.bot_api.bot.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChatMemberEventsConsumerTest {

    @Mock
    Bot bot;
    @Mock
    AccountService accountService;
    @Mock
    PrettyJsonLogger jsonLogger;
    @Mock
    TgChannelLogger channelLogger;

    ChatMemberEventsConsumer eventsConsumer;

    private static ChatMember buildChatMember(String status) {
        ChatMember newChatMember = new ChatMember();
        newChatMember.setStatus(status);
        return newChatMember;
    }

    private static ChatMemberUpdated buildChatMemberUpdated() {
        ChatMemberUpdated chatMemberEvent = new ChatMemberUpdated();
        chatMemberEvent.setChat(TestObjects.chat());
        chatMemberEvent.setFrom(TestObjects.user());
        return chatMemberEvent;
    }

    @BeforeEach
    void setUp() {
        eventsConsumer = new ChatMemberEventsConsumer(
                TestObjects.context(bot),
                accountService,
                jsonLogger,
                channelLogger);
    }

    @Test
    void consume_kick() {
        ChatMemberUpdated chatMemberEvent = buildChatMemberUpdated();
        chatMemberEvent.setNewChatMember(buildChatMember("kicked"));
        chatMemberEvent.setOldChatMember(buildChatMember("member"));
        when(accountService.findAndSaveWithStatus(TestObjects.USER_ID, TelegramChatStatus.KICKED))
                .thenReturn(Mono.just(new Telegram()));
        when(channelLogger.notifyUserStatusKicked(any(), any(), any()))
                .thenReturn(Mono.empty());


        StepVerifier.create(eventsConsumer.consume(chatMemberEvent))
                .verifyComplete();

        verify(accountService).findAndSaveWithStatus(TestObjects.USER_ID, TelegramChatStatus.KICKED);
        verify(channelLogger).notifyUserStatusKicked(any(), any(), any());
    }

    @Test
    void consume_reactivation() {
        ChatMemberUpdated chatMemberEvent = buildChatMemberUpdated();
        chatMemberEvent.setNewChatMember(buildChatMember("member"));
        chatMemberEvent.setOldChatMember(buildChatMember("kicked"));
        when(accountService.findAndSaveWithStatus(TestObjects.USER_ID, TelegramChatStatus.REACTIVATED))
                .thenReturn(Mono.just(new Telegram()));
        when(channelLogger.notifyUserStatusReactivated(any(), any(), any()))
                .thenReturn(Mono.empty());

        StepVerifier.create(eventsConsumer.consume(chatMemberEvent))
                .verifyComplete();

        verify(accountService).findAndSaveWithStatus(TestObjects.USER_ID, TelegramChatStatus.REACTIVATED);
        verify(channelLogger).notifyUserStatusReactivated(any(), any(), any());
    }

    @Test
    void consume_unexpected() {
        ChatMemberUpdated chatMemberEvent = buildChatMemberUpdated();
        chatMemberEvent.setNewChatMember(new ChatMember());
        chatMemberEvent.setOldChatMember(new ChatMember());
        StepVerifier.create(eventsConsumer.consume(chatMemberEvent))
                .verifyComplete();
        verify(jsonLogger).warn(any(), any(), any());
        verifyNoInteractions(accountService, channelLogger);
    }

}