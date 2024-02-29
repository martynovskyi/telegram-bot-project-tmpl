package demo.project.bot.main;

import demo.project.bot.BotContext;
import demo.project.bot.CallbackQueryHandler;
import demo.project.bot.main.callback.CallbackBuilder;
import com.motokyi.tg.bot_api.api.type.markup.CallbackQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallbackQueryRouter {
    private final Map<String, CallbackQueryHandler> handlers;

    private final CallbackQueryHandler cbNotSupportedHandler = (context, callbackQuery) -> {
        log.warn("Unsupported callback (id)={} (data)={}", callbackQuery.getId(), callbackQuery.getData());
        return Mono.empty();
    };

    public Mono<Void> route(BotContext context, CallbackQuery callbackQuery) {
        return CallbackBuilder.extractPrefix(callbackQuery.getData())
                .map(type -> handlers.getOrDefault(type, cbNotSupportedHandler))
                .map(handler -> handler.handle(context, callbackQuery))
                .orElse(cbNotSupportedHandler.handle(context, callbackQuery));
    }
}
