package demo.project.main;

import demo.project.TestObjects;
import demo.project.constant.Command;
import demo.project.service.CommonResponseService;
import com.motokyi.tg.bot_api.api.type.Response;
import com.motokyi.tg.bot_api.api.type.chat.Chat;
import com.motokyi.tg.bot_api.api.type.message.Message;
import com.motokyi.tg.bot_api.api.type.message.MessageEntity;
import com.motokyi.tg.bot_api.bot.Bot;
import demo.project.bot.main.CommandsRouter;
import demo.project.bot.main.MessageConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    @Mock
    Bot bot;
    @Mock
    CommandsRouter commandsRouter;
    @Mock
    CommonResponseService commonResponseService;
    MessageConsumer messageConsumer;

    private static Message createMessage(String entityType) {
        Message message = new Message();
        message.setText(Command.START.getCommand());
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setType(entityType);
        message.setEntities(List.of(messageEntity));
        return message;
    }

    @BeforeEach
    void setUp() {
        messageConsumer = new MessageConsumer(TestObjects.context(bot), commandsRouter, commonResponseService);
    }

    @Test
    void consume_withCommand() {
        Message message = createMessage("bot_command");
        when(commandsRouter.route(TestObjects.context(bot), message)).thenReturn(Mono.empty());
        Mono<Void> consume = messageConsumer.consume(message);
        StepVerifier.create(consume)
                .expectNextCount(0)
                .verifyComplete();

        consume.subscribe();

        verify(commandsRouter).route(TestObjects.context(bot), message);
        verifyNoInteractions(commonResponseService);
    }

    @Test
    void consume_noCommand() {
        Message message = createMessage("hashtag");
        Response<Message> response = new Response<>();
        Message result = new Message();
        result.setChat(new Chat());
        response.setResult(result);

        when(commonResponseService.noWayToHandle(bot, message)).thenReturn(Mono.just(response));
        Mono<Void> consume = messageConsumer.consume(message);
        StepVerifier.create(consume)
                .expectNextCount(0)
                .verifyComplete();

        consume.subscribe();

        verify(commonResponseService).noWayToHandle(bot, message);
        verifyNoInteractions(commandsRouter);
    }

    @Test
    void consume_messageWithNoText() {
        Message message = new Message();

        Mono<Void> consume = messageConsumer.consume(message);
        StepVerifier.create(consume)
                .expectNextCount(0)
                .verifyComplete();

        consume.subscribe();
        verifyNoInteractions(commandsRouter, commonResponseService);
    }
}