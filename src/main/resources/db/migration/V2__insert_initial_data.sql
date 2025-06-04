-- Вставка ролей
INSERT INTO enum_role (id, name) VALUES
    (RANDOM_UUID(), 'ROLE_ADMIN'),
    (RANDOM_UUID(), 'ROLE_USER');

INSERT INTO "user"(id, login , first_name, last_name, salary, password, enable, account_non_locked) VALUES
    (RANDOM_UUID(), 'Safonov', 'Slava', 'Safonov', 52.000,
     '$2a$12$YacR7qru9ssBj5n3LvMtOefHwIBQsHEokv4zHZskaXqGX1eQHkBTy', true, true);

SET @user_slava_id = (SELECT id FROM "user" WHERE first_name = 'Slava');
SET @role_admin_id = (SELECT id FROM enum_role WHERE name = 'ROLE_ADMIN');

INSERT INTO user_role (user_id , role_id) VALUES
    (@user_slava_id, @role_admin_id);





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
