package demo.project.bot.main.command;

import demo.project.bot.BotContext;
import demo.project.bot.CommandHandler;
import demo.project.component.TgChannelLogger;
import demo.project.constant.Command;
import demo.project.constant.Texts;
import demo.project.persistence.account.entity.Telegram;
import demo.project.persistence.account.service.AccountService;
import com.motokyi.tg.bot_api.api.type.Response;
import com.motokyi.tg.bot_api.api.type.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Component(Command.Value.START)
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {

    private final AccountService accountService;
    private final TgChannelLogger channelLogger;

    private static void onSuccessStartMessage(Response<Message> resp) {
        log.info("{} - success (resp.isOk)={}", Command.START, resp.isOk());
    }

    private static void onSuccessFlowCompleted(Response<Message> resp) {
        log.info("{} - completed", Command.START);
    }

    private static void onSuccessAccountCreation(Tuple2<Boolean, Telegram> accWithStatus) {
        var acc = accWithStatus.getT2();
        log.info("{} - with account (new record)={}, (telegram.id)={} (telegram.platformUserId)={}",
                Command.START,
                accWithStatus.getT1(),
                acc.getId(),
                acc.getUserId());
    }

    @Override
    public Mono<Void> handle(BotContext context, Message message) {
        return context.bot().sendMessage(message.getChat(), Texts.START_COMMAND_RESPONSE)
                .doOnSuccess(StartCommandHandler::onSuccessStartMessage)
                .then(accountService.getOrCreate(message.getFrom().getId()))
                .doOnSuccess(StartCommandHandler::onSuccessAccountCreation)
                .filter(Tuple2::getT1)
                .flatMap(acc -> channelLogger.notifyNewUser(context.name(), message))
                .doOnSuccess(StartCommandHandler::onSuccessFlowCompleted)
                .then();
    }
}
