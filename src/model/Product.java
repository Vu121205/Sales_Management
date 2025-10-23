package model;


public class Product {
    private String id;
    private String name;
    private Double price;
    private int quantity;
    private String categoryId; // tham chiếu đến bảng categories 
    private String description;
    private String image;
   
    public Product() {
    }

    public Product(String id, String name, Double price, int quantity, String categoryId, String description, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.description = description;
        this.image = image;
    }

    // Getter & Setter
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCategoryId() { return categoryId; }

    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }

    public int getQuantity() { return quantity;}

    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public String getDescription() { return description; }
    
    public void setDescription(String description) { this.description = description; }
    
    public String getImage() { return image; }
    
    public void setImage(String image) { this.image = image; }
}
