--liquibase formatted sql

--changeset otto15:baseline

alter table personal_user_profile rename to user_profile_details;

alter table personal_user_profile_id_seq rename to user_profile_details_id_seq;

alter table user_profile_details drop constraint personal_user_profile_pkey;

alter table user_profile_details rename column user_id to user_profile_id;

alter table user_profile_details add constraint user_profile_details_pk primary key (user_profile_id);
