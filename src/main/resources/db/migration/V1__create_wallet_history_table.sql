create table wallet_history
(
    id                   varchar(36)    not null,
    amount               decimal(7, 2)  not null,
    transaction_datetime timestamptz    not null,
    constraint wallet_history_pk
        primary key (id)
);

create unique index wallet_history_id_uindex
    on wallet_history (id);

