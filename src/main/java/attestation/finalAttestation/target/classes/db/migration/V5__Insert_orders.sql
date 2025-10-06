-- Заполнение тестовыми данными заказов
INSERT INTO order_table (product_id, customer_id, order_date, quantity, status_id) VALUES
(1, 1, CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 4),
(2, 2, CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 3),
(3, 3, CURRENT_TIMESTAMP - INTERVAL '3 days', 2, 2),
(4, 4, CURRENT_TIMESTAMP - INTERVAL '4 days', 1, 1),
(5, 5, CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 4),
(6, 6, CURRENT_TIMESTAMP - INTERVAL '6 days', 3, 3),
(7, 7, CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 2),
(8, 8, CURRENT_TIMESTAMP - INTERVAL '2 days', 2, 1),
(9, 9, CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 4),
(10, 10, CURRENT_TIMESTAMP - INTERVAL '7 days', 1, 3)
ON CONFLICT DO NOTHING;