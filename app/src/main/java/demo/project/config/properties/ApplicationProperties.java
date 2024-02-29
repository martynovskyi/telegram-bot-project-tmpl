package demo.project.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(value = "app-config")
public class ApplicationProperties {
    private Map<BotRole, String> roles;
    private long logChat;
    private String webhookUrl;
    private String webhookToken;


    public boolean isWebhookConfigured() {
        return StringUtils.isNotBlank(webhookUrl);
    }

    public String getBotNameForRole(BotRole role) {
        return roles.get(role);
    }
}
