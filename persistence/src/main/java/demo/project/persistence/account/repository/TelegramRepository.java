package demo.project.persistence.account.repository;


import demo.project.persistence.account.entity.Telegram;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TelegramRepository extends R2dbcRepository<Telegram, Long> {
    Mono<Telegram> findByUserId(Long id);
}
