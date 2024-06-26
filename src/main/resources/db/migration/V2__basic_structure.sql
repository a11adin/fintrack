CREATE TABLE account
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255),
    balance FLOAT,
    user_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

ALTER TABLE account
    ADD CONSTRAINT FK_ACCOUNT_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;


CREATE TABLE category
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

ALTER TABLE category
    ADD CONSTRAINT uc_category_name UNIQUE (name);


CREATE TABLE transaction
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    account_id  BIGINT                                  NOT NULL,
    amount      FLOAT                                   NOT NULL,
    timestamp   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    category_id BIGINT,
    description VARCHAR(1023),
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (id);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);