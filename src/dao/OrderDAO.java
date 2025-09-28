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



    // Thêm mới đơn hàng kèm chi tiết
    public boolean insertOrder(Order order) {
        String sqlOrder = "INSERT INTO orders (id, userName, orderDate, totalAmount, note, orderDelive) VALUES (?,?,?,?,?,?)";
        String sqlDetail = "INSERT INTO order_details (orderDetailId, orderId, productId, unitPrice, quantity, amount) VALUES (?,?,?,?,?,?)";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder)) {
                psOrder.setString(1, order.getId());
                psOrder.setString(2, order.getUserName());
                psOrder.setDate(3, Date.valueOf(order.getOrderDate()));
                psOrder.setDouble(4, order.getTotalAmount());
                psOrder.setString(5, order.getNote());
                psOrder.setDate(6, Date.valueOf(order.getOrderDelivery())); 
                psOrder.executeUpdate();
            }

            for (OrderDetail od : order.getDetails()) {
                try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                    psDetail.setString(1, od.getOrderDetailId());
                    psDetail.setString(2, od.getOrderId());
                    psDetail.setString(3, od.getProductId());
                    psDetail.setDouble(4, od.getPrice());
                    psDetail.setInt(5, od.getQuantity());
                    psDetail.setDouble(6, od.getAmount());
                    psDetail.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
