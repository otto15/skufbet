--liquibase formatted sql

--changeset dmitribazilov:change_personal_user_profile

alter table personal_user_profile
    rename password to passport
