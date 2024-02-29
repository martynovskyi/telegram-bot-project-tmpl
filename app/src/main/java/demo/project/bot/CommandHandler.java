package demo.project.bot;

import com.motokyi.tg.bot_api.api.type.message.Message;
import reactor.core.publisher.Mono;

public interface CommandHandler {

    Mono<Void> handle(BotContext context, Message message);

}
