package demo.project.persistence.account.service;


import demo.project.persistence.account.constant.TelegramChatStatus;
import demo.project.persistence.account.entity.Telegram;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface AccountService {

    Mono<Tuple2<Boolean, Telegram>> getOrCreate(Long platformUserId);

    Mono<Telegram> findAndSaveWithStatus(Long platformUserId, TelegramChatStatus status);

}
