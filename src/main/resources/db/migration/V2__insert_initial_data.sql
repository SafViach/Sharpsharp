INSERT INTO enum_role (id, name) VALUES
    (RANDOM_UUID(), 'ROLE_ADMIN'),
    (RANDOM_UUID(), 'ROLE_USER');

SET @role_admin_id = (SELECT id FROM enum_role WHERE name = 'ROLE_ADMIN');
SET @role_user_id = (SELECT id FROM enum_role WHERE name = 'ROLE_USER');

INSERT INTO enum_type_payment (id, type) VALUES
    (RANDOM_UUID(), 'CASH'),
    (RANDOM_UUID(), 'NON_CASH'),
    (RANDOM_UUID(), 'CASH_AND_NON_CASH'),
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
    (RANDOM_UUID(),'AVAILABLE'), --товар есть в наличии и доступен к продаже
    (RANDOM_UUID(),'NOT AVAILABLE'), --товара нет в наличии
    (RANDOM_UUID(),'MARRIAGE'), --товар бракованный , ожидает возврата
    (RANDOM_UUID(),'RETURN'), --товар возвращен
    (RANDOM_UUID(),'SOLD'), -- товар продан
    (RANDOM_UUID(),'PENDING_APPROVAL'), -- ожидание одобрения
    (RANDOM_UUID(),'PENDING'), -- в ожидании
    (RANDOM_UUID(),'APPROVED'), -- одобренный
    (RANDOM_UUID(),'CANCEL'), -- отклоненный
    (RANDOM_UUID(),'REMOVABLE'), -- удаляемый
    (RANDOM_UUID(),'MOVING'); -- товар переезжает

SET @status_product_examination_id = (SELECT id FROM enum_status_product WHERE status = 'EXAMINATION');
SET @status_product_available_id = (SELECT id FROM enum_status_product WHERE status = 'AVAILABLE');
SET @status_product_not_available_id = (SELECT id FROM enum_status_product WHERE status = 'NOT AVAILABLE');
SET @status_product_marriage_id = (SELECT id FROM enum_status_product WHERE status = 'MARRIAGE');
SET @status_product_return_id = (SELECT id FROM enum_status_product WHERE status = 'RETURN');
SET @status_product_sold_id = (SELECT id FROM enum_status_product WHERE status = 'SOLD');
SET @status_product_moving_id = (SELECT id FROM enum_status_product WHERE status = 'MOVING');

INSERT INTO "user"(id, login , first_name, last_name, salary, password,
                   enable, account_non_locked) VALUES
    (RANDOM_UUID(), 'Slava', 'Слава', 'Safonau', 52.000,
     '$2a$12$2wRQZn14AiSuKDlAyRWT1OuZIt3DxQvfDcQfMjR9qTgZazuk9EIwy', true, true),
    (RANDOM_UUID(), 'Julia', 'Юля', 'Кононова', 5000,
    '$2a$12$zLf31wcwhCiTUg3paTHTiOOKgWkxL5mlJ8h2K4eXHHiuUPpUST69y',true, true),
    (RANDOM_UUID(), 'Daria', 'Дарья', 'Зайцева', 800,
     '$2a$12$UbtafAmKM80WgTDKVN7Ml.O.4y1E7OjILQR6ocFqAQ21eAYbCsFna',true, true),
    (RANDOM_UUID(), 'Bogdan', 'Богдан', 'ДаХз', 1000,
     '$2a$12$KgdNvje5eg14Ab7ylSz6OOS/.ru9dgT18gP0KGjAnUe67hGIEG4Oa',true, true),
    (RANDOM_UUID(), 'Alexey', 'Алексей', 'Паршников', 3000,
     '$2a$12$KWgAyOWco0/VGB2hMs1TpOoRiKOPQw2Ih5RrEhoK6ns3MLXJmqtnC',true, true),
    (RANDOM_UUID(), 'DariaP', 'Дарья', 'Посталовская', 78320,
     '$2a$12$Mk2R.SMfsnGw1vMhiKSmkOmU9RPczEBameOlioEB/Inu5HPIFrIO6',true, true),
    (RANDOM_UUID(), 'Natali', 'Наталья', 'Морозова', 78320,
     '$2a$12$BGJmb0862ma0aIMjvoEmVOLi/z6qKS4AZ/4KJ8odVMOd7VDjl4.Oa',true, true),
    (RANDOM_UUID(), 'DariaK', 'Дарья', 'Кучерова', 5460,
     '$2a$12$SLBsgz/Xw9ma8YfRLA2Kb.g7gA617nKgVEiPB02hQBvr.MEzGz0Me',true, true);

SET @user_slava_id = (SELECT id FROM "user" WHERE login = 'Slava');
SET @user_julia_id = (SELECT id FROM "user" WHERE login = 'Julia');
SET @user_daria_id = (SELECT id FROM "user" WHERE login = 'Daria');
SET @user_bogdan_id = (SELECT id FROM "user" WHERE login = 'Bogdan');
SET @user_alexey_id = (SELECT id FROM "user" WHERE login = 'Alexey');
SET @user_daria_p_id = (SELECT id FROM "user" WHERE login = 'DariaP');
SET @user_daria_k_id = (SELECT id FROM "user" WHERE login = 'DariaK');

INSERT INTO user_role (user_id , role_id) VALUES
    (@user_slava_id, @role_admin_id),
    (@user_slava_id, @role_user_id),
    (@user_alexey_id, @role_admin_id),
    (@user_alexey_id, @role_user_id),
    (@user_julia_id, @role_user_id),
    (@user_daria_id, @role_user_id),
    (@user_bogdan_id, @role_user_id),
    (@user_daria_p_id, @role_user_id),
    (@user_daria_k_id, @role_user_id);

INSERT INTO trade_point (id, name, address, money_in_box, money_in_the_cash_register, sum_finish_off_the_money,
                         total_life_and_laptop, total_life, total_laptop) VALUES
    (RANDOM_UUID(), 'Хатаевича', 'Хатаевича 9', 0, 0, 0, 0, 0, 0),
    (RANDOM_UUID(), 'Ленина', 'Ленина 67', 0, 0, 0, 0, 0, 0),
    (RANDOM_UUID(), 'Вакзал', 'Курчатова 1', 0, 0, 0, 0, 0, 0),
    (RANDOM_UUID(), 'Мазурова', 'Мазурова 63', 0, 0, 0, 0, 0, 0);

SET @trade_point_hataevicha_id = (SELECT id FROM trade_point WHERE name = 'Хатаевича');
SET @trade_point_lenino_id = (SELECT id FROM trade_point WHERE name = 'Ленина');
SET @trade_point_vaczal_id = (SELECT id FROM trade_point WHERE name = 'Вакзал');
SET @trade_point_mazyrova_id = (SELECT id FROM trade_point WHERE name = 'Мазурова');

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

SET @category_phone_id = (SELECT id FROM category WHERE name = 'Телефоны');
SET @category_films_id = (SELECT id FROM category WHERE name = 'Плёнки');
SET @category_rugs_id = (SELECT id FROM category WHERE name ='Коврики для мыши');
SET @category_glass_id = (SELECT id FROM category WHERE name = 'Стёкла');

INSERT INTO subcategory (id, name) VALUES
    (RANDOM_UUID(), 'Кнопки'),
    (RANDOM_UUID(), 'Смартфоны'),
    (RANDOM_UUID(), 'Глянцевые'),
    (RANDOM_UUID(), 'Матовые');

SET @subcategory_buttons_id = (SELECT id FROM subcategory WHERE name = 'Кнопки');
SET @subcategory_smartphones_id = (SELECT id FROM subcategory WHERE name = 'Смартфоны');
SET @subcategory_glossy_id = (SELECT id FROM subcategory WHERE name = 'Глянцевые');
SET @subcategory_matte_id = (SELECT id FROM subcategory WHERE name = 'Матовые');

INSERT INTO category_subcategory (id, category_id, subcategory_id) VALUES
    (RANDOM_UUID(), @category_phone_id, @subcategory_buttons_id),
    (RANDOM_UUID(), @category_phone_id, @subcategory_smartphones_id),
    (RANDOM_UUID(), @category_films_id, @subcategory_glossy_id),
    (RANDOM_UUID(), @category_films_id, @subcategory_matte_id),
    (RANDOM_UUID(), @category_glass_id, null),
    (RANDOM_UUID(), @category_rugs_id, null);

-- INSERT INTO discount (id , discount_amount) VALUES
--     (RANDOM_UUID(), 0),
--     (RANDOM_UUID(), 5),
--     (RANDOM_UUID(), 10),
--     (RANDOM_UUID(), 15),
--     (RANDOM_UUID(), 20),
--     (RANDOM_UUID(), 25),
--     (RANDOM_UUID(), 30),
--     (RANDOM_UUID(), 35),
--     (RANDOM_UUID(), 40),
--     (RANDOM_UUID(), 45),
--     (RANDOM_UUID(), 50),
--     (RANDOM_UUID(), 55),
--     (RANDOM_UUID(), 60),
--     (RANDOM_UUID(), 65),
--     (RANDOM_UUID(), 70),
--     (RANDOM_UUID(), 75),
--     (RANDOM_UUID(), 80),
--     (RANDOM_UUID(), 85),
--     (RANDOM_UUID(), 90),
--     (RANDOM_UUID(), 95),
--     (RANDOM_UUID(), 100);

-- SET @discount_zero_id = (SELECT id FROM discount WHERE discount_amount = 0);
-- SET @discount_five_id = (SELECT id FROM discount WHERE discount_amount = 5);
-- SET @discount_ten_id = (SELECT id FROM discount WHERE discount_amount = 10);
-- SET @discount_fifteen_id = (SELECT id FROM discount WHERE discount_amount = 15);
-- SET @discount_twenty_id = (SELECT id FROM discount WHERE discount_amount = 20);
-- SET @discount_twenty_five_id = (SELECT id FROM discount WHERE discount_amount = 25);
-- SET @discount_thirty_id = (SELECT id FROM discount WHERE discount_amount = 30);
-- SET @discount_thirty_five_id = (SELECT id FROM discount WHERE discount_amount = 35);
-- SET @discount_fourty_id = (SELECT id FROM discount WHERE discount_amount = 40);
-- SET @discount_fourty_five_id = (SELECT id FROM discount WHERE discount_amount = 45);
-- SET @discount_fifty_id = (SELECT id FROM discount WHERE discount_amount = 50);
-- SET @discount_fifty_five_id = (SELECT id FROM discount WHERE discount_amount = 55);
-- SET @discount_sixty_id = (SELECT id FROM discount WHERE discount_amount = 60);
-- SET @discount_sixty_fiveid = (SELECT id FROM discount WHERE discount_amount = 65);
-- SET @discount_seventy_id = (SELECT id FROM discount WHERE discount_amount = 70);
-- SET @discount_seventy_five_id = (SELECT id FROM discount WHERE discount_amount = 75);
-- SET @discount_eighty_id = (SELECT id FROM discount WHERE discount_amount = 80);
-- SET @discount_eighty_five_id = (SELECT id FROM discount WHERE discount_amount = 85);
-- SET @discount_ninety_id = (SELECT id FROM discount WHERE discount_amount = 90);
-- SET @discount_ninety_five_id = (SELECT id FROM discount WHERE discount_amount = 95);
-- SET @discount_one_hundred_id = (SELECT id FROM discount WHERE discount_amount = 100);

SET @category_subcategory_id_category_phone_subcategory_smartphones = (SELECT id FROM  category_subcategory WHERE
               category_id = @category_phone_id AND subcategory_id = @subcategory_smartphones_id);
SET @category_subcategory_id_category_phone_subcategory_buttons = (SELECT id FROM  category_subcategory WHERE
               category_id = @category_phone_id AND subcategory_id = @subcategory_buttons_id);
SET @category_subcategory_id_category_films_subcategory_glossy = (SELECT id FROM  category_subcategory WHERE
               category_id = @category_films_id AND subcategory_id = @subcategory_glossy_id);
SET @category_subcategory_id_category_films_subcategory_matte = (SELECT id FROM  category_subcategory WHERE
               category_id = @category_films_id AND subcategory_id = @subcategory_matte_id);
SET @category_subcategory_id_category_glass_subcategory_null = (SELECT id FROM category_subcategory WHERE
                category_id = @category_glass_id AND subcategory_id IS NULL );

INSERT INTO enum_currency(id, description, rate, last_update) VALUES
    (RANDOM_UUID(), 'USD', 0.00, '2025-08-27'),
    (RANDOM_UUID(), 'EUR', 0.00, '2025-08-27'),
    (RANDOM_UUID(), 'BYN', 1, '2025-08-27');

SET @currency_usd_id = (SELECT id FROM enum_currency WHERE description = 'USD');
SET @currency_eur_id = (SELECT id FROM enum_currency WHERE description = 'EUR');
SET @currency_byn_id = (SELECT id FROM enum_currency WHERE description = 'BYN');

INSERT INTO product (id, brand, model, characteristics, quantity, currency_id, currency_rate, price_with_vat,
                      price_selling, status_id, date_of_arrival, user_accepted_product_id,
                     user_sale_product_id, category_subcategory_id, trade_point_id, sku) VALUES

    (RANDOM_UUID(), 'Honor', 'x8c','8/256', 1, @currency_usd_id, '3.0', '199', '1092.90', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
     @trade_point_mazyrova_id, 'Телефоны-Смартфоны-Honor-x8c-8/256'),
    (RANDOM_UUID(), 'Honor', 'x8c','8/256', 1, @currency_usd_id,'3.0', '300', '1092.90', @status_product_available_id, '2025-08-28 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
     @trade_point_mazyrova_id, 'Телефоны-Смартфоны-Honor-x8c-8/256'),
    (RANDOM_UUID(), 'Redmi', 'Note 14','4/128', 1, @currency_usd_id,'3.0', '300', '1092.90', @status_product_marriage_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
     @trade_point_mazyrova_id, 'Телефоны-Смартфоны-Redmi-Note 14-4/128' ),
    (RANDOM_UUID(), 'Maxvi', 'P24','', 1, @currency_usd_id,'3.0', '87', '174.90', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_buttons,
     @trade_point_mazyrova_id, 'Телефоны-Кнопки-Maxvi-P24- ' ),
    (RANDOM_UUID(), 'Maxvi', 'P40','', 1, @currency_usd_id,'3.0', '74', '148.90', @status_product_marriage_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_buttons,
     @trade_point_mazyrova_id, 'Телефоны-Кнопки-Maxvi-P40- '),
    (RANDOM_UUID(), '', 'плёнка','', 1, @currency_usd_id,'3.0', '2', '15.00', @status_product_marriage_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_films_subcategory_glossy,
     @trade_point_mazyrova_id, 'Плёнки--плёнка- '),
    (RANDOM_UUID(), '', 'Honor','x5b Plus/x8b/x7c', 22, @currency_usd_id,'3.0', '5', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_mazyrova_id, 'Стёкла-null-Honor-x5b Plus/x8b/x7c'),
    (RANDOM_UUID(), '', 'Redmi','14C/NOTE 14 PRO/12', 15, @currency_usd_id,'3.0', '5', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_mazyrova_id, 'Стёкла-null-Redmi-14C/NOTE 14 PRO/12'),
    (RANDOM_UUID(), '', 'Samsung','A50/A55/23/56', 32, @currency_usd_id,'3.0', '5', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_mazyrova_id, 'Стёкла-null-Samsung-A50/A55/23/56'),
    (RANDOM_UUID(), '', 'Redmi','9A', 32, @currency_usd_id,'3.0', '5', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_mazyrova_id, 'Стёкла-null-Redmi-9A'),
    (RANDOM_UUID(), '', 'IPhone','15Pro/14', 32, @currency_usd_id,'3.0', '9', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_mazyrova_id, 'Стёкла-null-IPhone-15Pro/14'),
    (RANDOM_UUID(), 'IPhone', '16','8/256', 2, @currency_usd_id,'3.0', '1500', '5500', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
     @trade_point_mazyrova_id, 'Телефоны-Смартфоны-IPhone-16-8/256'),
    (RANDOM_UUID(), 'IPhone', '15','8/256', 2, @currency_usd_id,'3.0', '1400', '5000', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
     @trade_point_mazyrova_id, 'Телефоны-Смартфоны-IPhone-15-8/256'),
    (RANDOM_UUID(), 'IPhone', '15','6/512', 2, @currency_usd_id,'3.0', '1800', '6200', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
     @trade_point_mazyrova_id, 'Телефоны-Смартфоны-IPhone-15-6/512'),
    (RANDOM_UUID(), 'Redmi', '14C','4/128', 2, @currency_usd_id,'3.0', '150', '400', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
     @trade_point_mazyrova_id, 'Телефоны-Смартфоны-Redmi-14C-4/128'),
    (RANDOM_UUID(), '', 'IPhone','16Pro', 32, @currency_usd_id,'3.0', '9', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_mazyrova_id, 'Стёкла-null-IPhone-16Pro'),
    (RANDOM_UUID(), 'Honor', 'x5b Plus','4/128', 1, @currency_usd_id,'3.0', '200', '774.90', @status_product_examination_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
     @trade_point_hataevicha_id, 'Телефоны-Смартфоны-Honor-x5b Plus-4/128'),
    (RANDOM_UUID(), 'Poco', 'C75','4/128', 1, @currency_usd_id,'3.0', '155', '600.90', @status_product_marriage_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
    @trade_point_hataevicha_id, 'Телефоны-Смартфоны-Poco-C75-4/128'),
    (RANDOM_UUID(), 'Redmi', 'Note 14','4/128', 1, @currency_usd_id,'3.0', '205', '900.90', @status_product_marriage_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
     @trade_point_hataevicha_id, 'Телефоны-Смартфоны-Redmi-Note 14-4/128'),
    (RANDOM_UUID(), 'Maxvi', 'T100','', 1, @currency_byn_id,'3.0', '45', '90.90', @status_product_marriage_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_buttons,
     @trade_point_hataevicha_id, 'Телефоны-Кнопки-Maxvi-T100- '),
    (RANDOM_UUID(), 'Maxvi', 'T200','', 1, @currency_byn_id,'3.0', '53', '106.90', @status_product_examination_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_buttons,
     @trade_point_hataevicha_id, 'Телефоны-Кнопки-Maxvi-T100- '),
    (RANDOM_UUID(), '', 'плёнка','', 25, @currency_usd_id,'3.0', '2', '15.00', @status_product_marriage_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_films_subcategory_glossy,
     @trade_point_hataevicha_id, 'Плёнки-плёнка- '),
    (RANDOM_UUID(), '', 'плёнка','', 10, @currency_usd_id,'3.0', '2', '15.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_films_subcategory_glossy,
     @trade_point_hataevicha_id, 'Плёнки--плёнка- '),
    (RANDOM_UUID(), 'Honor', 'x5b Plus/x8b/x7c','', 22, @currency_usd_id,'3.0', '5', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_hataevicha_id, 'СТЁКЛА- -HONOR-X5B PLUS/X8B/X7C'),
    (RANDOM_UUID(), 'IPHONE', '17PRO','8/256', 1, @currency_usd_id,'3.2', '4', '999.99', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_phone_subcategory_smartphones,
     @trade_point_hataevicha_id, 'СТЁКЛА- -HONOR-X5B PLUS/X8B/X7C'),
    (RANDOM_UUID(), '', 'Redmi','14C/NOTE 14 PRO/12', 15, @currency_usd_id,'3.0', '5', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_hataevicha_id, 'Стёкла-null-Redmi-14C/NOTE 14 PRO/12'),
    (RANDOM_UUID(), '', 'Samsung','A50/A55/23/56', 32, @currency_usd_id,'3.0', '5', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_hataevicha_id, 'Стёкла-null-Samsung-A50/A55/23/56'),
    (RANDOM_UUID(), '', 'Redmi','9A', 32, @currency_usd_id,'3.0', '5', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_hataevicha_id, 'Стёкла-null-Redmi-9A'),
    (RANDOM_UUID(), '', 'IPhone','15Pro/14', 32, @currency_usd_id,'3.0', '9', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_hataevicha_id, 'Стёкла-null-IPhone-15Pro/14'),
    (RANDOM_UUID(), '', 'IPhone','16Pro', 32, @currency_usd_id,'3.0', '9', '12.00', @status_product_available_id, '2025-08-29 15:08:38.451562',
     @user_slava_id, null, @category_subcategory_id_category_glass_subcategory_null,
     @trade_point_hataevicha_id, 'Стёкла-null-IPhone-16Pro');




