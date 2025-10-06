-- Создание индексов для оптимизации запросов
CREATE INDEX IF NOT EXISTS idx_order_product_id ON order_table(product_id);
CREATE INDEX IF NOT EXISTS idx_order_customer_id ON order_table(customer_id);
CREATE INDEX IF NOT EXISTS idx_order_date ON order_table(order_date);
CREATE INDEX IF NOT EXISTS idx_order_status_id ON order_table(status_id);
CREATE INDEX IF NOT EXISTS idx_product_category ON product(category);