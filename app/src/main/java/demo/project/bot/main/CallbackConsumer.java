package demo.project.bot.main;

import demo.project.bot.BotContext;
import com.motokyi.tg.bot_api.api.type.markup.CallbackQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallbackConsumer {
    private final BotContext mainBotContext;
    private final CallbackQueryRouter callbackQueryRouter;

    public Mono<Void> consume(CallbackQuery callbackQuery) {
        log.info("Incoming callback (id)={} (data)={}", callbackQuery.getId(), callbackQuery.getData());

        if (StringUtils.isBlank(callbackQuery.getData())) {
            log.warn("Empty callback (id)={}", callbackQuery.getId());
            return Mono.empty();
        }
        return callbackQueryRouter.route(mainBotContext, callbackQuery);
    }
}
