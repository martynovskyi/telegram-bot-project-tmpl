package demo.project.bot.main.event;

import demo.project.bot.BotContext;
import demo.project.component.PrettyJsonLogger;
import demo.project.component.TgChannelLogger;
import demo.project.persistence.account.constant.TelegramChatStatus;
import demo.project.persistence.account.service.AccountService;
import com.motokyi.tg.bot_api.api.constant.ChatMemberStatus;
import com.motokyi.tg.bot_api.api.type.chat.Chat;
import com.motokyi.tg.bot_api.api.type.chat_member.ChatMemberUpdated;
import com.motokyi.tg.bot_api.api.type.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMemberEventsConsumer {

    private final BotContext mainBotContext;
    private final AccountService accountService;
    private final PrettyJsonLogger jsonLogger;
    private final TgChannelLogger channelLogger;

    private static boolean isReactivation(ChatMemberUpdated chatMemberEvent) {
        return ChatMemberStatus.KICKED.equals(chatMemberEvent.getOldChatMember().getStatus()) &&
               ChatMemberStatus.MEMBER.equals(chatMemberEvent.getNewChatMember().getStatus());
    }

    private static boolean isKick(ChatMemberUpdated chatMemberEvent) {
        return ChatMemberStatus.MEMBER.equals(chatMemberEvent.getOldChatMember().getStatus()) &&
               ChatMemberStatus.KICKED.equals(chatMemberEvent.getNewChatMember().getStatus());
    }

    public Mono<Void> consume(ChatMemberUpdated chatMemberEvent) {
        User user = chatMemberEvent.getFrom();
        Chat chat = chatMemberEvent.getChat();
        Mono<ChatMemberUpdated> event = Mono.just(chatMemberEvent);
        log.info("Chat member event handler. (event.user_name)={} (event.user_id)={}", user.getUserName(), user.getId());

        if (isReactivation(chatMemberEvent)) {
            return accountService.findAndSaveWithStatus(user.getId(), TelegramChatStatus.REACTIVATED)
                    .flatMap(ev -> channelLogger.notifyUserStatusReactivated(mainBotContext.name(), user, chat))
                    .then();
        }
        if (isKick(chatMemberEvent)) {
            return accountService.findAndSaveWithStatus(user.getId(), TelegramChatStatus.KICKED)
                    .flatMap(ev -> channelLogger.notifyUserStatusKicked(mainBotContext.name(), user, chat))
                    .then();
        }
        return event
                .doOnSuccess(e -> jsonLogger.warn("No actions taken", e, log))
                .then();
    }
}
