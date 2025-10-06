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

-- Создание таблицы заказов (используем order_table вместо order, т.к. order - зарезервированное слово)
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

-- Создание индексов
CREATE INDEX IF NOT EXISTS idx_order_product_id ON order_table(product_id);
CREATE INDEX IF NOT EXISTS idx_order_customer_id ON order_table(customer_id);
CREATE INDEX IF NOT EXISTS idx_order_date ON order_table(order_date);
CREATE INDEX IF NOT EXISTS idx_order_status_id ON order_table(status_id);
CREATE INDEX IF NOT EXISTS idx_product_category ON product(category);

-- Заполнение тестовыми данными
INSERT INTO order_status (status_name) VALUES
('Обрабатывается'),
('Подтвержден'),
('Отправлен'),
('Доставлен'),
('Отменен')
ON CONFLICT (status_name) DO NOTHING;

INSERT INTO product (description, price, quantity, category) VALUES
('Ноутбук Lenovo IdeaPad', 45000.00, 10, 'Электроника'),
('Смартфон Samsung Galaxy', 35000.00, 15, 'Электроника'),
('Наушники Sony WH-1000XM4', 25000.00, 20, 'Аксессуары'),
('Книга "Java для начинающих"', 1500.00, 30, 'Книги'),
('Кофемашина DeLonghi', 30000.00, 5, 'Бытовая техника'),
('Футболка хлопковая', 1200.00, 50, 'Одежда'),
('Кресло офисное', 8000.00, 8, 'Мебель'),
('Мышь беспроводная', 1500.00, 25, 'Аксессуары'),
('Монитор 24"', 18000.00, 12, 'Электроника'),
('Чайник электрический', 2000.00, 18, 'Бытовая техника')
ON CONFLICT DO NOTHING;

INSERT INTO customer (first_name, last_name, phone, email) VALUES
('Иван', 'Петров', '+79161234567', 'ivan.petrov@mail.ru'),
('Мария', 'Сидорова', '+79162345678', 'maria.sidorova@mail.ru'),
('Алексей', 'Козлов', '+79163456789', 'alexey.kozlov@mail.ru'),
('Елена', 'Николаева', '+79164567890', 'elena.nikolaeva@mail.ru'),
('Дмитрий', 'Васильев', '+79165678901', 'dmitry.vasiliev@mail.ru'),
('Ольга', 'Иванова', '+79166789012', 'olga.ivanova@mail.ru'),
('Сергей', 'Смирнов', '+79167890123', 'sergey.smirnov@mail.ru'),
('Анна', 'Кузнецова', '+79168901234', 'anna.kuznetsova@mail.ru'),
('Павел', 'Попов', '+79169012345', 'pavel.popov@mail.ru'),
('Наталья', 'Лебедева', '+79160123456', 'natalya.lebedeva@mail.ru')
ON CONFLICT DO NOTHING;

INSERT INTO order_table (product_id, customer_id, order_date, quantity, status_id) VALUES
(1, 1, '2024-01-15 10:30:00', 1, 4),
(2, 2, '2024-01-14 14:20:00', 1, 3),
(3, 3, '2024-01-13 09:15:00', 2, 2),
(4, 4, '2024-01-12 16:45:00', 1, 1),
(5, 5, '2024-01-11 11:20:00', 1, 4),
(6, 6, '2024-01-10 13:30:00', 3, 3),
(7, 7, '2024-01-09 15:40:00', 1, 2),
(8, 8, '2024-01-08 12:25:00', 2, 1),
(9, 9, '2024-01-07 17:10:00', 1, 4),
(10, 10, '2024-01-06 10:05:00', 1, 3),
(1, 2, '2024-01-05 14:50:00', 1, 2),
(3, 1, '2024-01-04 08:35:00', 1, 1)
ON CONFLICT DO NOTHING;