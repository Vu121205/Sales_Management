package model;

/**
 *
 * @author ADMIN
 */
public class OrderDetail {
    private String orderDetailId;  // Mã chi tiết
    private String orderId;        // Mã đơn hàng
    private String productId;      // Mã sản phẩm
    private double price;      // Đơn giá
    private int quantity;          // Số lượng
    private double amount;         // Thành tiền = unitPrice * quantity

    // Constructor đầy đủ
    public OrderDetail(String orderDetailId, String orderId, String productId, double price, int quantity) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.amount = price * quantity;
    }

    // Constructor khi chưa có orderDetailId (auto tạo từ DB)
    public OrderDetail(String orderId, String productId, double price, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.amount = price * quantity;
    }

    // Getter & Setter
    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        this.amount = this.price * this.quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.amount = this.price * this.quantity;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderDetailId='" + orderDetailId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", unitPrice=" + price +
                ", quantity=" + quantity +
                ", amount=" + amount +
                '}';
    }
}
