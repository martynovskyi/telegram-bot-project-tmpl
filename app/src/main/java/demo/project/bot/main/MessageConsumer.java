package demo.project.bot.main;

import demo.project.bot.BotContext;
import demo.project.service.CommonResponseService;
import com.motokyi.tg.bot_api.api.type.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final BotContext mainBotContext;
    private final CommandsRouter commandsRouter;
    private final CommonResponseService commonResponseService;

    public Mono<Void> consume(Message message) {
        log.info("New message: {}", message.getText());
        if (StringUtils.isBlank(message.getText())) {
            return Mono.empty();
        }
        if (isCommand(message)) {
            return commandsRouter.route(mainBotContext, message);
        }
        return commonResponseService.noWayToHandle(mainBotContext.bot(), message)
                .doOnSuccess(resp -> log.info("chat:{}, ok:{}, text:{}",
                        resp.getResult().getChat().getId(), resp.isOk(), resp.getResult().getText()))
                .then();
    }

    private boolean isCommand(Message message) {
        return CollectionUtils.isNotEmpty(message.getEntities())
               && "bot_command".equals(message.getEntities().get(0).getType());
    }
}
