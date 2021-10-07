INSERT INTO USERS ("LOGIN", "NAME", "PASSWORD", "USER_ROLE")
VALUES ('admin', 'Админ Банковский Служащий', '$2a$12$dEkN2.LTY1DbpLoMZgk3cuPIGWpxMZF/fF.f15ZJ4ifl0LlYV57hm', 'ROLE_ADMIN'),
       ('user', 'Тестовый Тест Девелоперович', '$2a$12$oEd0Ez8IdRjvvtyippus0uqCvUzdiXhrTwlsJWDq3bAN7wId1j9gG', 'ROLE_USER');

INSERT INTO CONTRAGENTS ("BANK", "NAME", "VERSION")
VALUES (0, 'Клиент Сбербанка Первый', 0),
       (0, 'Клиент Сбербанка Второй', 0),
       (1, 'Клиент ДругогоБанка Первый', 0),
       (2, 'Клиент ДругогоБанка Второй', 0);

INSERT INTO ACCOUNTS ("ACCOUNT_NUMBER", "ACCOUNT_TYPE", "CONTRAGENT_ID", "VERSION")
VALUES ('11112222333344445555', 0, 1, 0),
       ('11112222333344445556', 1, 2, 0),
       ('11112222333344445557', 2, 2, 0),
       ('11112222333344446666', 1, 1, 0),
       ('11112222333344445558', 3, 3, 0),
       ('11112222333344445559', 3, 3, 0),
       ('12342222333344445559', 1, 4, 0);

INSERT INTO CARDS("BALANCE", "CARD_NUMBER", "TYPE", "EXPIREDATE", "ACCOUNT_ID", "VERSION")
VALUES (0,'4444111122223333', 0, '2021-01-01', 1, 0),
       (0,'4444111122223336', 1, '2022-01-01', 1, 0),
       (20,'4444111122223337', 1, '2023-01-01', 2, 0),
       (0,'4444111122223338', 2, '2023-01-01', 3, 0),
       (1.15,'4444111122223339', 2, '2023-01-01', 4, 0),
       (0,'4444111122224444', 2, '2023-01-01', 6, 0),
       (0,'4444111122224445', 2, '2021-01-01', 5, 0),
       (100000,'6444411122224445', 2, '2024-01-01', 6, 0),
       (1.10,'7444411122224445', 1, '2025-01-01', 7, 0)


