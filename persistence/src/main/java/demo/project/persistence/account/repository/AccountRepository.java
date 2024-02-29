package demo.project.persistence.account.repository;

import demo.project.persistence.account.entity.Account;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends R2dbcRepository<Account, Long> {
}
