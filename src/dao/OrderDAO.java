package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import model.Order;
import model.OrderDetail;


public class OrderDAO {
    private Connection conn;

    public OrderDAO() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/salesmanagement?useSSL=false&serverTimezone=UTC",
                    "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy tất cả đơn hàng kèm chi tiết
    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, order_date, delivery_date, created_by, note, total_amount FROM orders";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order(
                    rs.getString("id"),
                    rs.getDate("order_date").toLocalDate(),
                    rs.getDate("delivery_date").toLocalDate(),
                    rs.getString("created_by"),
                    rs.getString("note"),
                    rs.getDouble("total_amount")
                );
                orders.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Lấy chi tiết đơn hàng theo orderId
    public List<OrderDetail> getOrderDetails(String orderId) {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM order_details WHERE order_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail od = new OrderDetail(
                        rs.getString("id"),
                        rs.getString("order_id"),
                        rs.getString("product_id"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                    );
                    details.add(od);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }
    
    public int getTotalOrders(String type) throws SQLException {
        String sql = buildQuery(type);
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total_orders");
            }
        }
        return 0;
    }

    public double getTotalRevenue(String type) throws SQLException {
        String sql = buildQuery(type);
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total_revenue");
            }
        }
        return 0;
    }

    private String buildQuery(String type) {
        switch (type) {
            case "DAY":
                return "SELECT COUNT(DISTINCT o.id) AS total_orders, SUM(od.amount) AS total_revenue "
                        + "FROM orders o "
                        + "JOIN order_details od ON o.id = od.order_id "
                        + "WHERE DATE(o.order_date) = CURDATE()";
            case "WEEK":
                return "SELECT COUNT(DISTINCT o.id) AS total_orders, SUM(od.amount) AS total_revenue "
                        + "FROM orders o "
                        + "JOIN order_details od ON o.id = od.order_id "
                        + "WHERE YEARWEEK(o.order_date, 1) = YEARWEEK(CURDATE(), 1)";
            case "MONTH":
                return "SELECT COUNT(DISTINCT o.id) AS total_orders, SUM(od.amount) AS total_revenue "
                        + "FROM orders o "
                        + "JOIN order_details od ON o.id = od.order_id "
                        + "WHERE YEAR(o.order_date) = YEAR(CURDATE()) AND MONTH(o.order_date) = MONTH(CURDATE())";
            case "YEAR":
                return "SELECT COUNT(DISTINCT o.id) AS total_orders, SUM(od.amount) AS total_revenue "
                        + "FROM orders o "
                        + "JOIN order_details od ON o.id = od.order_id "
                        + "WHERE YEAR(o.order_date) = YEAR(CURDATE())";
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
    
//    Phần của USER
    public List<Order> getOrdersByUser(String username) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, order_date, delivery_date, created_by, note, total_amount "
                   + "FROM orders WHERE created_by = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order(
                        rs.getString("id"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getDate("delivery_date").toLocalDate(),
                        rs.getString("created_by"),
                        rs.getString("note"),
                        rs.getDouble("total_amount")
                    );
                    orders.add(o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

}
