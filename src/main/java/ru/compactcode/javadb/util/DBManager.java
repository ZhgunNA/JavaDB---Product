package ru.compactcode.javadb.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ru.compactcode.javadb.model.Product;

/**
 * Устанавливает соединение с MySQL, хранит параметры подключения, 
 * выполняет CRUD-операции
 */
public class DBManager {

    // Строка подключения и учетные данные
    private final String url;
    private final String user;
    private final String password;

    /**
     * Конструктор инициализирует параметры подключения и загружает JDBC-драйвер
     * MySQL.
     */
    public DBManager(String host, int port, String dbName, String user, String password) {
        this.url = String.format(
                "jdbc:mysql://%s:%d/%s?serverTimezone=UTC&useSSL=false",
                host, port, dbName
        );
        this.user = user;
        this.password = password;

        // Регистрация JDBC-драйвера
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }

    /**
     * Получение всех записей из таблицы products. Используется для
     * заполненияJTable. Получение
     */
    public List<Product> getAllProducts() throws SQLException {
        List<Product> result = new ArrayList<>();
        String sql = "SELECT id, name, description, price, stock FROM products ORDER BY id";

        try (Connection c = getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        }
        return result;
    }

    // Создает и возвращает соединение с БД. Используется всеми методами ниже.
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Получение всех записей из таблицы products. Используется для заполнения
     * JTable.
     */
    // Добавление нового продукта в таблицу. Используется кнопкой "Add".
    public int insertProduct(Product p) throws SQLException {
        // Если id задан (>0) — вставляем явно, иначе даём БД сгенерировать
        if (p.getId() > 0) {
            String sql = "INSERT INTO products (id, name, description, price, stock) VALUES (?, ?, ?, ?, ?)";
            try (Connection c = getConnection();
                    PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setInt(1, p.getId());
                ps.setString(2, p.getName());
                ps.setString(3, p.getDescription());
                ps.setDouble(4, p.getPrice());
                ps.setInt(5, p.getStock());

                return ps.executeUpdate();
            }
        } else {
            String sql = "INSERT INTO products (name, description, price, stock) VALUES (?, ?, ?, ?)";
            try (Connection c = getConnection();
                    PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, p.getName());
                ps.setString(2, p.getDescription());
                ps.setDouble(3, p.getPrice());
                ps.setInt(4, p.getStock());

                int affected = ps.executeUpdate();

                // Получение сгенерированного ID (если БД сгенерировала)
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        p.setId(keys.getInt(1));
                    }
                }
                return affected;
            }
        }
    }

    // Обновление существующей записи. Используется кнопкой "Edit".
    public int updateProduct(Product p) throws SQLException {
        String sql = "UPDATE products SET name=?, description=?, price=?, stock=? WHERE id=?";

        try (Connection c = getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getId());

            return ps.executeUpdate();
        }
    }

    //Удаление продукта по идентификатору. Используется кнопкой "Delete".
    public int deleteProductById(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id=?";

        try (Connection c = getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}
