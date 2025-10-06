-- Заполнение справочника статусов заказов
INSERT INTO order_status (status_name) VALUES
('Обрабатывается'),
('Подтвержден'),
('Отправлен'),
('Доставлен'),
('Отменен')
ON CONFLICT (status_name) DO NOTHING;