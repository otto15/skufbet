--liquibase formatted sql

--changeset otto15:baseline

create table if not exists user_profile (
                                            id serial primary key,
                                            mail varchar(255),
    phone_number varchar(30),
    password varchar(255),
    balance int
    );

create table if not exists personal_user_profile (
                                                     id serial primary key,
                                                     user_id int references user_profile(id) not null,
    first_name varchar(30),
    last_name varchar(60),
    password varchar(255),
    date_of_birth date,
    taxpayer_id varchar(255)
    );

create table if not exists tournament (
                                          id serial primary key,
                                          tournament_name varchar(255),
    is_end bool
    );

create table if not exists sport (
                                     id serial primary key,
                                     sport_name varchar(50)
    );

create table if not exists event (
                                     id serial primary key,
                                     tournament_id int references tournament(id),
    sport_id int references sport(id),
    event_name varchar(100),
    is_end bool
    );

create table if not exists line_type (
                                         id serial primary key,
                                         type_name varchar(50)
    );

create table if not exists line_result (
                                           id serial primary key,
                                           line_id int not null,
                                           result varchar(20),
    coefficient float
    );

create table if not exists line (
                                    id serial primary key,
                                    event_id int references event(id) not null,
    line_type_id int references line_type(id) not null,
    result_id int,
    is_closed bool
    );

alter table line
    add constraint FK_result_id
        foreign key (result_id) references line_result(id);

alter table line_result
    add constraint FK_line_id
        foreign key (line_id) references line(id);

create table if not exists bet(
                                  id serial primary key,
                                  user_id int references user_profile(id) not null,
    line_id int references line(id) not null,
    result_id int references line_result(id) not null,
    amount int not null,
    coefficient float
    );
