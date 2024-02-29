package demo.project.persistence.account.repository;

import demo.project.persistence.Entities;
import demo.project.persistence.PostgresTestContainer;
import demo.project.persistence.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;


@DataR2dbcTest
@ExtendWith(PostgresTestContainer.class)
class AccountRepositoryTest {
    @Autowired
    AccountRepository accountRepository;

    @Test
    void persistAndExist() {
        accountRepository.save(Entities.account())
                .flatMap(acc -> accountRepository.existsById(acc.getId()))
                .as(StepVerifier::create)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }
}