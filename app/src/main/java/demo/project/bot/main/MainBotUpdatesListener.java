package demo.project.bot.main;


import demo.project.bot.BotContext;
import demo.project.component.PrettyJsonLogger;
import demo.project.component.TgChannelLogger;
import demo.project.config.properties.ApplicationProperties;
import com.motokyi.tg.bot_api.api.type.Response;
import com.motokyi.tg.bot_api.api.type.update.WebhookInfo;
import com.motokyi.tg.bot_api.bot.Bot;
import com.motokyi.tg.bot_api.tools.UpdateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MainBotUpdatesListener implements ApplicationListener<ApplicationReadyEvent> {
    private final ApplicationProperties applicationProperties;
    private final UpdateHandler updateHandler;
    private final PrettyJsonLogger bodyLogger;
    private final TgChannelLogger channelLogger;
    private final BotContext mainBotContext;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Initialization MAIN bot updates handling");
        if (applicationProperties.isWebhookConfigured()) {
            webhookHandler();
        } else {
            longPollingHandler();
        }
    }

    private void webhookHandler() {
        log.info("Configuring webhook for '{}'...", mainBotContext.name());
        mainBotContext.bot()
                .getWebhookInfo()
                .subscribe(response -> {
                    if (response.isOk()) {
                        registerWebhook(mainBotContext.bot(), response.getResult());
                    }
                });
    }

    private void registerWebhook(Bot bot, WebhookInfo existingWebhookInfo) {
        if (applicationProperties.getWebhookUrl().equals(existingWebhookInfo.getUrl())) {
            log.info("Already configured on this host {}", applicationProperties.getWebhookUrl());
            return;
        }
        var previousStep = Mono.just(mockOkResponse());

        if (StringUtils.isNotBlank(existingWebhookInfo.getUrl())) {
            log.warn("Previous configuration set up is referencing on different host");
            previousStep = bot.deleteWebhook().send();
        }
        previousStep.subscribe(resp -> {
            if (resp.isOk() && resp.getResult()) {
                bot.setWebhook(applicationProperties.getWebhookUrl())
                        .secretToken(applicationProperties.getWebhookToken())
                        .subscribe(r -> log.info("Webhook registration result {} - {}", r.isOk(), r.getResult()));
            }
        });
    }

    private void longPollingHandler() {
        log.info("Configuring long polling for {}...", mainBotContext.name());
        mainBotContext.bot()
                .getUpdates()
                .updateStream()
                .doOnNext(update -> { log.info("New Update: (update.updateId)={}", update.getUpdateId());
                    bodyLogger.debug(update,log);})
                .subscribe(update -> {
                            try {
                                updateHandler.apply(update);
//                                log.info("Update handling completed successfully: (update.updateId)={}", update.getUpdateId());
                            } catch (Exception e) {
                                log.error("Update Handler Exception", e);
                                channelLogger.notifyException(mainBotContext.name(), "On updates handling", e)
                                        .subscribe();
                            }
                        },
                        throwable -> {
                            log.error("Broken updates stream", throwable);
                            channelLogger.notifyException(mainBotContext.name(), "Updates subscription broken", throwable)
                                    .subscribe();
                        });
    }

    private Response<Boolean> mockOkResponse() {
        Response<Boolean> booleanResponse = new Response<>();
        booleanResponse.setOk(true);
        booleanResponse.setResult(true);
        return booleanResponse;
    }
}