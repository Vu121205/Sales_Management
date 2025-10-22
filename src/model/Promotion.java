package model;

import java.time.LocalDate;

public class Promotion {
    private String id;
    private String name;
    private double discount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public Promotion(String id, String name, double discount, LocalDate startDate, LocalDate endDate, String description) {
        this.id = id;
        this.name = name;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }
    // Getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
