INSERT INTO enum_role (id, name) VALUES
    (RANDOM_UUID(), 'ROLE_ADMIN'),
    (RANDOM_UUID(), 'ROLE_USER');
-- INSERT INTO enum_money_location (id, path, payment_transaction_id) VALUES

INSERT INTO enum_status_product (id, status) VALUES
    (RANDOM_UUID(),'AVAILABLE'), --товар есть в налиции и доступен к продаже
    (RANDOM_UUID(),'NOT AVAILABLE'), --товара нет в наличии
    (RANDOM_UUID(),'MARRIAGE'), --товар бракованный , ожидает возврата
    (RANDOM_UUID(),'RETURN'), --товар возвращен
    (RANDOM_UUID(),'SOLD'); -- товар продан

INSERT INTO "user"(id, login , first_name, last_name, salary, password, enable, account_non_locked) VALUES
    (RANDOM_UUID(), 'Slava', 'Slava', 'Safonau', 52.000,
     '$2a$12$HZdZHUQTbGNQl8864NhY1.IgemB68Gddytmi5Oe8Ru1h4nxtea6Lq', true, true);

SET @user_slava_id = (SELECT id FROM "user" WHERE first_name = 'Slava');
SET @role_admin_id = (SELECT id FROM enum_role WHERE name = 'ROLE_ADMIN');
SET @role_user_id = (SELECT id FROM enum_role WHERE name = 'ROLE_USER');

INSERT INTO user_role (user_id , role_id) VALUES
    (@user_slava_id, @role_admin_id),
    (@user_slava_id, @role_user_id);





-- -- Вставка типов платежей
-- INSERT INTO enum_type_payment (id, type) VALUES
--     (RANDOM_UUID(), 'CASH'),
--     (RANDOM_UUID(), 'CASHLESS'),
--     (RANDOM_UUID(), 'CREDIT');

-- -- Вставка статусов продуктов
-- INSERT INTO enum_status_product (id, status) VALUES
--     (RANDOM_UUID(), 'IN_STOCK'),
--     (RANDOM_UUID(), 'SOLD'),
--     (RANDOM_UUID(), 'RESERVED');
