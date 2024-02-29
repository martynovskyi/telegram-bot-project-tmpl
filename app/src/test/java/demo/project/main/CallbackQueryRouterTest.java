package demo.project.main;

import demo.project.TestObjects;
import demo.project.bot.BotContext;
import com.motokyi.tg.bot_api.api.type.markup.CallbackQuery;
import com.motokyi.tg.bot_api.bot.Bot;
import demo.project.bot.CallbackQueryHandler;
import demo.project.bot.main.CallbackQueryRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CallbackQueryRouterTest {

    @Mock
    Bot bot;
    @Mock
    CallbackQueryHandler testHandler;

    Map<String, CallbackQueryHandler> handlers;
    CallbackQueryRouter router;

    @BeforeEach
    void setUp() {
        handlers = new HashMap<>();
        router = new CallbackQueryRouter(handlers);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"abc#123"})
    void route_unknownCommand(String query) {
        CallbackQuery callbackQuery = TestObjects.callbackQuery(query);
        StepVerifier.create(router.route(TestObjects.context(bot), callbackQuery))
                .verifyComplete();
    }

    @Test
    void route_success() {
        CallbackQuery callbackQuery = TestObjects.callbackQuery("ctg#1024");
        BotContext context = TestObjects.context(bot);
        handlers.put("ctg", testHandler);
        when(testHandler.handle(context, callbackQuery)).thenReturn(Mono.empty());

        StepVerifier.create(router.route(context, callbackQuery))
                .verifyComplete();

        verify(testHandler).handle(context, callbackQuery);
    }
}