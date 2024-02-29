package demo.project.service.impl;

import demo.project.constant.Texts;
import demo.project.service.CommonResponseService;
import com.motokyi.tg.bot_api.api.type.Response;
import com.motokyi.tg.bot_api.api.type.message.Message;
import com.motokyi.tg.bot_api.bot.Bot;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class CommonResponseImpl implements CommonResponseService {
    @Override
    public Mono<Response<Message>> noWayToHandle(@NotNull Bot bot, @NotNull Message message) {
        return bot.sendMessage(message.getChat())
                .text(Texts.I_DONT_KNOW_HOW_HANDLE)
                .send();
    }
}
