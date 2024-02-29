package demo.project.service;

import com.motokyi.tg.bot_api.api.type.Response;
import com.motokyi.tg.bot_api.api.type.message.Message;
import com.motokyi.tg.bot_api.bot.Bot;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface CommonResponseService {

    Mono<Response<Message>> noWayToHandle(@NotNull Bot bot, @NotNull Message message);
}
