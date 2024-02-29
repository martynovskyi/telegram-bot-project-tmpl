package demo.project.persistence.account.service.impl;

import demo.project.persistence.account.constant.TelegramChatStatus;
import demo.project.persistence.account.entity.Account;
import demo.project.persistence.account.entity.Telegram;
import demo.project.persistence.account.repository.AccountRepository;
import demo.project.persistence.account.repository.TelegramRepository;
import demo.project.persistence.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TelegramRepository telegramRepository;

    @Override
    @Transactional
    public Mono<Tuple2<Boolean, Telegram>> getOrCreate(Long platformUserId) {
        return telegramRepository.findByUserId(platformUserId)
                .map(acc -> Tuples.of(Boolean.FALSE, acc))
                .switchIfEmpty(
                        Mono.defer(() -> registerAccount(platformUserId, TelegramChatStatus.ACTIVE))
                                .map(newAcc -> Tuples.of(Boolean.TRUE, newAcc)));
    }

    @Override
    @Transactional
    public Mono<Telegram> findAndSaveWithStatus(Long platformUserId, TelegramChatStatus status) {
        return telegramRepository.findByUserId(platformUserId)
                .flatMap(tg -> {
                    tg.setDirectChatStatus(status.getValue());
                    return telegramRepository.save(tg);
                })
                .switchIfEmpty(
                        Mono.defer(() -> registerAccount(platformUserId, status)));
    }

    private Mono<Telegram> registerAccount(Long platformUserId, TelegramChatStatus status) {
        return accountRepository.save(new Account())
                .map(account -> {
                    Telegram telegram = new Telegram();
                    telegram.setAccountId(account.getId());
                    telegram.setUserId(platformUserId);
                    telegram.setDirectChatStatus(status.getValue());
                    return telegram;
                })
                .flatMap(telegramRepository::save);
    }
}
