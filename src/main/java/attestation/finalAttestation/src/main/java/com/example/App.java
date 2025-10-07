package com.example;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;

public class App {
    private Connection connection;

    public static void main(String[] args) {
        App app = new App();
        try {
            app.run();
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void run() {
        try {
            Properties props = loadProperties();

            // Проверяем, что исходная база существует
            checkSourceDatabase(props);

            // Мигрируем в целевую базу
//            runFlywayMigrations(props);

            // Работаем с целевой базой
            connectToDatabase(props);
            demonstrateCRUDOperations();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void checkSourceDatabase(Properties props) throws SQLException {
        String url = props.getProperty("db.source.url");
        String user = props.getProperty("db.source.username");
        String password = props.getProperty("db.source.password");

        try (Connection checkConn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Source database exists: " + url);
        }
    }

    private Properties loadProperties() throws Exception {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) {
                throw new Exception("Cannot find application.properties in classpath");
            }
            props.load(in);
        }
        return props;
    }

    private void connectToDatabase(Properties props) throws SQLException {
        String url = props.getProperty("db.source.url");
        String user = props.getProperty("db.source.username");
        String password = props.getProperty("db.source.password");

        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to database successfully!");
    }

    private void runFlywayMigrations(Properties props) {
        System.out.println("Running Flyway migrations...");

        String url = props.getProperty("flyway.url");
        String user = props.getProperty("flyway.user");
        String password = props.getProperty("flyway.password");

        // Create and configure Flyway
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .load();

        // Run migrations and get result
        MigrateResult result = flyway.migrate();

        System.out.println("Flyway migrations completed successfully!");
        System.out.println("Applied " + result.migrationsExecuted + " migration(s)");

        // Optional: Show details about applied migrations
        if (result.migrationsExecuted > 0) {
            System.out.println("Applied migrations:");
            for (var migration : result.migrations) {
                System.out.println("   - " + migration.version + " : " + migration.description);
            }
        }
    }


    private void demonstrateCRUDOperations() throws SQLException {
        System.out.println("\n=== DEMONSTRATING CRUD OPERATIONS ===\n");

        // Start transaction
        connection.setAutoCommit(false);

        try {
            // 1. Show existing recent orders first
            System.out.println("\n1. EXISTING RECENT ORDERS:");
            readLast5Orders();

            // 2. Insert new product and customer
            System.out.println("\n2. INSERT OPERATIONS:");
            int newProductId = insertProduct();
            int newCustomerId = insertCustomer();

            // 3. Create order (with current timestamp)
            System.out.println("\n3. CREATE ORDER:");
            int newOrderId = createOrder(newProductId, newCustomerId);

            // 4. Read last 5 orders again (should include the new one)
            System.out.println("\n4. READ LAST 5 ORDERS AFTER INSERT:");
            readLast5Orders();

            // 5. Update product price and quantity
            System.out.println("\n5. UPDATE OPERATIONS:");
            updateProduct(newProductId);

            // 6. Test SQL queries
            System.out.println("\n6. TESTING SQL QUERIES:");
            testSQLQueries();

            // Commit transaction
            connection.commit();
            System.out.println("\nAll operations completed successfully!");

        } catch (SQLException e) {
            // Rollback transaction in case of error
            connection.rollback();
            System.err.println("Transaction rolled back due to error: " + e.getMessage());

            // Continue with read-only operations even if insert fails
            System.out.println("\nContinuing with read-only operations...");

            try {
                System.out.println("\nREAD-ONLY OPERATIONS AFTER ERROR:");
                readLast5Orders();
                testSQLQueries();
            } catch (SQLException readException) {
                System.err.println("Error during read operations: " + readException.getMessage());
            }

        } finally {
            connection.setAutoCommit(true);
        }
    }

    private int insertProduct() throws SQLException {
        String sql = "INSERT INTO product (description, price, quantity, category) VALUES (?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Generate unique product name
            String uniqueDescription = "Тестовый товар Java App " + System.currentTimeMillis();
            stmt.setString(1, uniqueDescription);
            stmt.setBigDecimal(2, new java.math.BigDecimal("9999.99"));
            stmt.setInt(3, 100);
            stmt.setString(4, "Тестовая категория");

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Inserted new product with ID: " + id + ", description: " + uniqueDescription);
                return id;
            }
        }
        throw new SQLException("Failed to insert product");
    }

    private int insertCustomer() throws SQLException {
        String sql = "INSERT INTO customer (first_name, last_name, phone, email) VALUES (?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "Тест");
            stmt.setString(2, "Пользователь");
            stmt.setString(3, "+79990001122");

            // Generate unique email using timestamp
            String uniqueEmail = "test.user." + System.currentTimeMillis() + "@example.com";
            stmt.setString(4, uniqueEmail);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Inserted new customer with ID: " + id + ", email: " + uniqueEmail);
                return id;
            }
        }
        throw new SQLException("Failed to insert customer");
    }

    private int createOrder(int productId, int customerId) throws SQLException {
        String sql = "INSERT INTO order_table (product_id, customer_id, quantity, status_id) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, customerId);
            stmt.setInt(3, 2);
            stmt.setInt(4, 1); // status_id = 1 (Обрабатывается)

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Created new order with ID: " + id);
                return id;
            }
        }
        throw new SQLException("Failed to create order");
    }

    private void readLast5Orders() throws SQLException {
        String sql = """
            SELECT 
                o.id as order_id,
                o.order_date,
                c.first_name || ' ' || c.last_name as customer_name,
                p.description as product_description,
                o.quantity,
                os.status_name,
                (p.price * o.quantity) as total_price
            FROM order_table o
            JOIN customer c ON o.customer_id = c.id
            JOIN product p ON o.product_id = p.id
            JOIN order_status os ON o.status_id = os.id
            ORDER BY o.order_date DESC
            LIMIT 5
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("┌─────┬────────────────────┬──────────────────┬──────────────────────────┬──────────┬──────────────────┬─────────────┐");
            System.out.println("│ ID  │ Дата заказа        │ Покупатель       │ Товар                    │ Кол-во   │ Статус           │ Сумма       │");
            System.out.println("├─────┼────────────────────┼──────────────────┼──────────────────────────┼──────────┼──────────────────┼─────────────┤");

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                Timestamp orderDate = rs.getTimestamp("order_date");
                String customerName = rs.getString("customer_name");
                String productDesc = rs.getString("product_description");
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status_name");
                double totalPrice = rs.getDouble("total_price");

                // Format output for better readability
                String formattedDate = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm").format(orderDate);
                String shortProductDesc = productDesc.length() > 20 ? productDesc.substring(0, 17) + "..." : productDesc;
                String shortCustomerName = customerName.length() > 15 ? customerName.substring(0, 12) + "..." : customerName;

                System.out.printf("│ %-3d │ %-18s │ %-16s │ %-24s │ %-8d │ %-16s │ %-11.2f │\n",
                        orderId, formattedDate, shortCustomerName, shortProductDesc, quantity, status, totalPrice);
            }
            System.out.println("└─────┴────────────────────┴──────────────────┴──────────────────────────┴──────────┴──────────────────┴─────────────┘");
        }
    }

    private void updateProduct(int productId) throws SQLException {
        // Update price
        String updatePriceSql = "UPDATE product SET price = price * 1.05 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updatePriceSql)) {
            stmt.setInt(1, productId);
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Updated price for product ID " + productId + ". Rows affected: " + rowsUpdated);
        }

        // Update quantity
        String updateQuantitySql = "UPDATE product SET quantity = quantity - 5 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuantitySql)) {
            stmt.setInt(1, productId);
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Updated quantity for product ID " + productId + ". Rows affected: " + rowsUpdated);
        }
    }

    private void testSQLQueries() throws SQLException {
        System.out.println("\n=== TESTING SQL QUERIES ===\n");

        // Test query 1: Recent orders
        System.out.println("1. ЗАКАЗЫ ЗА ПОСЛЕДНИЕ 7 ДНЕЙ:");
        String recentOrdersSQL = """
        SELECT
            o.id as order_id,
            o.order_date,
            c.first_name || ' ' || c.last_name as customer_name,
            p.description as product_description,
            o.quantity,
            os.status_name,
            (p.price * o.quantity) as total_price
        FROM order_table o
        JOIN customer c ON o.customer_id = c.id
        JOIN product p ON o.product_id = p.id
        JOIN order_status os ON o.status_id = os.id
        WHERE o.order_date >= CURRENT_DATE - INTERVAL '7 days'
        ORDER BY o.order_date DESC
        """;

        executeAndPrintQuery(recentOrdersSQL, "Recent Orders");

        // Test query 2: Popular products
        System.out.println("\n2. ТОП-3 ПОПУЛЯРНЫХ ТОВАРА:");
        String popularProductsSQL = """
        SELECT
            p.description,
            p.category,
            COUNT(o.id) as order_count,
            SUM(o.quantity) as total_quantity_ordered
        FROM product p
        JOIN order_table o ON p.id = o.product_id
        GROUP BY p.id, p.description, p.category
        ORDER BY total_quantity_ordered DESC
        LIMIT 3
        """;

        executeAndPrintQuery(popularProductsSQL, "Popular Products");

        // Test query 3: Customers with total spent
        System.out.println("\n3. ПОКУПАТЕЛИ С ОБЩЕЙ СУММОЙ ЗАКАЗОВ:");
        String customersSQL = """
        SELECT
            c.first_name || ' ' || c.last_name as customer_name,
            c.email,
            COUNT(o.id) as total_orders,
            COALESCE(SUM(o.quantity * p.price), 0) as total_spent
        FROM customer c
        LEFT JOIN order_table o ON c.id = o.customer_id
        LEFT JOIN product p ON o.product_id = p.id
        GROUP BY c.id, c.first_name, c.last_name, c.email
        ORDER BY total_spent DESC
        LIMIT 5
        """;

        executeAndPrintQuery(customersSQL, "Top Customers");

        // Test query 4: Orders by status
        System.out.println("\n4. ЗАКАЗЫ ПО СТАТУСАМ:");
        String ordersByStatusSQL = """
        SELECT
            os.status_name,
            COUNT(o.id) as orders_count,
            COALESCE(AVG(p.price * o.quantity), 0) as avg_order_value
        FROM order_status os
        LEFT JOIN order_table o ON os.id = o.status_id
        LEFT JOIN product p ON o.product_id = p.id
        GROUP BY os.id, os.status_name
        ORDER BY os.id
        """;

        executeAndPrintQuery(ordersByStatusSQL, "Orders by Status");

        // Test query 5: Low stock products
        System.out.println("\n5. ТОВАРЫ С НИЗКИМ ЗАПАСОМ:");
        String lowStockSQL = """
        SELECT
            description,
            price,
            quantity,
            category
        FROM product
        WHERE quantity < 15
        ORDER BY quantity ASC
        LIMIT 5
        """;

        executeAndPrintQuery(lowStockSQL, "Low Stock Products");
    }

    private void executeAndPrintQuery(String sql, String title) throws SQLException {
        System.out.println("--- " + title + " ---");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            int totalRows = 0;

            // Print headers
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                // Shorten long column names for better display
                if (columnName.length() > 15) {
                    columnName = columnName.substring(0, 12) + "...";
                }
                System.out.printf("%-20s", columnName);
            }
            System.out.println();

            // Print separator
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", "--------------------");
            }
            System.out.println();

            // Print data
            while (rs.next()) {
                totalRows++;
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value != null) {
                        // Format numbers nicely
                        if (value.matches("-?\\d+\\.\\d+")) {
                            double num = Double.parseDouble(value);
                            if (num == (long) num) {
                                value = String.format("%d", (long) num);
                            } else {
                                value = String.format("%.2f", num);
                            }
                        }
                        // Shorten long values for better display
                        if (value.length() > 18) {
                            value = value.substring(0, 15) + "...";
                        }
                    } else {
                        value = "NULL";
                    }
                    System.out.printf("%-20s", value);
                }
                System.out.println();
            }

            if (totalRows == 0) {
                System.out.println("No data found");
            } else {
                System.out.println("Found " + totalRows + " rows");
            }

        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            throw e;
        }
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
