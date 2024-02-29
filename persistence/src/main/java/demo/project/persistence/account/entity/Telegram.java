package demo.project.persistence.account.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity represents telegram account in the system
 */
@Table(schema = "account", name = "telegram")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Telegram {
    @Id
    private Long id;

    @Column("account_id")
    private Long accountId;

    @Column("user_id")
    private Long userId;

    @Column("direct_chat_status")
    private String directChatStatus;

    @Column("language_code")
    private String languageCode;

    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ReadOnlyProperty
    @Column("created_at")
    private LocalDateTime createdAt;
}
