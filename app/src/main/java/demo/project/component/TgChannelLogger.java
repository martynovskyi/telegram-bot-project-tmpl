package demo.project.component;

import demo.project.bot.BotManager;
import demo.project.config.properties.ApplicationProperties;
import demo.project.config.properties.BotRole;
import com.motokyi.tg.bot_api.api.type.Response;
import com.motokyi.tg.bot_api.api.type.chat.Chat;
import com.motokyi.tg.bot_api.api.type.message.Message;
import com.motokyi.tg.bot_api.api.type.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TgChannelLogger {
    private static final String NEW_USER_MESSAGE_FORMAT = """
            🟢 #%s #new_user
            name: %s %s
            id: %s
            username: %s
            lang: %s
            chatType: %s
            chatTitle: %s
            """;
    private static final String USER_REACTIVATE_MESSAGE_FORMAT = """
            🔵 #%s #user_react
            name: %s %s
            id: %s
            username: %s
            lang: %s
            chatType: %s
            chatTitle: %s
            """;
    private static final String USER_KICKED_MESSAGE_FORMAT = """
            🟠 #%s #user_kick
            name: %s %s
            id: %s
            username: %s
            lang: %s
            chatType: %s
            chatTitle: %s
            """;
    private final BotManager botManager;
    private final ApplicationProperties applicationProperties;
    private final PrettyJsonLogger bodyLogger;

    public Mono<Response<Message>> notifyException(String botContext, String message, Throwable throwable) {
        return botManager.getBot(BotRole.LOG)
                .flatMap(logBot -> {
                    final String text = "🔴 %s :: %s Exception %s %s \nStackTrace:\n %s"
                            .formatted(botContext,
                                    message,
                                    throwable.getClass().getSimpleName(),
                                    throwable,
                                    ExceptionUtils.getStackTrace(throwable));

                    return logBot.sendMessage(applicationProperties.getLogChat(), text);
                })
                .doOnSuccess(res -> log.info(res.getResult().toString()));
    }

    public Mono<Response<Message>> notifyNewUser(String botContext, Message startCommandMessage) {
        return botManager.getBot(BotRole.LOG)
                .flatMap(bot -> {
                    Chat chat = startCommandMessage.getChat();
                    User user = startCommandMessage.getFrom();
                    return bot.sendMessage(
                            applicationProperties.getLogChat(),
                            NEW_USER_MESSAGE_FORMAT
                                    .formatted(botContext,
                                            chat.getFirstName(), chat.getLastName(),
                                            chat.getId(),
                                            chat.getUserName(),
                                            user.getLanguageCode(),
                                            chat.getType(),
                                            chat.getTitle()));
                })
                .doOnSuccess(res -> bodyLogger.logIfNotSuccessful(res, log));
    }

    public Mono<Response<Message>> notifyUserStatusReactivated(String botContext, User user, Chat chat) {
        return botManager.getBot(BotRole.LOG)
                .flatMap(bot ->
                        bot.sendMessage(
                                applicationProperties.getLogChat(),
                                USER_REACTIVATE_MESSAGE_FORMAT
                                        .formatted(botContext,
                                                user.getFirstName(), user.getLastName(),
                                                user.getId(),
                                                user.getUserName(),
                                                user.getLanguageCode(),
                                                chat.getType(),
                                                chat.getTitle())))
                .doOnSuccess(res -> bodyLogger.logIfNotSuccessful(res, log));
    }

    public Mono<Response<Message>> notifyUserStatusKicked(String botContext, User user, Chat chat) {
        return botManager.getBot(BotRole.LOG)
                .flatMap(bot ->
                        bot.sendMessage(
                                applicationProperties.getLogChat(),
                                USER_KICKED_MESSAGE_FORMAT
                                        .formatted(botContext,
                                                user.getFirstName(), user.getLastName(),
                                                user.getId(), user.getUserName(),
                                                user.getLanguageCode(),
                                                chat.getType(),
                                                chat.getTitle())))
                .doOnSuccess(res -> bodyLogger.logIfNotSuccessful(res, log));
    }
}
