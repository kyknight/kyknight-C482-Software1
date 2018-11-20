/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.*;

/**
 *
 * @author kyleighknight
 */
public abstract class Part {

    private final StringProperty partName;
    private final IntegerProperty partID, partInventoryLevel, partMin, partMax;
    private final DoubleProperty partPricePerUnit;
    
    //Constructor
    public Part(){
        partID = new SimpleIntegerProperty();
        partName = new SimpleStringProperty();
        partInventoryLevel = new SimpleIntegerProperty();
        partPricePerUnit = new SimpleDoubleProperty();
        partMin = new SimpleIntegerProperty();
        partMax = new SimpleIntegerProperty();
    }
    
    public IntegerProperty partIDProperty(){
        return partID;
    }
    
    public StringProperty partNameProperty(){
        return partName;
    }
    
    public DoubleProperty partPricePerUnitProperty(){
        return partPricePerUnit;
    }
    
    public IntegerProperty partInventoryLevelProperty(){
        return partInventoryLevel;
    }

    //Getters and Setters
    public String getPartName() {
        return partName.get();
    }

    public void setPartName(String partName){
        this.partName.set(partName);
    }
    
    public int getPartID() {
        return partID.get();
    }

    public void setPartID(int partID){
        this.partID.set(partID);
    } 
    
    public int getPartInventoryLevel() {
        return partInventoryLevel.get();
    }
    
    public void setPartInventoryLevel(int partInventoryLevel){
        this.partInventoryLevel.set(partInventoryLevel);
    }

    public int getPartMin() {
        return partMin.get();
    }
    
    public void setPartMin(int partMin){
        this.partMin.set(partMin);
    }

    public int getPartMax() {
        return partMax.get();
    }
    
    public void setPartMax(int partMax){
        this.partMax.set(partMax);
    }

    public double getPartPricePerUnit() {
        return partPricePerUnit.get();
    }
    
    public void setPartPricePerUnit(double partPricePerUnit){
        this.partPricePerUnit.set(partPricePerUnit);
    }
    
    //Validators
    public static String isPartValid(String partName, int partMin, int partMax, int partInventory, double partPrice, String errorMessage) {
        if (partName == null) {
            errorMessage = errorMessage + ("Name field is blank.");
        }
        if (partInventory < 0) {
            errorMessage = errorMessage + ("The inventory must be greater than 0.");
        }
        if (partPrice < 0) {
            errorMessage = errorMessage + ("The price must be greater than 0.");
        }
        if (partMin > partMax) {
            errorMessage = errorMessage + ("The inventory MIN must be less than the MAX.");
        }
        if (partInventory < partMin || partInventory > partMax) {
            errorMessage = errorMessage + ("Part inventory must be between MIN and MAX values.");
        }
        return errorMessage;
    }
}
