-- ЗАПРОСОВ НА ЧТЕНИЕ
-- 1 Список всех заказов за последние 7 дней с именем покупателя и описанием товара
SELECT
    o.id as order_id,
    o.order_date,
    c.first_name || ' ' || c.last_name as customer_name,
    p.description as product_description,
    o.quantity,
    os.status_name,
    (p.price * o.quantity) as total_price
FROM order_table o  -- ИСПРАВЛЕНО: order → order_table
JOIN customer c ON o.customer_id = c.id
JOIN product p ON o.product_id = p.id
JOIN order_status os ON o.status_id = os.id
WHERE o.order_date >= CURRENT_DATE - INTERVAL '7 days'
ORDER BY o.order_date DESC;

-- 2. Топ-3 самых популярных товаров (по количеству заказов)
SELECT
    p.description,
    p.category,
    COUNT(o.id) as order_count,
    SUM(o.quantity) as total_quantity_ordered
FROM product p
JOIN order_table o ON p.id = o.product_id  -- ИСПРАВЛЕНО
GROUP BY p.id, p.description, p.category
ORDER BY total_quantity_ordered DESC
LIMIT 3;

-- 3. Покупатели с общей суммой всех заказов
SELECT
    c.first_name || ' ' || c.last_name as customer_name,
    c.email,
    COUNT(o.id) as total_orders,
    SUM(o.quantity * p.price) as total_spent
FROM customer c
LEFT JOIN order_table o ON c.id = o.customer_id  -- ИСПРАВЛЕНО
LEFT JOIN product p ON o.product_id = p.id
GROUP BY c.id, c.first_name, c.last_name, c.email
ORDER BY total_spent DESC NULLS LAST;

-- 4. Заказы по статусам с детальной информацией
SELECT
    os.status_name,
    COUNT(o.id) as orders_count,
    AVG(p.price * o.quantity) as avg_order_value
FROM order_status os
LEFT JOIN order_table o ON os.id = o.status_id  -- ИСПРАВЛЕНО
LEFT JOIN product p ON o.product_id = p.id
GROUP BY os.id, os.status_name
ORDER BY os.id;

-- 5. Товары, которые скоро закончатся на складе (меньше 10 штук)
SELECT
    description,
    price,
    quantity,
    category
FROM product
WHERE quantity < 10
ORDER BY quantity ASC;

-- 3 ЗАПРОСА НА ИЗМЕНЕНИЕ (UPDATE)
-- 1. Обновление количества на складе при покупке (симуляция продажи)
UPDATE product
SET quantity = quantity - 1
WHERE id = 1 AND quantity >= 1;

-- 2. Повышение цены на товары категории "Электроника" на 10%
UPDATE product
SET price = price * 1.1
WHERE category = 'Электроника';

-- 3. Обновление статуса заказов старше 3 дней на "Доставлен"
UPDATE order_table  -- ИСПРАВЛЕНО: order → order_table
SET status_id = (SELECT id FROM order_status WHERE status_name = 'Доставлен')
WHERE order_date < CURRENT_DATE - INTERVAL '3 days'
AND status_id != (SELECT id FROM order_status WHERE status_name = 'Доставлен');

-- 2 ЗАПРОСА НА УДАЛЕНИЕ (DELETE)

-- 1. Удаление клиентов без заказов
DELETE FROM customer
WHERE id NOT IN (SELECT DISTINCT customer_id FROM order_table);  -- ИСПРАВЛЕНО

-- 2. Удаление товаров, которых нет на складе и нет в активных заказах
DELETE FROM product
WHERE quantity = 0
AND id NOT IN (
    SELECT DISTINCT product_id
    FROM order_table  -- ИСПРАВЛЕНО
    WHERE status_id IN (
        SELECT id FROM order_status
        WHERE status_name IN ('Обрабатывается', 'Подтвержден')
    )
);