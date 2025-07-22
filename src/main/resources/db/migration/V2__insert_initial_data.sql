INSERT INTO enum_role (id, name) VALUES
    (RANDOM_UUID(), 'ROLE_ADMIN'),
    (RANDOM_UUID(), 'ROLE_USER');

INSERT INTO enum_type_payment (id, type) VALUES
    (RANDOM_UUID(), 'CASH'),
    (RANDOM_UUID(), 'NON_CASH'),
    (RANDOM_UUID(), 'CREDIT');

INSERT INTO enum_money_location (id, path) VALUES
    (RANDOM_UUID(), 'CASH_IN_BOX'), -- наличные пошли в коробку
    (RANDOM_UUID(), 'CASH_IN_REGISTER_LIFE'), -- наличные пошли в кассу от life
    (RANDOM_UUID(), 'CASH_IN_REGISTER_LAPTOP'), -- наличные пошли в кассу от laptop
    (RANDOM_UUID(), 'CASH_IN_REGISTER_LIFE_FROM_LAPTOP'), -- наличные пошли в кассу life от laptop
    (RANDOM_UUID(), 'NON_CASH_IN_REGISTER_LIFE'), -- безнал в кассу от life
    (RANDOM_UUID(), 'NON_CASH_IN_REGISTER_LAPTOP'), -- безнал в кассу от laptop
    (RANDOM_UUID(), 'NON_CASH_IN_REGISTER_LIFE_FROM_LAPTOP'), -- безнал в кассу life от laptop
    (RANDOM_UUID(), 'CREDIT'); -- кредит

INSERT INTO enum_status_product (id, status) VALUES
    (RANDOM_UUID(),'EXAMINATION'), --товар есть в налиции и находится на проверке
    (RANDOM_UUID(),'AVAILABLE'), --товар есть в налиции и доступен к продаже
    (RANDOM_UUID(),'NOT AVAILABLE'), --товара нет в наличии
    (RANDOM_UUID(),'MARRIAGE'), --товар бракованный , ожидает возврата
    (RANDOM_UUID(),'RETURN'), --товар возвращен
    (RANDOM_UUID(),'SOLD'), -- товар продан
    (RANDOM_UUID(),'MOVING'); -- товар переезжает

INSERT INTO "user"(id, login , first_name, last_name, salary, password, enable, account_non_locked) VALUES
    (RANDOM_UUID(), 'Slava', 'Slava', 'Safonau', 52.000,
     '$2a$12$HZdZHUQTbGNQl8864NhY1.IgemB68Gddytmi5Oe8Ru1h4nxtea6Lq', true, true);

SET @user_slava_id = (SELECT id FROM "user" WHERE first_name = 'Slava');
SET @role_admin_id = (SELECT id FROM enum_role WHERE name = 'ROLE_ADMIN');
SET @role_user_id = (SELECT id FROM enum_role WHERE name = 'ROLE_USER');

INSERT INTO user_role (user_id , role_id) VALUES
    (@user_slava_id, @role_admin_id),
    (@user_slava_id, @role_user_id);

INSERT INTO trade_point (id, name, address, money_in_box, money_in_the_cash_register, sum_finish_off_the_money,
                         total_life_and_laptop, total_life, total_laptop) VALUES
    (RANDOM_UUID(), 'Хатаевича', 'Хатаевича 9', 0, 0, 0, 0, 0, 0),
    (RANDOM_UUID(), 'Ленина', 'Ленина 67', 0, 0, 0, 0, 0, 0),
    (RANDOM_UUID(), 'Вакзал', 'Курчатова', 0, 0, 0, 0, 0, 0);

INSERT INTO category (id, name) VALUES
    (RANDOM_UUID(), 'Телефоны'),
    (RANDOM_UUID(), 'Карты памяти'),
    (RANDOM_UUID(), 'Стёкла'),
    (RANDOM_UUID(), 'Плёнки'),
    (RANDOM_UUID(), 'Наушники'),
    (RANDOM_UUID(), 'Кабеля'),
    (RANDOM_UUID(), 'Чехлы'),
    (RANDOM_UUID(), 'Повербанки'),
    (RANDOM_UUID(), 'Клавиатуры'),
    (RANDOM_UUID(), 'Модемы'),
    (RANDOM_UUID(), 'Чистящие средства'),
    (RANDOM_UUID(), 'Сетевые удлинители'),
    (RANDOM_UUID(), 'Батарейки'),
    (RANDOM_UUID(), 'Картридеры'),
    (RANDOM_UUID(), 'USB - хабы'),
    (RANDOM_UUID(), 'Мыши'),
    (RANDOM_UUID(), 'Коврики для мыши'),
    (RANDOM_UUID(), 'СЗУ'),
    (RANDOM_UUID(), 'АЗУ'),
    (RANDOM_UUID(), 'Лампочки'),
    (RANDOM_UUID(), 'Фонарики'),
    (RANDOM_UUID(), 'Микрофоны'),
    (RANDOM_UUID(), 'Держатели'),
    (RANDOM_UUID(), 'Адаптеры');

INSERT INTO subcategory (id, name) VALUES
    (RANDOM_UUID(), 'Кнопки'),
    (RANDOM_UUID(), 'Смартфоны'),
    (RANDOM_UUID(), 'Глянцевые'),
    (RANDOM_UUID(), 'Матовые');
