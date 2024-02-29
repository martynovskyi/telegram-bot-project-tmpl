package demo.project.config;

import demo.project.bot.BotContext;
import demo.project.bot.BotManager;
import demo.project.bot.main.CallbackConsumer;
import demo.project.bot.main.MessageConsumer;
import demo.project.bot.main.event.ChatMemberEventsConsumer;
import demo.project.config.properties.BotRole;
import com.motokyi.tg.bot_api.bot.BotFactory;
import com.motokyi.tg.bot_api.bot.TelegramBotFactory;
import com.motokyi.tg.bot_api.config.properties.TelegramBotProperties;
import com.motokyi.tg.bot_api.tools.UpdateHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class BeanDefinitions {

    @Bean
    @ConfigurationProperties(prefix = "telegram")
    public TelegramBotProperties telegramBotProperties() {
        return new TelegramBotProperties();
    }

    @Bean
    public BotFactory telegramBotFactory(TelegramBotProperties botProperties) {
        return new TelegramBotFactory(botProperties, WebClient.builder());
    }

    @Bean
    public UpdateHandler updateHandler(MessageConsumer messageConsumer,
                                       ChatMemberEventsConsumer chatMemberEventsConsumer,
                                       CallbackConsumer callbackConsumer) {
        return UpdateHandler.builder()
                .messageConsumer(message -> messageConsumer.consume(message).subscribe())
                .myChatMemberConsumer(event -> chatMemberEventsConsumer.consume(event).subscribe())
                .callbackQueryConsumer(callbackQuery -> callbackConsumer.consume(callbackQuery).subscribe() )
                .build();
    }

    @Bean(BotRole.Value.MAIN)
    public BotContext mainBot(BotManager botManager) {
        return botManager.getBotContext(BotRole.MAIN)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("MAIN bot is not configured")))
                .block();
    }
}
