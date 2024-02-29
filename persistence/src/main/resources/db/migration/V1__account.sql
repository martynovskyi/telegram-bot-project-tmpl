CREATE SCHEMA account;

CREATE SEQUENCE account.account_id AS BIGINT
    START 1
    INCREMENT 1;

CREATE TABLE account.account
(
    id         BIGINT             DEFAULT nextval('account.account_id') PRIMARY KEY,
    created_at timestamp NOT NULL DEFAULT now()
);
---
CREATE SEQUENCE account.telegram_id AS BIGINT
    START 1
    INCREMENT 1;

CREATE TABLE account.telegram
(
    id                 BIGINT                 DEFAULT nextval('account.telegram_id') PRIMARY KEY,
    account_id         BIGINT        NOT NULL,
    user_id            BIGINT UNIQUE NOT NULL,
    language_code      varchar(4),
    direct_chat_status varchar(8)             DEFAULT NULL,
    updated_at         timestamp              DEFAULT NULL,
    created_at         timestamp     NOT NULL DEFAULT now()
);
ALTER TABLE account.telegram
    ADD CONSTRAINT account_fk FOREIGN KEY (account_id) REFERENCES account.account (id);
