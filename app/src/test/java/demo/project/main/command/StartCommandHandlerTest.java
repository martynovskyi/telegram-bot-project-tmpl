package demo.project.main.command;

import demo.project.TestObjects;
import demo.project.bot.BotContext;
import demo.project.component.TgChannelLogger;
import demo.project.constant.Command;
import demo.project.constant.Texts;
import demo.project.bot.main.command.StartCommandHandler;
import demo.project.persistence.account.entity.Telegram;
import demo.project.persistence.account.service.AccountService;
import com.motokyi.tg.bot_api.api.type.Response;
import com.motokyi.tg.bot_api.api.type.chat.Chat;
import com.motokyi.tg.bot_api.api.type.message.Message;
import com.motokyi.tg.bot_api.bot.Bot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartCommandHandlerTest {

    @Mock
    Bot bot;
    @Mock
    TgChannelLogger tgChannelLogger;
    @Mock
    AccountService accountService;

    @InjectMocks
    StartCommandHandler startCommandHandler;


    @Test
    void handle_successNewUser() {
        BotContext context = TestObjects.context(bot);
        Message message = TestObjects.command(Command.START);

        when(bot.sendMessage(any(Chat.class), eq(Texts.START_COMMAND_RESPONSE)))
                .thenReturn(Mono.just(new Response<>()));

        when(accountService.getOrCreate(any())).thenReturn(Mono.just(Tuples.of(Boolean.TRUE, new Telegram())));
        when(tgChannelLogger.notifyNewUser(context.name(), message)).thenReturn(Mono.empty());

        startCommandHandler.handle(context, message).block();

        verify(bot).sendMessage(message.getChat(), Texts.START_COMMAND_RESPONSE);
        verify(accountService).getOrCreate(any());
        verify(tgChannelLogger).notifyNewUser(context.name(), message);
    }

    @Test
    void handle_successUserReactivation() {
        BotContext context = TestObjects.context(bot);
        Message message = TestObjects.command(Command.START);

        when(bot.sendMessage(any(Chat.class), eq(Texts.START_COMMAND_RESPONSE)))
                .thenReturn(Mono.just(new Response<>()));

        when(accountService.getOrCreate(any())).thenReturn(Mono.just(Tuples.of(Boolean.FALSE, new Telegram())));

        startCommandHandler.handle(context, message).block();

        verify(bot).sendMessage(message.getChat(), Texts.START_COMMAND_RESPONSE);
        verify(accountService).getOrCreate(any());
        verifyNoInteractions(tgChannelLogger);
    }
}