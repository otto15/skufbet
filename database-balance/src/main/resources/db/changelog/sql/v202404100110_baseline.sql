--liquibase formatted sql

--changeset otto15:baseline

CREATE TABLE balance_operation
(
    id              UUID PRIMARY KEY,
    payment_token   VARCHAR(255),
    user_profile_id INTEGER      NOT NULL,
    amount          INTEGER      NOT NULL,
    type            VARCHAR(20)  NOT NULL,
    status          VARCHAR(20)  NOT NULL
);
