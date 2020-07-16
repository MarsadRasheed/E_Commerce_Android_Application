package com.hamlet.MrFixer;

public class CartItem {

    private String id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String salePrice;
    private String regularPrice;
    private String category;
    private String imageUrl;
    private String quantity;

    public CartItem(){}

    public CartItem(ExampleItem item,String quantity){
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.startDate = item.getStartDate();
        this.endDate = item.getEndDate();
        this.salePrice = item.getSalePrice();
        this.regularPrice = item.getRegularPrice();
        this.category = item.getCategory();
        this.imageUrl = item.getImageUrl();
        this.quantity = quantity;
    }

    public CartItem(String id, String name, String description, String startDate, String endDate, String salePrice, String regularPrice, String category, String imageUrl, String quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.salePrice = salePrice;
        this.regularPrice = regularPrice;
        this.category = category;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", salePrice='" + salePrice + '\'' +
                ", regularPrice='" + regularPrice + '\'' +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
