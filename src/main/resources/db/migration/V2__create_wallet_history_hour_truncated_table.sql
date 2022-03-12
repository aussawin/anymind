create table hourly_transaction_history
(
    transaction_datetime timestamptz not null
        constraint table_name_pk
            primary key,
    amount               decimal(7, 2)
);

create unique index table_name_transaction_datetime_uindex
    on hourly_transaction_history (transaction_datetime);

