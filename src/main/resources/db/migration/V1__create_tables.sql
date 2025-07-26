CREATE TABLE payment_transaction (
    id UUID PRIMARY KEY,
    cash_amount DECIMAL(10, 2) DEFAULT 0.00,
    cashless_amount DECIMAL(10, 2) DEFAULT 0.00,
    credit_amount DECIMAL(10, 2) DEFAULT 0.00
);

CREATE TABLE enum_money_location (
    id UUID PRIMARY KEY,
    path VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE enum_type_payment (
    id UUID PRIMARY KEY,
    type VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE payment_transaction_money_location (
    id UUID PRIMARY KEY,
    payment_transaction_id UUID NOT NULL REFERENCES payment_transaction(id),
    enum_money_location_id UUID NOT NULL REFERENCES enum_money_location(id),
    enum_type_payment_id UUID NOT NULL REFERENCES enum_type_payment(id),
    amount DECIMAL(10, 2) NOT NULL
);

CREATE TABLE category (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    coefficient_sale DECIMAL(15, 2) DEFAULT 0.00
);
CREATE TABLE enum_role (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE trade_point (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    money_in_box DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    money_in_the_cash_register DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    sum_finish_off_the_money DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    total_life_and_laptop DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    total_life DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    total_laptop DECIMAL(10, 2) NOT NULL DEFAULT 0.00
);

CREATE TABLE "user" (
    id UUID PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL UNIQUE,
    salary DOUBLE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enable BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    trade_point_id UUID ,
    CONSTRAINT fk_trade_point FOREIGN KEY (trade_point_id)
                    REFERENCES trade_point(id)
                    ON DELETE SET NULL
);
CREATE TABLE refresh_token (
    id UUID PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id UUID NOT NULL REFERENCES "user"(id),
    expiry_date TIMESTAMP
);
CREATE TABLE user_role (
    user_id UUID NOT NULL REFERENCES "user"(id),
    role_id UUID NOT NULL REFERENCES enum_role(id),
    PRIMARY KEY (user_id, role_id)
);
CREATE TABLE subcategory (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    coefficient_sales DECIMAL(15, 2) DEFAULT 0.00
);
CREATE TABLE category_subcategory (
    id UUID PRIMARY KEY,
    category_id UUID NOT NULL REFERENCES category(id),
    subcategory_id UUID NOT NULL REFERENCES subcategory(id)
);
-- Уникальность пары
ALTER TABLE category_subcategory
    ADD CONSTRAINT uq_category_subcategory UNIQUE (category_id, subcategory_id);

CREATE TABLE history_trade_point (
    id UUID PRIMARY KEY,
    date_time_login TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id UUID NOT NULL REFERENCES "user"(id),
    trade_point_id UUID NOT NULL REFERENCES trade_point(id),
    date_time_logout TIMESTAMP
);
CREATE TABLE enum_currency (
    id UUID PRIMARY KEY,
    description VARCHAR(50) NOT NULL UNIQUE,
    rate DECIMAL(4, 2),
    last_update TIMESTAMP
);

CREATE TABLE enum_status_product (
    id UUID PRIMARY KEY,
    status VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE discount (
    id UUID PRIMARY KEY,
    discount_amount DECIMAL(15, 2) NOT NULL
);
CREATE TABLE product (
    id UUID PRIMARY KEY,
    brand VARCHAR(100),
    model VARCHAR(100),
    characteristics TEXT,
    quantity INT NOT NULL,
    currency_id UUID NOT NULL REFERENCES enum_currency(id),
    price_with_vat DECIMAL(10, 2) NOT NULL,
    price_selling DECIMAL(10, 2) NOT NULL,
    status_id UUID NOT NULL REFERENCES enum_status_product(id),
    date_of_arrival TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_accepted_product_id UUID NOT NULL REFERENCES "user"(id),
    user_sale_product_id UUID REFERENCES "user"(id),
    category_subcategory_id UUID NOT NULL REFERENCES category_subcategory(id),
    trade_point_id UUID NOT NULL REFERENCES trade_point(id),
    discount_id UUID REFERENCES discount(id)
);
CREATE TABLE update_product_history (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES product(id),
    user_id UUID NOT NULL REFERENCES "user"(id),
    date_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    old_brand VARCHAR(100),
    new_brand VARCHAR(100),
    old_model VARCHAR(100),
    new_model VARCHAR(100),
    old_characteristics TEXT,
    new_characteristics TEXT,
    old_quantity INT,
    new_quantity INT,
    old_currency_id UUID NOT NULL REFERENCES enum_currency(id),
    new_currency_id UUID NOT NULL REFERENCES enum_currency(id),
    old_price_with_vat DECIMAL(10, 2) NOT NULL,
    new_price_with_vat DECIMAL(10, 2) NOT NULL,
    old_price_selling DECIMAL(10, 2) NOT NULL,
    new_price_selling DECIMAL(10, 2) NOT NULL,
    old_status_product_id UUID NOT NULL REFERENCES enum_status_product(id),
    new_status_product_id UUID NOT NULL REFERENCES enum_status_product(id),
    old_category_subcategory_id UUID NOT NULL REFERENCES category_subcategory(id),
    new_category_subcategory_id UUID NOT NULL REFERENCES category_subcategory(id),
    old_trade_point_id UUID NOT NULL REFERENCES trade_point(id),
    new_trade_point_id UUID NOT NULL REFERENCES trade_point(id)
);

CREATE TABLE sale (
    id UUID PRIMARY KEY,
    total_price DECIMAL(10, 2) NOT NULL,
    sale_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    trade_point_id UUID NOT NULL REFERENCES trade_point(id),
    user_id UUID NOT NULL REFERENCES "user"(id),
    transaction_id UUID NOT NULL REFERENCES payment_transaction(id)
);
CREATE TABLE sales_products (
    id UUID PRIMARY KEY,
    sale_id UUID NOT NULL REFERENCES sale(id),
    product_id UUID NOT NULL REFERENCES product(id),
    price DECIMAL(10, 2) NOT NULL
);

