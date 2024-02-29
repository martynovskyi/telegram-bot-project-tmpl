package demo.project.main;

import demo.project.TestObjects;
import demo.project.bot.BotContext;
import demo.project.constant.Command;
import demo.project.constant.Texts;
import com.motokyi.tg.bot_api.api.type.chat.Chat;
import com.motokyi.tg.bot_api.api.type.message.Message;
import com.motokyi.tg.bot_api.bot.Bot;
import demo.project.bot.CommandHandler;
import demo.project.bot.main.CommandsRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandsRouterTest {
    @Mock
    Bot bot;

    @Mock
    CommandHandler testHandler;

    Map<String, CommandHandler> handlers;
    CommandsRouter router;

    @BeforeEach
    void setUp() {
        handlers = new HashMap<>();
        router = new CommandsRouter(handlers);
    }

    @Test
    void route_unknownCommand() {
        Message message = TestObjects.withText("/unsupported");
        when(bot.sendMessage(any(Chat.class), eq(Texts.COMMAND_NOT_SUPPORTED)))
                .thenReturn(Mono.just(TestObjects.success()));

        router.route(TestObjects.context(bot), message);

        verify(bot).sendMessage(message.getChat(), Texts.COMMAND_NOT_SUPPORTED);

    }

    @Test
    void route_success() {
        Message message = TestObjects.withText(Command.HELP.getCommand());
        BotContext context = TestObjects.context(bot);
        handlers.put(Command.Value.HELP, testHandler);
        when(testHandler.handle(context, message)).thenReturn(Mono.empty());

        router.route(context, message);

        verify(testHandler).handle(context, message);
    }
}