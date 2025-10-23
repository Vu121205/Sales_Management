package dao;

import dao.DBConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.sql.Date;
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
 
    //Thống kê theo ngày và sản phẩm
    public ProductStatistic getProductStatisticByRange(LocalDate startDate, LocalDate endDate, String productName) throws SQLException {
            String sql = """
                SELECT 
                    SUM(od.quantity) AS total_quantity,
                    SUM(od.amount) AS total_revenue
                FROM Order_Details od
                JOIN Orders o ON od.order_id = o.id
                JOIN Product p ON od.product_id = p.id
                WHERE o.order_date BETWEEN ? AND ?
                AND p.name = ?
            """;

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setDate(1, Date.valueOf(startDate));
                ps.setDate(2, Date.valueOf(endDate));
                ps.setString(3, productName);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int quantity = rs.getInt("total_quantity");
                    double revenue = rs.getDouble("total_revenue");
                    return new ProductStatistic(productName, quantity, revenue);
                }

            } catch (SQLException e) {
                System.err.println("Lỗi khi thống kê theo ngày và sản phẩm: " + e.getMessage());
            }
            return new ProductStatistic(productName, 0, 0);
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
