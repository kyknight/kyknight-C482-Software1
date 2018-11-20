/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author kyleighknight
 */
public class Product {
    //Instance Variables
    private static ObservableList<Part> parts = FXCollections.observableArrayList();
    private final IntegerProperty productID, productInventoryLevel, productMin, productMax;
    private final StringProperty productName;
    private final DoubleProperty productPricePerUnit;
    
    //Constructor
    public Product(){
        productID = new SimpleIntegerProperty();
        productName = new SimpleStringProperty();
        productPricePerUnit = new SimpleDoubleProperty();
        productInventoryLevel = new SimpleIntegerProperty();
        productMin = new SimpleIntegerProperty();
        productMax = new SimpleIntegerProperty();
    }
    
    public IntegerProperty productIDProperty(){
        return productID;
    }
    
    public StringProperty productNameProperty(){
        return productName;
    }
    
    public DoubleProperty productPricePerUnitProperty(){
        return productPricePerUnit;
    }
    
    public IntegerProperty productInventoryLevelProperty(){
        return productInventoryLevel;
    }

    //Getters and Setters
    public int getProductID(){
        return this.productID.get();
    }
    
    public void setProductID(int productID){
        this.productID.set(productID);
    }
    
    public String getProductName(){
        return this.productName.get();
    }
    
    public void setProductName(String productName){
        this.productName.set(productName);
    }
    
    public double getProductPricePerUnit(){
        return this.productPricePerUnit.get();
    }
    
    public void setProductPricePerUnit(double productPricePerUnit){
        this.productPricePerUnit.set(productPricePerUnit);
    }
    
    public int getProductInventoryLevel(){
        return this.productInventoryLevel.get();
    }
    
    public void setProductInventoryLevel(int productInventoryLevel){
        this.productInventoryLevel.set(productInventoryLevel);
    }
    
    public int getProductMin(){
        return this.productMin.get();
    }
    
    public void setProductMin(int productMin){
        this.productMin.set(productMin);
    }
    
    public int getProductMax(){
        return this.productMax.get();
    }
    
    public void setProductMax(int productMax){
        this.productMax.set(productMax);
    }
    
    public ObservableList getProductParts() {
        return parts;
    }

    public void setProductParts(ObservableList<Part> parts) {
        this.parts = parts;
    }
    
    //Validation Method
    public static String isProductValid(String productName, int productMin, int partMax, int productInventory, double partPrice, ObservableList<Part> parts, String message) {
        double sumParts = 0.00;
        for (int i = 0; i < parts.size(); i++) {
            sumParts = sumParts + parts.get(i).getPartPricePerUnit();
        }
        if (productName.equals("")) {
            message = message + ("Name field is blank.");
        }
        if (productMin < 0) {
            message = message + ("The inventory must be greater than 0.");
        }
        if (partPrice < 0) {
            message = message + ("The price must be greater than 0.");
        }
        if (productMin > partMax) {
            message = message + ("The inventory MIN must be less than the MAX.");
        }
        if (productInventory < productMin || productInventory > partMax) {
            message = message + ("Part inventory must be between MIN and MAX values.");
        }
        if (parts.size() < 1) {
            message = message + ("Product must contain at least 1 part.");
        }
        if (sumParts > partPrice) {
            message = message + ("Product price must be greater than cost of parts.");
        }
        return message;
    }
}
