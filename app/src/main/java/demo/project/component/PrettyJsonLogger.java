package demo.project.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.motokyi.tg.bot_api.api.type.Response;
import com.motokyi.tg.bot_api.api.type.message.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrettyJsonLogger {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

    public void info(Object obj, Logger log) {
        try {
            final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            log.info(objectWriter.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            log.warn("Pretty JSON printer failed", e);
        }
    }

    public void warn(String message, Object obj, Logger log) {
        try {
            final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            log.warn("{}\n{}", message, objectWriter.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            log.warn("Pretty JSON printer failed", e);
        }
    }

    public void warn(Object obj, Logger log) {
        try {
            final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            log.warn(objectWriter.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            log.warn("Pretty JSON printer failed", e);
        }
    }

    public void debug(Object obj, Logger log) {
        try {
            final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            log.debug(objectWriter.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            log.warn("Pretty JSON printer failed", e);
        }
    }


    public void logIfNotSuccessful(Response<Message> messageResponse, Logger log) {
        if (!messageResponse.isOk()) {
            warn(messageResponse, log);
        }
    }
}
