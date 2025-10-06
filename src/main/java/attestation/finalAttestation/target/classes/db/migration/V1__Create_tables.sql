-- Создание таблицы статусов заказов
CREATE TABLE IF NOT EXISTS order_status (
    id SERIAL PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE
);

COMMENT ON TABLE order_status IS 'Справочник статусов заказов';
COMMENT ON COLUMN order_status.id IS 'Уникальный идентификатор статуса';
COMMENT ON COLUMN order_status.status_name IS 'Наименование статуса';

-- Создание таблицы товаров
CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    category VARCHAR(100) NOT NULL
);

COMMENT ON TABLE product IS 'Таблица товаров';
COMMENT ON COLUMN product.id IS 'Уникальный идентификатор товара';
COMMENT ON COLUMN product.description IS 'Описание товара';
COMMENT ON COLUMN product.price IS 'Цена товара (должна быть >= 0)';
COMMENT ON COLUMN product.quantity IS 'Количество на складе (должно быть >= 0)';
COMMENT ON COLUMN product.category IS 'Категория товара';

-- Создание таблицы покупателей
CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE
);

COMMENT ON TABLE customer IS 'Таблица покупателей';
COMMENT ON COLUMN customer.id IS 'Уникальный идентификатор покупателя';
COMMENT ON COLUMN customer.first_name IS 'Имя покупателя';
COMMENT ON COLUMN customer.last_name IS 'Фамилия покупателя';
COMMENT ON COLUMN customer.phone IS 'Телефон покупателя';
COMMENT ON COLUMN customer.email IS 'Email покупателя (уникальный)';

-- Создание таблицы заказов
CREATE TABLE IF NOT EXISTS order_table (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES product(id),
    customer_id INTEGER NOT NULL REFERENCES customer(id),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    status_id INTEGER NOT NULL REFERENCES order_status(id)
);

COMMENT ON TABLE order_table IS 'Таблица заказов';
COMMENT ON COLUMN order_table.id IS 'Уникальный идентификатор заказа';
COMMENT ON COLUMN order_table.product_id IS 'Внешний ключ на товар';
COMMENT ON COLUMN order_table.customer_id IS 'Внешний ключ на покупателя';
COMMENT ON COLUMN order_table.order_date IS 'Дата и время заказа';
COMMENT ON COLUMN order_table.quantity IS 'Количество товара в заказе';
COMMENT ON COLUMN order_table.status_id IS 'Статус заказа';
