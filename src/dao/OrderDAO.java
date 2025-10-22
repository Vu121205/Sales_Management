package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Order;
import model.OrderDetail;
import java.util.UUID;
import model.ProductStatistic;


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
        String sql = "SELECT id, order_date, delivery_date, created_by, address, note, total_amount FROM orders";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order(
                    rs.getString("id"),
                    rs.getDate("order_date").toLocalDate(),
                    rs.getDate("delivery_date").toLocalDate(),
                    rs.getString("created_by"),
                    rs.getString("address"),
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

//    Thống kê theo thời gian
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
//    Thống kê theo sản phẩm
    private String buildProductQuery(String type) {
        String baseQuery = """
            SELECT 
                p.name AS product_name,
                SUM(od.quantity) AS total_quantity,
                SUM(od.amount) AS total_revenue
            FROM orders o
            JOIN order_details od ON o.id = od.order_id
            JOIN product p ON od.product_id = p.id
            WHERE p.name = ?
        """;

        switch (type) {
            case "DAY":
                return baseQuery + " AND DATE(o.order_date) = CURDATE() GROUP BY p.name";
            case "WEEK":
                return baseQuery + " AND YEARWEEK(o.order_date, 1) = YEARWEEK(CURDATE(), 1) GROUP BY p.name";
            case "MONTH":
                return baseQuery + " AND MONTH(o.order_date) = MONTH(CURDATE()) AND YEAR(o.order_date) = YEAR(CURDATE()) GROUP BY p.name";
            case "YEAR":
                return baseQuery + " AND YEAR(o.order_date) = YEAR(CURDATE()) GROUP BY p.name";
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
    
    public ProductStatistic getProductStatistic(String type, String productName) throws SQLException {
        String sql = buildProductQuery(type);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProductStatistic(
                        rs.getString("product_name"),
                        rs.getInt("total_quantity"),
                        rs.getDouble("total_revenue")
                    );
                } else {
                    // Nếu chưa bán sản phẩm nào
                    return new ProductStatistic(productName, 0, 0.0);
                }
            }
        }
    }


//    Phần của USER
    public List<Order> getOrdersByUser(String username) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, order_date, delivery_date, created_by, address, note, total_amount "
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
                        rs.getString("address"),
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

    // Sinh OrderId
    public String generateOrderId() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0,9).toUpperCase();
        return "O" + uuid;  // VD: O9F8A1B2C3
    }

    // Sinh OrderDetailId
    public String generateOrderDetailId() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "OD" + uuid; // VD: OD7C2D9F8A1
    }
 
    // Thêm Order vào bảng Orders
    public void insertOrder(String id, String orderDate, String deliveryDate,
                            String createdBy, String address, String note, double total) throws SQLException {
        String sql = "INSERT INTO Orders (id, order_date, delivery_date, created_by, address, note, total_amount) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, orderDate);
            ps.setString(3, deliveryDate);
            ps.setString(4, createdBy);
            ps.setString(5, address);
            ps.setString(6, note);
            ps.setDouble(7, total);
            ps.executeUpdate();
        }
    }
    
    // Thêm OrderDetail vào bảng Order_Details
    public void insertOrderDetail(String id, String orderId, String productId,
                                  double price, int quantity, double amount) throws SQLException {
        String sql = "INSERT INTO Order_Details (id, order_id, product_id, price, quantity) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, orderId);
            ps.setString(3, productId);
            ps.setDouble(4, price);
            ps.setInt(5, quantity);
            ps.executeUpdate();
        }

    }
    
    public void updateOrderInfo(String orderId, String address, String note) throws SQLException {
        String sql = "UPDATE Orders SET address=?, note=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address);
            ps.setString(2, note);
            ps.setString(3, orderId);
            ps.executeUpdate();
        }
    }

}
