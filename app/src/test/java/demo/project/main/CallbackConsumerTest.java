package demo.project.main;

import demo.project.TestObjects;
import demo.project.bot.BotContext;
import com.motokyi.tg.bot_api.api.type.markup.CallbackQuery;
import demo.project.bot.main.CallbackConsumer;
import demo.project.bot.main.CallbackQueryRouter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CallbackConsumerTest {

    @Mock
    BotContext mainBotContext;
    @Mock
    CallbackQueryRouter callbackQueryRouter;

    @InjectMocks
    CallbackConsumer callbackConsumer;


    @Test
    void consume_success() {
        CallbackQuery callbackQuery = TestObjects.callbackQuery("ctg#255");
        when(callbackQueryRouter.route(mainBotContext, callbackQuery)).thenReturn(Mono.empty());

        StepVerifier.create(callbackConsumer.consume(callbackQuery))
                .verifyComplete();

        verify(callbackQueryRouter).route(any(), any());
    }

    @Test
    void consume_noData() {
        CallbackQuery callbackQuery = TestObjects.callbackQuery("");

        StepVerifier.create(callbackConsumer.consume(callbackQuery))
                .verifyComplete();

        verifyNoInteractions(callbackQueryRouter);
    }
}