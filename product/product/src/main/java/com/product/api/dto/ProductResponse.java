package com.product.api.dto;

public class ProductResponse {

    private int product_id;
    private String gtin;
    private String product;
    private double price;

    public ProductResponse(int id, String gtin, String product, double price){
        this.product_id = id;
        this.gtin = gtin;
        this.product = product;
        this.price = price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
