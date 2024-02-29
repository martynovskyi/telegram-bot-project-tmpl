package demo.project.persistence.account.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity represents general account in the system
 */

@Table(schema = "account", name = "account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Account {
    @Id
    private Long id;

    @ReadOnlyProperty
    @Column("created_at")
    private LocalDateTime createdAt;
}
