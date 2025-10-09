package model;

public class ProductStatistic {
    private String productName;
    private int totalQuantity;
    private double totalRevenue;

    public ProductStatistic(String productName, int totalQuantity, double totalRevenue) {
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public String getProductName() { return productName; }
    public int getTotalQuantity() { return totalQuantity; }
    public double getTotalRevenue() { return totalRevenue; }
}
