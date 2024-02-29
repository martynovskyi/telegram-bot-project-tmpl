package demo.project.bot.main;

import demo.project.bot.BotContext;
import demo.project.bot.CommandHandler;
import demo.project.constant.Command;
import demo.project.constant.Texts;
import com.motokyi.tg.bot_api.api.type.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandsRouter {
    private final Map<String, CommandHandler> handlers;

    private final CommandHandler commandNotSupportedHandler = (context, message) ->
            context.bot().sendMessage(message.getChat(), Texts.COMMAND_NOT_SUPPORTED)
                    .doOnSuccess(resp ->
                            log.info("Unsupported command {} answer: (user.id)={} (response.ok)={}",
                                    message.getText(),
                                    message.getFrom().getId(),
                                    resp.isOk()))
                    .then();

    public Mono<Void> route(BotContext context, Message message) {
        log.info("Incoming command: (user.id)={} (message.text)={}", message.getFrom().getId(), message.getText());
        return Command.of(message.getText())
                .map(cmnd -> handlers.getOrDefault(cmnd.getCommand().substring(1), commandNotSupportedHandler))
                .map(handler -> handler.handle(context, message))
                .orElseGet(() -> commandNotSupportedHandler.handle(context, message));
    }
}
