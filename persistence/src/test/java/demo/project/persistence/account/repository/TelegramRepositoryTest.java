package demo.project.persistence.account.repository;

import demo.project.persistence.Entities;
import demo.project.persistence.PostgresTestContainer;
import demo.project.persistence.account.repository.AccountRepository;
import demo.project.persistence.account.repository.TelegramRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;


@DataR2dbcTest
@ExtendWith(PostgresTestContainer.class)
class TelegramRepositoryTest {

    @Autowired
    TelegramRepository telegramRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void persistAndExist() {
        Long platformUserId = 888L;
        accountRepository.save(Entities.account())
                .flatMap(acc -> telegramRepository.save(Entities.telegram(acc, platformUserId)))
                .flatMap(telegramAcc -> telegramRepository.existsById(telegramAcc.getId()))
                .as(StepVerifier::create)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void findByUserId() {
        StepVerifier.create(telegramRepository.findByUserId(1000L))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByUserId_noData() {
        StepVerifier.create(telegramRepository.findByUserId(404L))
                .expectNextCount(0)
                .verifyComplete();
    }
}