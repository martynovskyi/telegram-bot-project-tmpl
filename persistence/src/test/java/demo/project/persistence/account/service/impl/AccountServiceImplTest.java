package demo.project.persistence.account.service.impl;

import demo.project.persistence.Entities;
import demo.project.persistence.account.constant.TelegramChatStatus;
import demo.project.persistence.account.entity.Telegram;
import demo.project.persistence.account.repository.AccountRepository;
import demo.project.persistence.account.repository.TelegramRepository;
import demo.project.persistence.account.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    TelegramRepository telegramRepository;

    @Captor
    ArgumentCaptor<Telegram> tgAccCaptor;

    @InjectMocks
    AccountServiceImpl accountService;

    @Test
    void getOrCreate_successWhenRecordExist() {
        Long platformUserId = 2000L;
        Telegram telegramSaved = Entities.telegramSaved(platformUserId);
        when(telegramRepository.findByUserId(platformUserId))
                .thenReturn(Mono.just(telegramSaved));

        var acc = accountService.getOrCreate(platformUserId);

        StepVerifier.create(acc)
                .expectNext(Tuples.of(Boolean.FALSE, telegramSaved))
                .verifyComplete();
        verifyNoInteractions(accountRepository);
    }

    @Test
    void getOrCreate_successWhenRecordNotExist() {
        Long platformUserId = 2000L;
        when(telegramRepository.findByUserId(platformUserId))
                .thenReturn(Mono.empty());
        when(accountRepository.save(any())).thenReturn(Mono.just(Entities.accountSaved()));
        Telegram telegramSaved = Entities.telegramSaved(platformUserId);
        when(telegramRepository.save(any())).thenReturn(Mono.just(telegramSaved));
        var acc = accountService.getOrCreate(platformUserId);

        StepVerifier.create(acc)
                .expectNext(Tuples.of(Boolean.TRUE, telegramSaved))
                .verifyComplete();
    }

    @Test
    void findAndSaveWithStatus_successWhenRecordExist() {
        Long platformUserId = 2000L;
        Telegram telegramSaved = Entities.telegramSaved(platformUserId);
        when(telegramRepository.findByUserId(platformUserId))
                .thenReturn(Mono.just(telegramSaved));
        when(telegramRepository.save(tgAccCaptor.capture())).thenReturn(Mono.just(telegramSaved));
        TelegramChatStatus status = TelegramChatStatus.KICKED;

        var acc = accountService.findAndSaveWithStatus(platformUserId, status);

        StepVerifier.create(acc)
                .expectNextCount(1)
                .verifyComplete();
        assertEquals(status.getValue(), tgAccCaptor.getValue().getDirectChatStatus());
        verifyNoInteractions(accountRepository);
    }

    @Test
    void findAndSaveWithStatus_successWhenRecordNotExist() {
        Long platformUserId = 2000L;
        when(telegramRepository.findByUserId(platformUserId))
                .thenReturn(Mono.empty());
        Telegram telegramSaved = Entities.telegramSaved(platformUserId);
        when(accountRepository.save(any())).thenReturn(Mono.just(Entities.accountSaved()));
        when(telegramRepository.save(tgAccCaptor.capture())).thenReturn(Mono.just(telegramSaved));
        TelegramChatStatus status = TelegramChatStatus.ACTIVE;

        var acc = accountService.findAndSaveWithStatus(platformUserId, status);

        StepVerifier.create(acc)
                .expectNextCount(1)
                .verifyComplete();
        assertEquals(status.getValue(), tgAccCaptor.getValue().getDirectChatStatus());
    }
}