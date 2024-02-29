package demo.project.bot;

import com.motokyi.tg.bot_api.api.type.markup.CallbackQuery;
import reactor.core.publisher.Mono;

public interface CallbackQueryHandler {
    Mono<Void> handle(BotContext context, CallbackQuery callbackQuery);
}
