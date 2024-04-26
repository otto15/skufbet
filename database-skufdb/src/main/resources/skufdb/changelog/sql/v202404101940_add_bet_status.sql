--liquibase formatted sql

--changeset dmitribazilov:add_bet_status

ALTER TABLE bet
    ADD COLUMN status varchar(40)
    check (
        status = 'VALIDATING'
        or status = 'FAILED_BY_BALANCE'
        or status = 'FAILED_BY_COEFFICIENT'
        or status = 'ACCEPTED'
        or status = 'CALCULATED'
    );
