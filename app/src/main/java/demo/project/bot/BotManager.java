package demo.project.bot;

import demo.project.config.properties.ApplicationProperties;
import demo.project.config.properties.BotRole;
import com.motokyi.tg.bot_api.bot.Bot;
import com.motokyi.tg.bot_api.bot.BotFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotManager {
    private final BotFactory botFactory;
    private final ApplicationProperties properties;

    @PostConstruct
    public void postConstruct() {
        log.info("Bots instantiated {}", botFactory.botNames());
    }

    public Mono<Bot> getBot(BotRole role) {
        final String botNameForRole = properties.getBotNameForRole(role);
        return Objects.nonNull(botNameForRole)
                ? Mono.justOrEmpty(botFactory.getBot(botNameForRole))
                : Mono.empty();
    }

    public Mono<BotContext> getBotContext(BotRole role) {
        final String botNameForRole = properties.getBotNameForRole(role);
        return Objects.nonNull(botNameForRole)
                ? Mono.justOrEmpty(botFactory.getBot(botNameForRole).map(bot -> new BotContext(botNameForRole, bot)))
                : Mono.empty();
    }
}
