package model;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class Order {
    private String id;
    private LocalDate orderDate;
    private LocalDate orderDelivery; // ngày giao hàng
    private String createdBy;   // thêm trường này
    private String note; // ghi chú
    private Double totalAmount;
    private List<OrderDetail> details; // danh sách chi tiết đơn hàng

    public Order() {
    }

    public Order(String id, LocalDate orderDate, LocalDate orderDelivery,
                 String createdBy, String note, Double totalAmount) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderDelivery = orderDelivery;
        this.createdBy = createdBy;
        this.note = note;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getOrderDelivery() {
        return orderDelivery; 
    }
    
    public void setOrderDelivery(LocalDate orderDelivery) {
        this.orderDelivery = orderDelivery; 
    }

    public String getCreatedBy() {
        return createdBy; 
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy; 
    }
    
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + orderDelivery +
                ", createdBy='" + createdBy + '\'' +
                ", note='" + note + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
