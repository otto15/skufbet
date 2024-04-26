--liquibase formatted sql

--changeset dmitribazilov:baseline

CREATE TABLE if not exists user_profile (
    id INTEGER PRIMARY KEY,
    mail varchar(255),
    phone_number varchar(30),
    password varchar(255),
    balance INTEGER
)
