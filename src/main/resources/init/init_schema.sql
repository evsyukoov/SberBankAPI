create table IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment
        primary key,
    LOGIN     VARCHAR(255)
        constraint loginidx unique,
    NAME      VARCHAR(255),
    PASSWORD  VARCHAR(255),
    USER_ROLE VARCHAR(255)
);

create table IF NOT EXISTS CONTRAGENTS
(
    CONTRAGENT_ID INTEGER auto_increment
        primary key,
    BANK          INTEGER,
    NAME          VARCHAR(255),
    VERSION       INTEGER
);

create table IF NOT EXISTS ACCOUNTS
(
    ACCOUNT_ID     INTEGER auto_increment
        primary key,
    ACCOUNT_NUMBER VARCHAR(255),
    ACCOUNT_TYPE   INTEGER,
    CONTRAGENT_ID  INTEGER,
    foreign key (CONTRAGENT_ID) references CONTRAGENTS (CONTRAGENT_ID),
    VERSION INTEGER
);

create index IF NOT EXISTS IDX_ACCOUNT_NUMBER
    on ACCOUNTS (ACCOUNT_NUMBER);

create table IF NOT EXISTS CARDS
(
    CARD_ID     INTEGER auto_increment
        primary key,
    BALANCE     DECIMAL(19, 2),
    CARD_NUMBER VARCHAR(255)
        constraint cardNumberIdx
            unique,
    TYPE        INTEGER,
    EXPIREDATE  TIMESTAMP,
    ACCOUNT_ID  INTEGER,
    foreign key (ACCOUNT_ID) references ACCOUNTS (ACCOUNT_ID),
    VERSION INTEGER
)